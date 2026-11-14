package com.winter.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量导入的通用结果：满足“部分成功”——通过的行入库，失败的行返回行号与原因。
 * 学员、员工导入共用。
 */
@Data
public class ImportResult {

    private int total;          // 文件中的数据行数（不含表头）
    private int successCount;   // 成功入库行数
    private int failCount;      // 失败行数
    private List<RowError> errors = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RowError {
        private int row;        // Excel 行号：表头是第 1 行，数据从第 2 行开始
        private String name;    // 关键字段（学员姓名/员工姓名），便于人工核对
        private String reason;  // 失败原因
    }
}
