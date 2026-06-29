package com.Aura.Mapper;

import com.Aura.entity.HotelOrder;
import com.Aura.entity.OrderStats;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface OrderMapper {

    int insertOrder(HotelOrder order);

    List<HotelOrder> selectByUserId(Long userId);

    int updateStatus(Long id, Integer status);

    HotelOrder selectById(Long id);

    OrderStats getOverviewStats();

    String selectUserEmailById(Long userId);

    List<HotelOrder> selectAllOrders(String orderNo, Integer status);
}