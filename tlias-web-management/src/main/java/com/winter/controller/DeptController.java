package com.winter.controller;

import com.winter.pojo.Dept;
import com.winter.pojo.Result;
import com.winter.service.DeptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/depts")  //抽取公共路径
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;

    //@RequestMapping(value = "/depts",method = RequestMethod.GET)
    //查找所有部门信息
    @GetMapping
    public Result list() {
        List<Dept> lists = deptService.findAll();
        return Result.success(lists);
    }

    //根据id删除指定部门
    //方式一
    /*@DeleteMapping("/depts")
    public Result delete(HttpServletRequest request){
        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);
        System.out.println("根据id删除部门" + id);
        return Result.success();
    }*/
    //方式二 required默认是true,不传递参数会报错
    /*@DeleteMapping("/depts")
    public Result delete(@RequestParam(value = "id",required = false) Integer deid){
        System.out.println("根据id删除部门" + deid);
        return Result.success();
    }*/

    //方式三  前端传递的参数名与服务器端参数名一致 可省略注解 【推荐】
    @DeleteMapping
    public Result delete(Integer id){
        //System.out.println("根据id删除部门" + id);
        log.info("根据id删除部门 : {}", id);
        deptService.deleteById(id);
        return Result.success();
    }

    //新增部门
    //使用实体对象来接收json数据，保证属性名与json的键名一致，并加上@RequestBody注解
    @PostMapping
    public Result add(@RequestBody Dept dept){
        //System.out.println("新增部门 " + dept);
        log.info("新增部门 : {}" ,dept);
        deptService.add(dept);
        return Result.success();
    }

    //根据id查询部门
    /*@GetMapping("/depts/{id}")
    public Result findById(@PathVariable("id") Integer deptId){
        System.out.println("根据id查询部门" + deptId);
        Dept dept = deptService.findById(deptId);
        return Result.success(dept);
    }*/
    //   @PathVariable接收路径参数
    //如果形参名与路径参数名称一致可省略("id")
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){
//        System.out.println("根据id查询部门 " + id);
        log.info("根据id查询部门 :{}" ,id);
        Dept dept = deptService.findById(id);
        return Result.success(dept);
    }

    //修改部门信息
    @PutMapping
    public Result update(@RequestBody Dept dept){
        //System.out.println("修改部门 ");
        log.info("修改部门 ");
        deptService.update(dept);
        return Result.success();
    }
}
