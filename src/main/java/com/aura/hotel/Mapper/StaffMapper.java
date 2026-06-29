package com.aura.hotel.Mapper;

import com.aura.hotel.entity.Staff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StaffMapper {
    int insert(Staff staff);
    int delete(Long id);
    int update(Staff staff);
    Staff selectById(Long id);
    // 支持按酒店筛选和按姓名模糊搜索
    List<Staff> selectList(Long hotelId, String name);
}