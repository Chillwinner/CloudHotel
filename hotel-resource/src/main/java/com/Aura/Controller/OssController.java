package com.Aura.Controller;

import com.Aura.common.Result;
import com.Aura.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Tag(name = "管理端上传")
@Slf4j
@RestController
@RequestMapping("/admin/upload")
public class OssController {
    
    @Autowired
    private AliOssUtil aliOssUtil;
    
    @Operation(summary = "上传")
    @PostMapping
    public Result upload(MultipartFile file) throws IOException {
        String objectName = UUID.randomUUID().toString() + file.getOriginalFilename();
        String url = aliOssUtil.upload(file.getBytes(), objectName);
        return Result.success(url);
    }
}

@Tag(name = "用户端上传")
@Slf4j
@RestController
@RequestMapping("/user/upload")
class UserOssController {
    
    @Autowired
    private AliOssUtil aliOssUtil;
    
    @Operation(summary = "上传")
    @PostMapping
    public Result upload(MultipartFile file) throws IOException {
        String objectName = UUID.randomUUID().toString() + file.getOriginalFilename();
        String url = aliOssUtil.upload(file.getBytes(), objectName);
        return Result.success(url);
    }
}
