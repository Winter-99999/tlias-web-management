package com.winter.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 员工导出条件：与列表查询一致，但不需要分页。
 */
@Data
public class EmpExportParam {
    private String name;
    private Integer gender;
    private Integer deptId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;
}
