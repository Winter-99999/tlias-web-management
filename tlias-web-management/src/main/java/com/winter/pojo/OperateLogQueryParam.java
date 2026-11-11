package com.winter.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class OperateLogQueryParam {
    private Integer page=1;
    private Integer pageSize = 10;     // 每页记录数
    private String module;             // 所属模块，模糊匹配
    private String operateType;        // 操作类型，精确匹配
    private Integer operateEmpId;      // 操作人ID
    private Integer status;            // 执行结果：0失败 1成功
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;           // 操作时间起
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;             // 操作时间止
}
