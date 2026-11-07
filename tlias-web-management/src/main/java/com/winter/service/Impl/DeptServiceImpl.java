package com.winter.service.Impl;

import com.winter.exception.BusinessException;
import com.winter.mapper.DeptMapper;
import com.winter.pojo.Dept;
import com.winter.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;
    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }
    @Override
    public void deleteById(Integer id) {
        if(deptMapper.findById(id) == null){
            throw new BusinessException("部门不存在");
        }
        if (deptMapper.getEmpCountDept(id) == 0) {
            deptMapper.deleteById(id);
        } else {
            throw new BusinessException("该部门下有员工，请先删除员工");
        }
    }
    @Override
    public void add(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.add(dept);
    }

    @Override
    public Dept findById(Integer id) {
        return deptMapper.findById(id);
    }

    @Override
    public void update(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }

    //统计部门员工人数
    @Override
    public Integer getDeptData(Integer id) {
        return deptMapper.getEmpCountDept(id);
    }
}
