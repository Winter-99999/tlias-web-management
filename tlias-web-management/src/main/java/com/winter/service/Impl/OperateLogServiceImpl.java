package com.winter.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winter.mapper.OperateLogMapper;
import com.winter.pojo.OperateLog;
import com.winter.pojo.OperateLogQueryParam;
import com.winter.pojo.PageResult;
import com.winter.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OperateLogServiceImpl implements OperateLogService {
    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public PageResult<OperateLog> page(OperateLogQueryParam param) {
        // 设置分页参数
        PageHelper.startPage(param.getPage(), param.getPageSize());
        // 查询数据
        List<OperateLog> rows = operateLogMapper.list(param);
        // 解析结果并封装
        Page<OperateLog> p = (Page<OperateLog>) rows;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public OperateLog getById(Integer id) {
        return operateLogMapper.getById(id);
    }
}
