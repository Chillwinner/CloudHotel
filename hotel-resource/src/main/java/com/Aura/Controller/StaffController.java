package com.Aura.Controller;

import com.Aura.entity.Staff;
import com.Aura.entity.StaffLoginResult;
import com.Aura.Service.StaffService;
import com.Aura.common.Result;
import com.Aura.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "员工管理")
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Operation(summary = "登录")
    @PostMapping("/admin/login")
    public Result<StaffLoginResult> login(String phone, String password) {
        Staff staff = staffService.login(phone, password);
        if (staff == null) {
            return Result.error("账号或密码不对");
        }

        String token = JwtUtil.createAdminToken(staff.getId(), staff.getRole(), staff.getHotelId());
        return Result.success("登录成功", new StaffLoginResult(token, staff.getName(), staff.getRole(), staff.getHotelId()));
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Staff staff) {
        staffService.addStaff(staff);
        return Result.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        staffService.removeStaff(id);
        return Result.success();
    }

    @Operation(summary = "修改")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Staff staff) {
        staffService.modifyStaff(staff);
        return Result.success();
    }

    @Operation(summary = "详情")
    @GetMapping("/{id}")
    public Result<Staff> getOne(@PathVariable Long id) {
        return Result.success(staffService.getStaff(id));
    }

    @Operation(summary = "列表")
    @GetMapping("/list")
    public Result<List<Staff>> list(@RequestParam(required = false) Long hotelId,
                                    @RequestParam(required = false) String name) {
        return Result.success(staffService.listStaff(hotelId, name));
    }
}