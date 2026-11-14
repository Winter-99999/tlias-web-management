package com.winter.pojo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 员工导出的行模型：固定 9 列。
 */
@Data
public class EmployeeExportVO {

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("性别")
    private String gender;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("岗位")
    private String job;

    @ExcelProperty("薪资")
    private Integer salary;

    @ExcelProperty("入职日期")
    private String entryDate;

    @ExcelProperty("部门")
    private String deptName;

    @ExcelProperty("角色")
    private String role;
}
