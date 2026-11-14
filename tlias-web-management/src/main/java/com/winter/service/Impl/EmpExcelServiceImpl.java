package com.winter.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.winter.exception.BusinessException;
import com.winter.mapper.DeptMapper;
import com.winter.mapper.EmpMapper;
import com.winter.pojo.Dept;
import com.winter.pojo.Emp;
import com.winter.pojo.EmpExportParam;
import com.winter.pojo.dto.EmployeeExportVO;
import com.winter.pojo.dto.EmployeeImportDTO;
import com.winter.pojo.dto.ImportResult;
import com.winter.service.EmpExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class EmpExcelServiceImpl implements EmpExcelService {

    private static final int MAX_ROWS = 1000;

    private static final Map<String, Integer> JOB = Map.of(
            "班主任", 1, "讲师", 2, "学工主管", 3, "教研主管", 4, "咨询师", 5);

    private static final Map<Integer, String> JOB_TEXT = Map.of(
            1, "班主任", 2, "讲师", 3, "学工主管", 4, "教研主管", 5, "咨询师");

    private static final Map<Integer, String> ROLE_TEXT = Map.of(
            1, "超级管理员", 2, "教务", 3, "任课教师");

    private static final DateTimeFormatter[] DATE_FORMATS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/M/d"),
            DateTimeFormatter.ofPattern("yyyy.M.d"),
            DateTimeFormatter.ofPattern("yyyy年M月d日")
    };

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private DeptMapper deptMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportResult importEmployees(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("请选择要导入的 Excel 文件");
        }

        List<EmployeeImportDTO> rows;
        try {
            rows = EasyExcel.read(file.getInputStream())
                    .head(EmployeeImportDTO.class)
                    .sheet()
                    .doReadSync();
        } catch (Exception e) {
            log.warn("员工导入文件解析失败", e);
            throw new BusinessException("文件解析失败，请使用下载的模板并确认格式为 .xlsx");
        }

        if (rows == null || rows.isEmpty()) {
            throw new BusinessException("文件中没有可导入的数据");
        }
        if (rows.size() > MAX_ROWS) {
            throw new BusinessException("单次最多导入 " + MAX_ROWS + " 行，请拆分后再试");
        }

        Map<String, Integer> deptMap = new HashMap<>();
        for (Dept dept : deptMapper.findAll()) {
            deptMap.put(dept.getName(), dept.getId());
        }

        List<Emp> validRows = new ArrayList<>();
        List<ImportResult.RowError> errors = new ArrayList<>();
        Set<String> fileUsernames = new HashSet<>();
        Set<String> filePhones = new HashSet<>();

        for (int i = 0; i < rows.size(); i++) {
            EmployeeImportDTO dto = rows.get(i);
            int rowNo = i + 2;
            Emp emp = new Emp();
            String reason = validate(dto, emp, deptMap, fileUsernames, filePhones);
            if (reason != null) {
                errors.add(new ImportResult.RowError(rowNo, text(dto.getName()), reason));
            } else {
                validRows.add(emp);
            }
        }

        for (Emp emp : validRows) {
            empMapper.insert(emp);
        }

        ImportResult result = new ImportResult();
        result.setTotal(rows.size());
        result.setSuccessCount(validRows.size());
        result.setFailCount(errors.size());
        result.setErrors(errors);
        log.info("员工导入完成：总 {} 行，成功 {} 行，失败 {} 行",
                rows.size(), validRows.size(), errors.size());
        return result;
    }

    @Override
    public List<EmployeeExportVO> exportEmployees(EmpExportParam param) {
        List<Emp> list = empMapper.exportList(param);
        List<EmployeeExportVO> result = new ArrayList<>(list.size());
        for (Emp emp : list) {
            EmployeeExportVO vo = new EmployeeExportVO();
            vo.setUsername(emp.getUsername());
            vo.setName(emp.getName());
            vo.setGender(emp.getGender() == null ? "" : (emp.getGender() == 1 ? "男" : "女"));
            vo.setPhone(emp.getPhone());
            vo.setJob(emp.getJob() == null ? "" : JOB_TEXT.getOrDefault(emp.getJob().intValue(), ""));
            vo.setSalary(emp.getSalary());
            vo.setEntryDate(emp.getEntryDate() == null ? "" : emp.getEntryDate().toString());
            vo.setDeptName(emp.getDeptName() == null ? "" : emp.getDeptName());
            vo.setRole(emp.getRole() == null ? "" : ROLE_TEXT.getOrDefault(emp.getRole().intValue(), ""));
            result.add(vo);
        }
        return result;
    }

    private String validate(EmployeeImportDTO dto, Emp emp,
                            Map<String, Integer> deptMap,
                            Set<String> fileUsernames, Set<String> filePhones) {
        String username = text(dto.getUsername());
        if (username.isEmpty()) {
            return "用户名不能为空";
        }
        if (username.length() > 20) {
            return "用户名不能超过 20 个字符";
        }

        String name = text(dto.getName());
        if (name.isEmpty()) {
            return "姓名不能为空";
        }
        if (name.length() > 10) {
            return "姓名不能超过 10 个字";
        }

        Integer gender = switch (text(dto.getGender())) {
            case "男" -> 1;
            case "女" -> 2;
            default -> null;
        };
        if (gender == null) {
            return "性别只能填“男”或“女”";
        }

        String phone = text(dto.getPhone());
        if (!phone.matches("^1\\d{10}$")) {
            return "手机号格式不正确（11 位，1 开头）";
        }

        Integer job = JOB.get(text(dto.getJob()));
        if (job == null) {
            return "岗位只能填：班主任/讲师/学工主管/教研主管/咨询师";
        }

        Integer salary = null;
        String salaryText = text(dto.getSalary());
        if (!salaryText.isEmpty()) {
            try {
                salary = Integer.valueOf(salaryText);
            } catch (NumberFormatException e) {
                return "薪资必须是整数";
            }
            if (salary < 0) {
                return "薪资不能为负数";
            }
        }

        LocalDate entryDate = null;
        String dateText = text(dto.getEntryDate());
        if (!dateText.isEmpty()) {
            entryDate = parseDate(dateText);
            if (entryDate == null) {
                return "入职日期格式应为 yyyy-MM-dd";
            }
        }

        String deptName = text(dto.getDeptName());
        if (deptName.isEmpty()) {
            return "部门名称不能为空";
        }
        Integer deptId = deptMap.get(deptName);
        if (deptId == null) {
            return "部门不存在：" + deptName;
        }

        if (!fileUsernames.add(username)) {
            return "文件中用户名重复：" + username;
        }
        if (!filePhones.add(phone)) {
            return "文件中手机号重复：" + phone;
        }
        if (empMapper.countByUsername(username) > 0) {
            return "用户名已存在：" + username;
        }
        if (empMapper.countByPhone(phone) > 0) {
            return "手机号已存在：" + phone;
        }

        emp.setUsername(username);
        emp.setName(name);
        emp.setGender(gender.shortValue());
        emp.setPhone(phone);
        emp.setJob(job.shortValue());
        emp.setRole((short) 2);          // 导入的员工默认是教务，角色只能由超管后续调整
        emp.setSalary(salary);
        emp.setEntryDate(entryDate);
        emp.setDeptId(deptId);
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        return null;
    }

    private LocalDate parseDate(String text) {
        for (DateTimeFormatter formatter : DATE_FORMATS) {
            try {
                return LocalDate.parse(text, formatter);
            } catch (DateTimeParseException ignored) {
                // 尝试下一种格式
            }
        }
        return null;
    }

    private String text(String raw) {
        if (raw == null) {
            return "";
        }
        String value = raw.trim();
        if (value.isEmpty()) {
            return value;
        }
        if (value.matches("^\\d+(\\.\\d+)?[eE]\\+?\\d+$")) {
            try {
                return new BigDecimal(value).stripTrailingZeros().toPlainString();
            } catch (NumberFormatException ignored) {
                return value;
            }
        }
        return value;
    }
}
