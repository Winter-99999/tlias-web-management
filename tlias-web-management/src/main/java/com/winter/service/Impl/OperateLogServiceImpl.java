package com.winter.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winter.exception.BusinessException;
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
    /** 每页最多返回多少条，防止 pageSize 传超大值拖垮数据库 */
    private static final int MAX_PAGE_SIZE = 100;

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public PageResult<OperateLog> page(OperateLogQueryParam param) {
        int page = (param.getPage() == null || param.getPage() < 1) ? 1 : param.getPage();
        int pageSize = (param.getPageSize() == null || param.getPageSize() < 1)
                ? 10 : Math.min(param.getPageSize(), MAX_PAGE_SIZE);
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        // 查询数据
        List<OperateLog> rows = operateLogMapper.list(param);
        // 解析结果并封装
        Page<OperateLog> p = (Page<OperateLog>) rows;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public OperateLog getById(Integer id) {
        OperateLog operateLog = operateLogMapper.getById(id);
        if (operateLog == null) {
            throw new BusinessException("日志不存在");
        }
        return operateLog;
    }
}
