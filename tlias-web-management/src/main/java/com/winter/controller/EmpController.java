package com.winter.controller;


import com.winter.anno.Log;
import com.winter.anno.RequireRole;
import com.winter.exception.BusinessException;
import com.winter.pojo.Emp;
import com.winter.pojo.EmpExportParam;
import com.winter.pojo.EmpQueryParam;
import com.winter.pojo.PageResult;
import com.winter.pojo.Result;
import com.winter.pojo.dto.EmployeeExportVO;
import com.winter.pojo.dto.EmployeeImportDTO;
import com.winter.service.EmpExcelService;
import com.winter.service.EmpService;
import com.winter.utils.ExcelUtils;
import com.winter.utils.UserContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/emps")
@RequireRole({1, 2})
public class EmpController {
    @Autowired
    private EmpService empService;
    @Autowired
    private EmpExcelService empExcelService;

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

    //下载员工导入模板
    @GetMapping("/template")
    public void template(HttpServletResponse response) throws IOException {
        log.info("下载员工导入模板");
        ExcelUtils.writeTemplate(response, EmployeeImportDTO.class, "员工导入模板", "员工导入模板");
    }

    //批量导入员工
    @Log(module = "员工管理", type = "导入")
    @PostMapping("/import")
    public Result importEmployees(@RequestParam("file") MultipartFile file) {
        log.info("批量导入员工，文件名：{}", file.getOriginalFilename());
        return Result.success(empExcelService.importEmployees(file));
    }

    //按条件导出员工
    @Log(module = "员工管理", type = "导出")
    @GetMapping("/export")
    public void export(EmpExportParam param, HttpServletResponse response) throws IOException {
        List<EmployeeExportVO> rows = empExcelService.exportEmployees(param);
        log.info("导出员工 {} 条，条件：{}", rows.size(), param);
        ExcelUtils.write(response, EmployeeExportVO.class, "员工名单", ExcelUtils.timestamped("员工名单"), rows);
    }
}
