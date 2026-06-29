package com.Aura.utils;

import com.Aura.Mapper.OrderMapper;
import com.Aura.Service.OrderService;
import com.Aura.utils.UserContext;
import com.Aura.common.Result;
import com.Aura.entity.Hotel;
import com.Aura.entity.HotelOrder;
import com.Aura.entity.Room;
import com.Aura.feign.ResourceClient;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("userAiTools")
public class UserAiTools {

    @Autowired
    private ResourceClient resourceClient;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Tool("按城市搜酒店")
    public String searchHotels(@P("城市名") String city) {
        Result<List<Hotel>> result = resourceClient.getHotelList(null, city);
        if (result == null || result.getCode() != 200 || result.getData() == null || result.getData().isEmpty()) {
            return "没有找到 " + city + " 的酒店";
        }
        StringBuilder sb = new StringBuilder();
        for (Hotel h : result.getData()) {
            sb.append("【").append(h.getName()).append("】星级：").append(h.getStarLevel())
              .append(" | ").append(h.getDescription()).append("\n");
        }
        return sb.toString();
    }

    @Tool("查看酒店详情")
    public String getHotelDetail(@P("酒店ID") Long hotelId) {
        Result<Hotel> result = resourceClient.getHotelById(hotelId);
        if (result == null || result.getCode() != 200 || result.getData() == null) {
            return "没有找到该酒店";
        }
        Hotel h = result.getData();
        return "酒店名称：" + h.getName() + "\n城市：" + h.getCity()
                + "\n地址：" + h.getAddress() + "\n电话：" + h.getPhone()
                + "\n星级：" + h.getStarLevel() + "星\n简介：" + h.getDescription();
    }

    @Tool("查看酒店房型")
    public String getHotelRoomTypes(@P("酒店ID") Long hotelId) {
        Result<List<String>> result = resourceClient.getRoomTypes(hotelId);
        if (result == null || result.getCode() != 200 || result.getData() == null || result.getData().isEmpty()) {
            return "没有找到该酒店的房型信息";
        }
        return "房型列表：" + String.join("、", result.getData());
    }

    @Tool("查空房")
    public String checkAvailability(@P("酒店ID") Long hotelId,
                                    @P("房型名") String typeName,
                                    @P("入住日期 yyyy-MM-dd") String startDate,
                                    @P("离店日期 yyyy-MM-dd") String endDate) {
        Result<Room> result = resourceClient.findOneAvailableRoom(hotelId, typeName, startDate, endDate);
        if (result == null || result.getCode() != 200 || result.getData() == null) {
            return "该时段此房型已售罄，请尝试其他房型或日期";
        }
        Room room = result.getData();
        return "有空房！房号：" + room.getRoomNumber() + "，价格：" + room.getPrice() + " 元/晚";
    }

    private Long getUserId(@ToolMemoryId String memoryId) {
        if (memoryId != null && memoryId.matches("\\d+")) return Long.parseLong(memoryId);
        Long uid = UserContext.getUserId();
        if (uid != null) return uid;
        return null;
    }

    @Tool("下单预订")
    public String submitOrder(@ToolMemoryId String memoryId,
                              @P("酒店ID") Long hotelId,
                              @P("房型名") String typeName,
                              @P("入住日期 yyyy-MM-dd") String startDate,
                              @P("离店日期 yyyy-MM-dd") String endDate) {
        Long uid = getUserId(memoryId);
        if (uid == null) return "请先登录";
        return orderService.submitOrder(uid, hotelId, typeName, startDate, endDate);
    }

    @Tool("查看我的订单")
    public String getMyOrders(@ToolMemoryId String memoryId) {
        Long uid = getUserId(memoryId);
        if (uid == null) return "请先登录";
        List<HotelOrder> orders = orderMapper.selectByUserId(uid);
        if (orders == null || orders.isEmpty()) return "暂无订单";
        StringBuilder sb = new StringBuilder();
        for (HotelOrder o : orders) {
            String statusText;
            if (o.getStatus() == 1) statusText = "待入住";
            else if (o.getStatus() == 2) statusText = "已入住";
            else if (o.getStatus() == 3) statusText = "已完成";
            else statusText = "未知";
            sb.append("订单号：").append(o.getOrderNo()).append(" | 金额：").append(o.getTotalAmount())
              .append(" 元 | 日期：").append(o.getStartDate()).append(" ~ ").append(o.getEndDate())
              .append(" | 状态：").append(statusText).append("\n");
        }
        return sb.toString();
    }

    @Tool("查看订单详情")
    public String getOrderDetail(@ToolMemoryId String memoryId, @P("订单ID") Long orderId) {
        Long uid = getUserId(memoryId);
        if (uid == null) return "请先登录";
        HotelOrder o = orderMapper.selectById(orderId);
        if (o == null || !o.getUserId().equals(uid)) return "订单不存在或无权限查看";
        String statusText;
        if (o.getStatus() == 1) statusText = "待入住";
        else if (o.getStatus() == 2) statusText = "已入住";
        else if (o.getStatus() == 3) statusText = "已完成";
        else statusText = "未知";
        return "订单号：" + o.getOrderNo() + "\n金额：" + o.getTotalAmount()
                + " 元\n入住日期：" + o.getStartDate() + "\n离店日期：" + o.getEndDate()
                + "\n状态：" + statusText + "\n创建时间：" + o.getCreateTime();
    }
}
