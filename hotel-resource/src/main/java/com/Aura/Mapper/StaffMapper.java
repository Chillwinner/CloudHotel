package com.Aura.Mapper;

import com.Aura.entity.Staff;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface StaffMapper {
    int insert(Staff staff);
    int delete(Long id);
    int update(Staff staff);
    Staff selectById(Long id);
    List<Staff> selectList(Long hotelId, String name);

    Staff selectByPhone(String phone);
}