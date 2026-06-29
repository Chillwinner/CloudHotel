package com.aura.hotel.Controller;

import com.aura.hotel.Service.UserService;
import com.aura.hotel.common.Result;
import com.aura.hotel.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户模块")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "通过ID查询（对接getUserInfo）")
    @GetMapping("/{id}")
    public Result<User> getInfo(@PathVariable Long id) {
        return Result.success(userService.getUserInfo(id));
    }

    @Operation(summary = "修改资料（对接updateUser）")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody User user) {
        userService.updateUserInfo(user);
        return Result.success();
    }

    @Operation(summary = "手机号注册")
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        String msg = userService.register(user);
        return msg.equals("注册成功") ? Result.success(msg) : Result.error(msg);
    }
}