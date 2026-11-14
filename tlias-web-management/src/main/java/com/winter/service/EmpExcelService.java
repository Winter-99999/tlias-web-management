package com.winter.service;

import com.winter.pojo.EmpExportParam;
import com.winter.pojo.dto.EmployeeExportVO;
import com.winter.pojo.dto.ImportResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmpExcelService {

    ImportResult importEmployees(MultipartFile file);

    List<EmployeeExportVO> exportEmployees(EmpExportParam param);
}
