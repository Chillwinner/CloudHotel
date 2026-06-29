package com.aura.hotel.Mapper;

import com.aura.hotel.entity.Hotel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HotelMapper {
    // 基础 CRUD
    int insert(Hotel hotel);
    int delete(Long id);
    int update(Hotel hotel);
    Hotel selectById(Long id);
    
    // 多条件查询（模糊搜名称，精确搜城市）
    List<Hotel> selectList(String name, String city);
}