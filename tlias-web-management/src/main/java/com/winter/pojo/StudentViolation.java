package com.winter.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudentViolation {
    private Integer id;
    private Integer studentId;
    private String reason;
    private Integer score;
    private LocalDate occurDate;        // 对应 date，和 Student.graduationDate 一致用 LocalDate
    private String handler;
    private String remark;
    private LocalDateTime createTime;

    // 联表字段
    private String studentName;
    private String studentNo;
}