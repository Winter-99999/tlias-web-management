package com.winter.controller;


import com.winter.pojo.Emp;
import com.winter.pojo.EmpQueryParam;
import com.winter.pojo.PageResult;
import com.winter.pojo.Result;
import com.winter.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
    @Autowired
    private EmpService empService;

    //条件分页查询   例 http://localhost:8080/emps?page=1&pageSize=5&name=王&gender=1&begin=2020-03-10&end=2026-04-10
    @GetMapping()
    public Result page(EmpQueryParam empQueryParam){
         PageResult<Emp> pageResult = empService.page(empQueryParam);
         return Result.success(pageResult);
    }

    //新增员工
    @PostMapping()
    public Result save(@RequestBody Emp emp){
        log.info("保存员工，员工信息：{}",emp);
        //保存员工
        empService.save(emp);
        return Result.success();
    }

    //删除员工
    @DeleteMapping()
    public Result delete(@RequestParam List<Integer> ids){
        log.info("删除员工，员工id：{}",ids);
        empService.delete(ids);
        return Result.success();
    }
}
