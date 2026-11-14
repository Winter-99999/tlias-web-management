package com.winter.service;

import com.winter.pojo.StudentExportParam;
import com.winter.pojo.dto.StudentExportVO;
import com.winter.pojo.dto.ImportResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentExcelService {

    /**
     * 批量导入学员：逐行校验，通过的行入库，失败的行返回行号与原因。
     */
    ImportResult importStudents(MultipartFile file);

    /**
     * 按条件导出学员：枚举值转成中文，供 Excel 写入。
     */
    List<StudentExportVO> exportStudents(StudentExportParam param);
}
