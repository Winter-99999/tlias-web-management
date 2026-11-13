package com.winter.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Emp {
    private Integer id;                 // ID,主键
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;            // 密码：只接收，不返回
    private String name;                // 姓名
    private Short gender;               // 性别，1:男，2:女
    private String phone;               // 手机号
    private Short job;                  // 职位，1班主任，2讲师，3学工主管，4教研主管，5咨询师
    private Integer salary;             // 薪资
    private String image;               // 头像
    private LocalDate entryDate;        // 入职日期
    private Integer deptId;             // 部门ID
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 修改时间
    private Short role;                 // 角色，1超级管理员，2教务，3任课教师
    //封装部门名称
    private String deptName; // 部门名称
    //封装员工经历
    private List<EmpExpr> exprList;
}
