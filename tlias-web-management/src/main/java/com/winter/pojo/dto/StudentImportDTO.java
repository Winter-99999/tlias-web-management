package com.winter.pojo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 学员批量导入的数据模型。
 * 所有字段都用 String 接收：Excel 里可能混进空格、全角字符、各种日期格式，
 * 先按字符串接住，再在 Service 里逐行校验并转换成实体字段，比让框架直接转换更可控。
 */
@Data
public class StudentImportDTO {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("学号")
    private String no;

    @ExcelProperty("性别")
    private String gender;          // 填“男”或“女”

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("身份证号")
    private String idCard;

    @ExcelProperty("院校性质")
    private String isCollege;       // 填“统招”或“非统招”

    @ExcelProperty("联系地址")
    private String address;

    @ExcelProperty("学历")
    private String degree;          // 填“本科”“大专”“硕士”等

    @ExcelProperty("毕业时间")
    private String graduationDate;  // yyyy-MM-dd

    @ExcelProperty("班级名称")
    private String clazzName;
}
