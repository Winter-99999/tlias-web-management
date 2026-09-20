package com.winter.pojo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmpExpr {
    private Integer id;         // ID，主键
    private Integer empId;      // 员工ID（外键，关联emp.id）
    private LocalDate begin;    // 开始时间
    private LocalDate end;      // 结束时间
    private String company;     // 公司名称
    private String job;         // 职位
}
