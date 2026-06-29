package com.aura.hotel.Controller;

import com.aura.hotel.entity.Staff;
import com.aura.hotel.Service.StaffService;
import com.aura.hotel.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "员工管理", description = "提供员工的增删改查相关接口")
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Operation(summary = "新增员工", description = "传入员工姓名、手机号、性别(1男2女)、角色等级(1店长2经理3员工)等信息")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Staff staff) {
        staffService.addStaff(staff);
        return Result.success();
    }

    @Operation(summary = "删除员工", description = "根据主键ID物理删除员工记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "员工ID") @PathVariable Long id) {
        staffService.removeStaff(id);
        return Result.success();
    }

    @Operation(summary = "更新员工信息", description = "根据ID修改员工信息，只更新传值的字段")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Staff staff) {
        staffService.modifyStaff(staff);
        return Result.success();
    }

    @Operation(summary = "获取单个员工详情")
    @GetMapping("/{id}")
    public Result<Staff> getOne(@Parameter(description = "员工ID") @PathVariable Long id) {
        return Result.success(staffService.getStaff(id));
    }

    @Operation(summary = "查询员工列表", description = "支持根据酒店ID筛选和员工姓名模糊搜索")
    @GetMapping("/list")
    public Result<List<Staff>> list(
            @Parameter(description = "酒店ID (可选)") @RequestParam(required = false) Long hotelId,
            @Parameter(description = "员工姓名 (模糊搜索, 可选)") @RequestParam(required = false) String name) {
        return Result.success(staffService.listStaff(hotelId, name));
    }
}