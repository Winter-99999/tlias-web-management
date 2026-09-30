package com.winter.controller;

import com.winter.pojo.Clazz;
import com.winter.pojo.ClazzQueryParam;
import com.winter.pojo.PageResult;
import com.winter.pojo.Result;
import com.winter.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/clazzs")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    //条件分页查询
    //例 http://localhost:8080/clazzs?page=1&pageSize=5&name=Java&room=A10&begin=2026-01-01&end=2026-12-31
    @GetMapping
    public Result page(ClazzQueryParam clazzQueryParam){
        log.info("分页查询班级，参数：{}",clazzQueryParam);
        PageResult<Clazz> pageResult = clazzService.page(clazzQueryParam);
        return Result.success(pageResult);
    }

    //查询所有班级（下拉列表用）
    @GetMapping("/list")
    public Result list(){
        log.info("查询所有班级信息");
        List<Clazz> clazzs = clazzService.findAll();
        return Result.success(clazzs);
    }

    //根据id查询班级
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("查询班级信息，id：{}",id);
        Clazz clazz = clazzService.findById(id);
        return Result.success(clazz);
    }

    //新增班级
    @PostMapping
    public Result add(@RequestBody Clazz clazz){
        log.info("新增班级信息：{}",clazz);
        clazzService.add(clazz);
        return Result.success(clazzService.findById(clazz.getId()));
    }

    //修改班级
    @PutMapping
    public Result update(@RequestBody Clazz clazz){
        log.info("修改班级信息：{}",clazz);
        clazzService.update(clazz);
        return Result.success();
    }

    //批量删除班级
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids){
        log.info("删除班级信息，ids：{}",ids);
        clazzService.delete(ids);
        return Result.success();
    }

}
