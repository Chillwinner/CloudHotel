package com.aura.hotel.Mapper;

import com.aura.hotel.entity.HotelOrder;
import com.aura.hotel.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrderMapper {
    // 核心：寻找该时段、该酒店、该房型下第一个空闲的房间
    Room findOneAvailableRoom(Long hotelId, String typeName, String startDate, String endDate);
    int insertOrder(HotelOrder order);
    List<HotelOrder> selectByUserId(Long userId);
    int updateStatus(Long id, Integer status);
}