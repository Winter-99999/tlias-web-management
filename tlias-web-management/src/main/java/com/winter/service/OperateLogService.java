package com.winter.service;

import com.winter.pojo.OperateLog;
import com.winter.pojo.OperateLogQueryParam;
import com.winter.pojo.PageResult;

public interface OperateLogService {
    // 条件分页查询
    PageResult<OperateLog> page(OperateLogQueryParam param);

    // 根据 id 查询详情
    OperateLog getById(Integer id);
}
