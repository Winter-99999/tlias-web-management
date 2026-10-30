package com.winter.controller;

import com.winter.pojo.*;
import com.winter.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    //条件分页查询
    //SpringMVC 自动把 query 参数（? 后面的）绑定到 POJO 对象属性上
    @GetMapping
    public Result list(StudentQueryParam studentQueryParam){
        log.info("分页查询学生，参数：{}",studentQueryParam);
        PageResult<Student> pageResult = studentService.page(studentQueryParam);
        return Result.success(pageResult);
    }
    //根据id查询学生
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("查询学生，id：{}",id);
        Student student = studentService.getById(id);
        return Result.success(student);
    }
    //新增学生
    @PostMapping
    public Result add(@RequestBody Student student){
        log.info("新增学生，参数：{}",student);
        studentService.add(student);
        return Result.success(studentService.getById(student.getId()));
    }
    //修改学生信息
    @PutMapping
    public Result update(@RequestBody Student student){
        log.info("修改学生，参数：{}",student);
        studentService.update(student);
        return Result.success();
    }
    //接收同名的多个 query 参数，封装成 List，集合/数组接收 query 参数，必须加 @RequestParam，否则绑定失败
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids){
        log.info("删除学生，ids：{}",ids);
        studentService.delete(ids);
        return Result.success();
    }

}
