package com.Aura.Service;

import com.Aura.common.Result;
import com.Aura.entity.HotelOrder;
import com.Aura.entity.Room;
import com.Aura.Mapper.OrderMapper;
import com.Aura.feign.ResourceClient;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ResourceClient resourceClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GlobalTransactional(name = "submit-order-tx", rollbackFor = Exception.class)
    public String submitOrder(Long userId, Long hotelId, String typeName, String startDate, String endDate) {
        Result<Room> result = resourceClient.findOneAvailableRoom(hotelId, typeName, startDate, endDate);
        if (result == null || result.getCode() != 200 || result.getData() == null) {
            return "没房了，换个日期或房型试试";
        }
        Room room = result.getData();

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        long days = ChronoUnit.DAYS.between(start, end);
        if (days <= 0) days = 1;
        BigDecimal totalAmount = room.getPrice().multiply(new BigDecimal(days));

        HotelOrder order = new HotelOrder();
        order.setOrderNo("HOTEL" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setHotelId(hotelId);
        order.setRoomId(room.getId());
        order.setStartDate(start);
        order.setEndDate(end);
        order.setTotalAmount(totalAmount);
        order.setStatus(1);
        orderMapper.insertOrder(order);

        resourceClient.lockRoomStatus(room.getId());

        String userEmail = orderMapper.selectUserEmailById(userId);
        rabbitTemplate.convertAndSend("order.sms.queue",
                userEmail + "|订单号:" + order.getOrderNo() + ", 房号:" + room.getRoomNumber() + ", 共" + totalAmount + "元");

        return "订好了，房号：" + room.getRoomNumber() + "，共" + totalAmount + "元";
    }

    public List<HotelOrder> getUserOrders(Long userId) {
        return orderMapper.selectByUserId(userId);
    }

    public List<HotelOrder> getAllOrders(String orderNo, Integer status) {
        return orderMapper.selectAllOrders(orderNo, status);
    }
}
