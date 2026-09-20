package com.winter.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winter.mapper.EmpExprMapper;
import com.winter.mapper.EmpMapper;
import com.winter.pojo.Emp;
import com.winter.pojo.EmpExpr;
import com.winter.pojo.EmpQueryParam;
import com.winter.pojo.PageResult;
import com.winter.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;

    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        /*
        //获取总记录数
        Long total = empMapper.count();
        //计算起始索引
        Integer start = (page-1)*pageSize;
        //获取当前页数据
        List<Emp> rows = empMapper.list(start,pageSize);
        //封装PageResult并返回
        return new PageResult<Emp>(total, rows);
        */

        //设置分页参数
        PageHelper.startPage(empQueryParam.getPage(),empQueryParam.getPageSize());
        //获取当前页数据
        List<Emp> rows = empMapper.list(empQueryParam);
        //解析查询结构，封装PageResult并返回
        Page<Emp> p = (Page<Emp>) rows;
        return new PageResult<Emp>(p.getTotal(), p.getResult());

    }

    @Transactional //事务管理
    @Override
    public void save(Emp emp) {
        //保存员工基本信息
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);

        //保存员工经历
        List<EmpExpr> exprList = emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList)){
            exprList.forEach(empExpr -> {
                empExpr.setEmpId(emp.getId());
            });
            empExprMapper.insertBatch(exprList);
        }
    }

    @Override
    public void delete(List<Integer> ids) {
        //删除员工基本信息
        empMapper.deleteByIds(ids);
        //删除员工经历
        empExprMapper.deleteByEmpIds(ids);
    }
}
