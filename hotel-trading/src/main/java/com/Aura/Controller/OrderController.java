package com.Aura.Controller;

import com.Aura.utils.UserContext;
import com.Aura.entity.HotelOrder;
import com.Aura.Service.OrderService;
import com.Aura.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "订单管理")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "提交订单")
    @PostMapping("/submit")
    public Result<String> submit(Long hotelId, String typeName, String startDate, String endDate) {

        Long currentUserId = UserContext.getUserId();
        if (currentUserId == null) {
            return Result.error(401, "请先登录");
        }

        String msg = orderService.submitOrder(currentUserId, hotelId, typeName, startDate, endDate);
        if (msg.contains("售罄")) return Result.error(msg);
        return Result.success(msg);
    }

    @Operation(summary = "我的订单")
    @GetMapping("/my")
    public Result<List<HotelOrder>> getMyOrders() {
        Long currentUserId = UserContext.getUserId();
        if (currentUserId == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(orderService.getUserOrders(currentUserId));
    }

    @Operation(summary = "订单列表(管理端)")
    @GetMapping("/admin/list")
    public Result<List<HotelOrder>> getAllOrders(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status) {

        List<HotelOrder> list = orderService.getAllOrders(orderNo, status);
        return Result.success(list);
    }
}
