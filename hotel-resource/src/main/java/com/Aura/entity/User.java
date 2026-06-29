package com.Aura.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "用户信息实体")
public class User {
    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "手机号（登录凭证）")
    private String phone;

    @Schema(description = "登录密码")
    private String password;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "注册时间")
    private Date createTime;
}