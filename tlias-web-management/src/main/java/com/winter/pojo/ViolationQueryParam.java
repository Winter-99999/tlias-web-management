package com.winter.pojo;

import lombok.Data;

@Data
public class ViolationQueryParam {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer studentId;
    private String studentName;
}