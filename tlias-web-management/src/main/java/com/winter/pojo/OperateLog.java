package com.winter.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperateLog {
    private Integer id;                //ID
    private Integer operateEmpId;      //操作人ID
    private LocalDateTime operateTime; //操作时间
    private String className;          //操作类名
    private String methodName;         //操作方法名
    private String methodParams;       //操作方法参数
    private String returnValue;        //操作方法返回值
    private Long costTime;             //操作耗时

    private String  module;            // 所属模块
    private String  operateType;       // 操作类型
    private Integer status;            // 执行结果：0失败 1成功
    private String  errorMsg;          // 异常信息
    private String  operateEmpName;    // 操作人姓名（联表查询用，不是表字段）
}
