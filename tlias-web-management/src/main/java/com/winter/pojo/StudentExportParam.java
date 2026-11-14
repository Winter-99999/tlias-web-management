package com.winter.pojo;

import lombok.Data;

/**
 * 学员导出条件：没有分页字段，导出的是“当前筛选条件下的全部数据”。
 * masterId 由服务端根据角色填充，前端传了也会被覆盖。
 */
@Data
public class StudentExportParam {
    private String name;
    private Integer degree;
    private Integer clazzId;
    private Integer masterId;
}
