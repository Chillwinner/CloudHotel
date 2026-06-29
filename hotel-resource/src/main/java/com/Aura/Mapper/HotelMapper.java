package com.Aura.Mapper;

import com.Aura.entity.Hotel;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface HotelMapper {
    // 基础 CRUD
    int insert(Hotel hotel);
    int delete(Long id);
    int update(Hotel hotel);
    Hotel selectById(Long id);

    List<Hotel> selectList(String name, String city);
}