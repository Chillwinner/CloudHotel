package com.aura.hotel.Service;

import com.aura.hotel.entity.HotelOrder;
import com.aura.hotel.entity.Room;
import com.aura.hotel.Mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public String submitOrder(Long hotelId, String typeName, String startDate, String endDate) {
        // 1. 寻找空房
        Room availableRoom = orderMapper.findOneAvailableRoom(hotelId, typeName, startDate, endDate);
        if (availableRoom == null) {
            return "该时段此房型已售罄，请尝试其他房型或日期";
        }

        // 2. 计算入住时长与总价
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        long days = ChronoUnit.DAYS.between(start, end);
        if (days <= 0) days = 1; // 至少按1天计费

        BigDecimal totalAmount = availableRoom.getPrice().multiply(new BigDecimal(days));

        // 3. 封装订单并落地
        HotelOrder order = new HotelOrder();
        order.setOrderNo("HOTEL" + System.currentTimeMillis());
        order.setHotelId(hotelId);
        order.setRoomId(availableRoom.getId());
        order.setStartDate(start);
        order.setEndDate(end);
        order.setTotalAmount(totalAmount);
        order.setStatus(1); // 默认待入住

        orderMapper.insertOrder(order);

        return "下单成功！为您分配房号：" + availableRoom.getRoomNumber() + "，共计：" + totalAmount + "元";
    }

    public List<HotelOrder> getUserOrders(Long userId) {
        return orderMapper.selectByUserId(userId);
    }

    // 在 OrderService 中添加
    public void changeOrderStatus(Long id, Integer status) {
        orderMapper.updateStatus(id, status);
    }
}