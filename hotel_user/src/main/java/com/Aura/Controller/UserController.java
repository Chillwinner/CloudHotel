package com.Aura.Controller;

import com.Aura.Service.UserService;
import com.Aura.utils.UserContext;
import com.Aura.common.Result;
import com.Aura.entity.LoginResult;
import com.Aura.entity.User;
import com.Aura.utils.AliOssUtil;
import com.Aura.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "用户模块")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AliOssUtil aliOssUtil;

    @Operation(summary = "注册")
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        String msg = userService.register(user);
        return msg.equals("注册成功") ? Result.success(msg) : Result.error(msg);
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<LoginResult> login(String phone, String password) {
        try {
            User user = userService.login(phone, password);

            if (user == null) {
                return Result.error(400, "账号或密码不对");
            }

            String token = JwtUtil.createToken(user.getId());
            return Result.success("登录成功", new LoginResult(token, user.getId(), user.getNickname()));

        } catch (RuntimeException e) {
            return Result.error(403, e.getMessage());
        }
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{id}")
    public Result<User> getInfoById(@PathVariable Long id) {
        return Result.success(userService.getUserInfo(id));
    }

    @Operation(summary = "我的信息")
    @GetMapping("/info")
    public Result<User> getInfo() {
        Long currentUserId = UserContext.getUserId();
        if (currentUserId == null) {
            return Result.error(401, "未登录");
        }
        return Result.success(userService.getUserInfo(currentUserId));
    }

    @Operation(summary = "修改资料")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody User user) {
        Long currentUserId = UserContext.getUserId();
        if (currentUserId == null) {
            return Result.error(401, "未登录");
        }
        user.setId(currentUserId);
        userService.updateUserInfo(user);
        return Result.success();
    }

    @Operation(summary = "上传头像")
    @PostMapping("/avatar/upload")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Long currentUserId = UserContext.getUserId();
        if (currentUserId == null) {
            return Result.error(401, "未登录");
        }
        if (file.isEmpty()) {
            return Result.error("选张图片吧");
        }
        try {
            String objectName = UUID.randomUUID().toString() + file.getOriginalFilename();
            String avatarUrl = aliOssUtil.upload(file.getBytes(), objectName);
            User user = new User();
            user.setId(currentUserId);
            user.setAvatar(avatarUrl);
            userService.updateUserInfo(user);
            return Result.success("上传成功", avatarUrl);
        } catch (IOException e) {
            return Result.error("上传失败：" + e.getMessage());
        }
    }

    @Operation(summary = "用户列表(管理端)")
    @GetMapping("/admin/list")
    public Result<List<User>> getUserList(@RequestParam(required = false) String keyword) {
        return Result.success(userService.getUserList(keyword));
    }

    @Operation(summary = "封禁/解封(管理端)")
    @PutMapping("/admin/status")
    public Result<String> changeStatus(
            Long userId,
            Integer status) {

        if (status != 0 && status != 1) {
            return Result.error("参数不对");
        }

        try {
            userService.changeUserStatus(userId, status);
            String msg = (status == 1) ? "已解封" : "已封禁";
            return Result.success(msg);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
