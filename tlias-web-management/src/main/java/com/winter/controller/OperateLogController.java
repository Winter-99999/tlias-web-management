package com.winter.controller;

import com.winter.anno.RequireRole;
import com.winter.pojo.OperateLog;
import com.winter.pojo.OperateLogQueryParam;
import com.winter.pojo.PageResult;
import com.winter.pojo.Result;
import com.winter.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequireRole({1})
@RequestMapping("/logs")
public class OperateLogController {

    @Autowired
    private OperateLogService operateLogService;

    // 条件分页查询操作日志
    @GetMapping
    public Result page(OperateLogQueryParam param) {
        log.info("分页查询操作日志，参数：{}", param);
        PageResult<OperateLog> pageResult = operateLogService.page(param);
        return Result.success(pageResult);
    }

    // 根据 id 查询日志详情
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("查询操作日志详情，id：{}", id);
        return Result.success(operateLogService.getById(id));
    }
}