package com.winter.pojo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 员工批量导入的数据模型。全部用 String 接收，Service 里再转换和校验。
 * 密码不在这里：新员工统一使用数据库默认密码 123456。
 */
@Data
public class EmployeeImportDTO {

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("性别")
    private String gender;        // 男 / 女

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("岗位")
    private String job;           // 班主任 / 讲师 / 学工主管 / 教研主管 / 咨询师

    @ExcelProperty("薪资")
    private String salary;        // 可空

    @ExcelProperty("入职日期")
    private String entryDate;     // yyyy-MM-dd，可空

    @ExcelProperty("部门名称")
    private String deptName;
}
