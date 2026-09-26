package com.winter.service;

import com.winter.pojo.JobOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ReportService {
    //统计员工职位
    public JobOption getJobData();
    //统计员工性别
    public List<Map<String,Object>> getGenderData();
}
