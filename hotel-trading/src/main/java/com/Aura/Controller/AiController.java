package com.Aura.Controller;

import com.Aura.Mapper.OrderMapper;
import com.Aura.utils.AiService;
import com.Aura.utils.UserContext;
import com.Aura.common.Result;
import com.Aura.entity.Hotel;
import com.Aura.entity.OrderStats;
import com.Aura.entity.Room;
import com.Aura.feign.ResourceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "AI 助手")
@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ResourceClient resourceClient;

    @Operation(summary = "用户AI")
    @PostMapping("/user/chat")
    public Result<String> userChat(String question) {
        Long userId = UserContext.getUserId();
        String memoryId = userId != null ? String.valueOf(userId) : "anonymous";
        String userContext = userId != null
                ? "userId=" + userId + "，已登录"
                : "未登录，userId=0";
        String answer = aiService.userChat(memoryId, userContext, question);
        return Result.success("成功", answer);
    }

    @Operation(summary = "管理AI")
    @PostMapping("/admin/chat")
    public Result<String> adminChat(String question) {
        // 查订单统计
        OrderStats stats = orderMapper.getOverviewStats();
        StringBuilder sb = new StringBuilder();
        if (stats != null) {
            sb.append("总订单：").append(stats.getTotalOrders())
              .append(" | 总营收：").append(stats.getTotalRevenue()).append(" 元")
              .append(" | 已完成：").append(stats.getFinishCount())
              .append(" | 已取消：").append(stats.getCancelCount());
        } else {
            sb.append("暂无订单数据");
        }

        // 查酒店和房间数据
        try {
            Result<List<Hotel>> hotelResult = resourceClient.getHotelList(null, null);
            if (hotelResult != null && hotelResult.getCode() == 200 && hotelResult.getData() != null) {
                List<Hotel> hotels = hotelResult.getData();
                sb.append("\n共 ").append(hotels.size()).append(" 家酒店");

                // 统计每个城市多少家酒店
                Map<String, Integer> cityCount = new HashMap<>();
                for (Hotel h : hotels) {
                    String c = h.getCity();
                    cityCount.put(c, cityCount.getOrDefault(c, 0) + 1);
                }
                sb.append("，分布：");
                for (Map.Entry<String, Integer> e : cityCount.entrySet()) {
                    sb.append(e.getKey()).append(e.getValue()).append("家、");
                }
                sb.deleteCharAt(sb.length() - 1);

                // 统计总房间数
                long totalRooms = 0;
                for (Hotel h : hotels) {
                    Result<List<Room>> roomResult = resourceClient.getRoomList(h.getId(), null);
                    if (roomResult != null && roomResult.getCode() == 200 && roomResult.getData() != null) {
                        totalRooms += roomResult.getData().size();
                    }
                }
                sb.append("，客房共 ").append(totalRooms).append(" 间");
            } else {
                sb.append("\n暂无酒店数据");
            }
        } catch (Exception e) {
            sb.append("\n酒店服务不可用");
        }

        Long adminId = UserContext.getUserId();
        String adminMemoryId = adminId != null ? "admin-" + adminId : "admin-anonymous";
        String answer = aiService.adminChat(adminMemoryId, sb.toString(), question);
        return Result.success("成功", answer);
    }
}
