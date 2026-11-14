package com.winter.controller;

import com.winter.anno.Log;
import com.winter.anno.RequireRole;
import com.winter.pojo.*;
import com.winter.pojo.dto.StudentExportVO;
import com.winter.pojo.dto.StudentImportDTO;
import com.winter.service.StudentExcelService;
import com.winter.service.StudentService;
import com.winter.utils.ExcelUtils;
import com.winter.utils.UserContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequireRole({1, 2, 3})
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentExcelService studentExcelService;

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
    @Log(module = "学员管理", type = "新增")
    @RequireRole({1, 2})
    @PostMapping
    public Result add(@RequestBody Student student){
        log.info("新增学生，参数：{}",student);
        studentService.add(student);
        return Result.success(studentService.getById(student.getId()));
    }
    //修改学生信息
    @Log(module = "学员管理", type = "修改")
    @RequireRole({1, 2})
    @PutMapping
    public Result update(@RequestBody Student student){
        log.info("修改学生，参数：{}",student);
        studentService.update(student);
        return Result.success();
    }
    //接收同名的多个 query 参数，封装成 List，集合/数组接收 query 参数，必须加 @RequestParam，否则绑定失败
    @Log(module = "学员管理", type = "删除")
    @RequireRole({1, 2})
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids){
        log.info("删除学生，ids：{}",ids);
        studentService.delete(ids);
        return Result.success();
    }

    //下载学员导入模板（表头由 StudentImportDTO 的 @ExcelProperty 生成）
    @GetMapping("/template")
    public void template(HttpServletResponse response) throws IOException {
        log.info("下载学员导入模板");
        ExcelUtils.writeTemplate(response, StudentImportDTO.class, "学员导入模板", "学员导入模板");
    }

    //批量导入学员
    @Log(module = "学员管理", type = "导入")
    @RequireRole({1, 2})
    @PostMapping("/import")
    public Result importStudents(@RequestParam("file") MultipartFile file) {
        log.info("批量导入学员，文件名：{}", file.getOriginalFilename());
        return Result.success(studentExcelService.importStudents(file));
    }

    //导出学员名单：任课教师只能导出本人担任班主任的班级
    @Log(module = "学员管理", type = "导出")
    @GetMapping("/export")
    public void export(StudentExportParam param, HttpServletResponse response) throws IOException {
        if (Integer.valueOf(3).equals(UserContext.getRole())) {
            // 任课教师只能看本人带的班，前端传什么都被覆盖
            param.setMasterId(UserContext.getUserId());
        }
        // 超管/教务：保留前端传入的 masterId 作为筛选条件，不传则导出全部
        List<StudentExportVO> rows = studentExcelService.exportStudents(param);
        log.info("导出学员 {} 条，条件：{}", rows.size(), param);
        ExcelUtils.write(response, StudentExportVO.class, "学员名单", ExcelUtils.timestamped("学员名单"), rows);
    }

}
