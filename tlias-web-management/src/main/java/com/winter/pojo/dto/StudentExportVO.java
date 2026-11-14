package com.winter.pojo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 学员导出的行模型：固定 12 列，枚举值在 Service 里转成中文。
 */
@Data
public class StudentExportVO {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("学号")
    private String no;

    @ExcelProperty("性别")
    private String gender;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("身份证号")
    private String idCard;

    @ExcelProperty("院校性质")
    private String isCollege;

    @ExcelProperty("联系地址")
    private String address;

    @ExcelProperty("学历")
    private String degree;

    @ExcelProperty("毕业时间")
    private String graduationDate;

    @ExcelProperty("班级")
    private String clazzName;

    @ExcelProperty("违纪次数")
    private Integer violationCount;

    @ExcelProperty("违纪扣分")
    private Integer violationScore;
}
