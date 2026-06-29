package com.aura.hotel.Controller;

import com.aura.hotel.common.Result;
import com.aura.hotel.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin/upload")
public class OssController {
    @Autowired
    private AliOssUtil ao;
    @PostMapping
    //用getname获取的是前端给的名字 用orinalname才是文件本名
    public Result upload_oss(MultipartFile file) throws IOException {
        String objectName = UUID.randomUUID().toString()+file.getOriginalFilename();
        String url=ao.upload(file.getBytes(),objectName);
        return Result.success(url);
    }
}

@Slf4j
@RestController
@RequestMapping("/user/upload")
class UserOssController {
    @Autowired
    private AliOssUtil ao;
    @PostMapping
    //用getname获取的是前端给的名字 用orinalname才是文件本名
    public Result upload_oss(MultipartFile file) throws IOException {
        String objectName = UUID.randomUUID().toString()+file.getOriginalFilename();
        String url=ao.upload(file.getBytes(),objectName);
        return Result.success(url);
    }
}
