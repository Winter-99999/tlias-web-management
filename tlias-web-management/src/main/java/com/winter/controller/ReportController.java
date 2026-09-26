package com.winter.controller;

import com.winter.mapper.EmpMapper;
import com.winter.pojo.JobOption;
import com.winter.pojo.Result;
import com.winter.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    //统计各职位员工人数
    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        log.info("统计各职位员工人数");
        JobOption jobData = reportService.getJobData();
        return Result.success(jobData);
    }
    //统计员工性别人数
    @GetMapping("/empGenderData")
    public Result getEmpGenderData(){
        log.info("统计员工性别人数");
        List<Map<String,Object>> genderData = reportService.getGenderData();
        return Result.success(genderData);
    }
}
