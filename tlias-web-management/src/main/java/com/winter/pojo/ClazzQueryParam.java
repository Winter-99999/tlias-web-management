package com.winter.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ClazzQueryParam {
    private Integer page = 1;       // 当前页码
    private Integer pageSize = 10;  // 每页记录数
    private String name;            // 班级名称（模糊匹配）
    private String room;            // 教室（模糊匹配，输入 A 可查所有A栋，输入 101 可查所有101教室）
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;        // 开课时间范围-开始
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;          // 开课时间范围-结束
}
