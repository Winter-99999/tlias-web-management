package com.winter.service.Impl;

import com.winter.mapper.EmpMapper;
import com.winter.pojo.JobOption;
import com.winter.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private EmpMapper empMapper;

    //统计员工职位
    @Override
    public JobOption getJobData() {
        List<Map<String,Object>> list = empMapper.getEmpCountJob();
        List<Object> jobList = list.stream().map(m -> m.get("pos")).toList();
        List<Object> dataList = list.stream().map(m -> m.get("num")).toList();
        return new JobOption(jobList,dataList);
    }
    //统计员工性别
    @Override
    public List<Map<String, Object>> getGenderData() {
        return empMapper.getEmpCountGender();
    }

}
