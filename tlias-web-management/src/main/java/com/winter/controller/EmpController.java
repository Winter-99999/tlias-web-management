package com.winter.controller;


import com.winter.anno.Log;
import com.winter.anno.RequireRole;
import com.winter.exception.BusinessException;
import com.winter.pojo.Emp;
import com.winter.pojo.EmpQueryParam;
import com.winter.pojo.PageResult;
import com.winter.pojo.Result;
import com.winter.service.EmpService;
import com.winter.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/emps")
@RequireRole({1, 2})
public class EmpController {
    @Autowired
    private EmpService empService;

    //条件分页查询   例 http://localhost:8080/emps?page=1&pageSize=5&name=王&gender=1&begin=2020-03-10&end=2026-04-10
    @GetMapping()
    public Result page(EmpQueryParam empQueryParam){
        log.info("分页查询，参数：{}",empQueryParam);
         PageResult<Emp> pageResult = empService.page(empQueryParam);
         return Result.success(pageResult);
    }

    //新增员工
    @Log(module = "员工管理", type = "新增")
    @PostMapping()
    public Result save(@RequestBody Emp emp){
        if (emp.getRole() != null && !isSuperAdmin()) {
            throw new BusinessException("只有超级管理员可以指定角色");
        }
        log.info("保存员工，员工信息：{}",emp);
        //保存员工
        empService.save(emp);
        return Result.success(empService.getById(emp.getId()));
    }

    //删除员工
    @Log(module = "员工管理", type = "删除")
    @DeleteMapping()
    public Result delete(@RequestParam List<Integer> ids){
        log.info("删除员工，员工id：{}",ids);
        empService.delete(ids);
        return Result.success();
    }

    //根据id查询员工信息
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id){
        log.info("查询员工，员工id：{}",id);
        Emp emp = empService.getById(id);
        return Result.success(emp);
    }

    //根据id修改员工信息
    @Log(module = "员工管理", type = "修改")
    @PutMapping()
    public Result update(@RequestBody Emp emp){
        Emp old = empService.getById(emp.getId());
        if (old == null) {
            throw new BusinessException("员工不存在");
        }
        if (emp.getRole() != null && !emp.getRole().equals(old.getRole()) && !isSuperAdmin()) {
            throw new BusinessException("只有超级管理员可以修改角色");
        }
        log.info("修改员工，员工信息：{}",emp);
        empService.update(emp);
        return Result.success();
    }
    private boolean isSuperAdmin() {
        return Integer.valueOf(1).equals(UserContext.getRole());
    }
}
