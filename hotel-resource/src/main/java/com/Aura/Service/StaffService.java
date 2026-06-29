package com.Aura.Service;

import com.Aura.entity.Staff;
import com.Aura.Mapper.StaffMapper;
import com.Aura.utils.MD5Util;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class StaffService {

    @Autowired
    private StaffMapper staffMapper;

    // 新增员工
    public String addStaff(Staff staff) {
        Staff exist = staffMapper.selectByPhone(staff.getPhone());
        if (exist != null) return "手机号已注册";
        staff.setPassword(MD5Util.encrypt(staff.getPassword()));
        staffMapper.insert(staff);
        return "添加成功";
    }

    // 员工登录
    public Staff login(String phone, String password) {
        Staff staff = staffMapper.selectByPhone(phone);
        if (staff == null) return null;
        if (!staff.getPassword().equals(MD5Util.encrypt(password))) return null;
        return staff;
    }

    // 删员工
    public void removeStaff(Long id) {
        staffMapper.delete(id);
    }

    // 改员工
    public void modifyStaff(Staff staff) {
        staffMapper.update(staff);
    }

    // 查员工
    public Staff getStaff(Long id) {
        return staffMapper.selectById(id);
    }

    // 员工列表(按酒店/姓名)
    public List<Staff> listStaff(Long hotelId, String name) {
        return staffMapper.selectList(hotelId, name);
    }
}
