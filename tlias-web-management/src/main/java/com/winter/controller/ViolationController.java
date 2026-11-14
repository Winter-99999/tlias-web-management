package com.winter.controller;

import com.winter.anno.Log;
import com.winter.anno.RequireRole;
import com.winter.pojo.PageResult;
import com.winter.pojo.Result;
import com.winter.pojo.StudentViolation;
import com.winter.pojo.ViolationQueryParam;
import com.winter.service.StudentViolationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/violations")
@RequireRole({1, 2, 3})   // 违纪查询与登记：三类角色都可以
public class ViolationController {

    @Autowired
    private StudentViolationService violationService;

    // 条件分页查询
    @GetMapping
    public Result page(ViolationQueryParam param) {
        log.info("分页查询违纪，参数：{}", param);
        PageResult<StudentViolation> pageResult = violationService.page(param);
        return Result.success(pageResult);
    }

    // 登记违纪
    @Log(module = "违纪管理", type = "登记")
    @PostMapping
    public Result register(@RequestBody StudentViolation violation) {
        log.info("登记违纪，参数：{}", violation);
        return Result.success(violationService.register(violation));
    }

    // 撤销违纪（只允许超管/教务，覆盖类上的三类角色）
    @Log(module = "违纪管理", type = "撤销")
    @RequireRole({1, 2})
    @DeleteMapping("/{id}")
    public Result revoke(@PathVariable Integer id) {
        log.info("撤销违纪，id：{}", id);
        violationService.revoke(id);
        return Result.success();
    }
}