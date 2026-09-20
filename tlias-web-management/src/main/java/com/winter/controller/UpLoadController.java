package com.winter.controller;


import com.winter.pojo.Result;
import com.winter.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UpLoadController {
    //本地存储
    /*@PostMapping("/upload")
    public Result upload(String name, String age,  MultipartFile file) throws IOException {
        log.info("文件上传开始 {} {} {}", name, age, file);
        //获取上传的文件名
        String originalFilename = file.getOriginalFilename();
        //生成新的文件名
        String randomName = UUID.randomUUID().toString();
        String newFileName = randomName + originalFilename.substring(originalFilename.lastIndexOf("."));

        file.transferTo(new File("D:/Captures/idea/" + newFileName));
        return Result.success();
    }*/

    //阿里云存储
    @Autowired
    private OssUtil ossUtil;
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        log.info("文件上传开始 {}", file.getOriginalFilename());
        String url = ossUtil.uploadFile(file);
        return Result.success(url);
    }
}
