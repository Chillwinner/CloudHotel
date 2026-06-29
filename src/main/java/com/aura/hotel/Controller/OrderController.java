package com.aura.hotel.Controller;

import com.aura.hotel.entity.HotelOrder;
import com.aura.hotel.Service.OrderService;
import com.aura.hotel.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "订单管理接口", description = "处理下单、订单查询及状态流转")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "提交订单（预约）", description = "根据房型和日期自动寻找空房并计算价格，完成后直接锁房")
    @PostMapping("/submit")
    public Result<String> submit(
            @RequestParam Long hotelId,
            @RequestParam String typeName,
            @Parameter(description = "格式：yyyy-MM-dd") @RequestParam String startDate,
            @Parameter(description = "格式：yyyy-MM-dd") @RequestParam String endDate) {
        String msg = orderService.submitOrder(hotelId, typeName, startDate, endDate);
        if (msg.contains("售罄")) return Result.error(msg);
        return Result.success(msg);
    }

    @Operation(summary = "获取当前用户订单列表", description = "默认查询用户ID为1的订单")
    @GetMapping("/my")
    public Result<List<HotelOrder>> getMyOrders() {
        return Result.success(orderService.getUserOrders(1L));
    }

    @Operation(summary = "变更订单状态", description = "用于模拟取消订单(0)或办理退房(3)")
    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "目标状态: 0-取消, 2-入住, 3-完成") @RequestParam Integer status) {
        orderService.changeOrderStatus(id, status);
        return Result.success();
    }
}