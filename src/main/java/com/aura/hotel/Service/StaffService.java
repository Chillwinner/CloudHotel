package com.aura.hotel.Service;

import com.aura.hotel.entity.Staff;
import com.aura.hotel.Mapper.StaffMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class StaffService {

    @Autowired
    private StaffMapper staffMapper;

    public void addStaff(Staff staff) {
        staffMapper.insert(staff);
    }

    public void removeStaff(Long id) {
        staffMapper.delete(id);
    }

    public void modifyStaff(Staff staff) {
        staffMapper.update(staff);
    }

    public Staff getStaff(Long id) {
        return staffMapper.selectById(id);
    }

    public List<Staff> listStaff(Long hotelId, String name) {
        return staffMapper.selectList(hotelId, name);
    }
}