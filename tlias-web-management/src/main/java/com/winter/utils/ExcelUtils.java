package com.winter.utils;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * Excel 导出工具：生成空模板、导出数据列表。
 */
public final class ExcelUtils {

    private static final DateTimeFormatter NAME_TIME =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ExcelUtils() {
    }

    /**
     * 生成带时间戳的文件名，例如“学员名单_20261114_201530”。
     * 服务端无法感知用户本地是否已有同名文件，用时间戳保证每次导出不重名。
     */
    public static String timestamped(String prefix) {
        return prefix + "_" + LocalDateTime.now().format(NAME_TIME);
    }

    /**
     * 用 DTO 上的 @ExcelProperty 生成表头，写出一个空模板。
     */
    public static void writeTemplate(HttpServletResponse response, Class<?> head,
                                     String sheetName, String fileName) throws IOException {
        write(response, head, sheetName, fileName, Collections.emptyList());
    }

    /**
     * 按 DTO 的表头导出数据。返回 void，直接写 response，不走 Result 包装。
     */
    public static void write(HttpServletResponse response, Class<?> head,
                             String sheetName, String fileName, List<?> data) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encoded + ".xlsx");
        EasyExcel.write(response.getOutputStream(), head)
                .sheet(sheetName)
                .doWrite(data);
    }
}
