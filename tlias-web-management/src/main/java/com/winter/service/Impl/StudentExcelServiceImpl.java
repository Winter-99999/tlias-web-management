package com.winter.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.winter.exception.BusinessException;
import com.winter.mapper.ClazzMapper;
import com.winter.mapper.StudentMapper;
import com.winter.pojo.Clazz;
import com.winter.pojo.Student;
import com.winter.pojo.StudentExportParam;
import com.winter.pojo.dto.StudentExportVO;
import com.winter.pojo.dto.StudentImportDTO;
import com.winter.pojo.dto.ImportResult;
import com.winter.service.StudentExcelService;
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
public class StudentExcelServiceImpl implements StudentExcelService {

    private static final int MAX_ROWS = 1000;

    private static final Map<String, Integer> DEGREE = Map.of(
            "初中", 1, "高中", 2, "大专", 3, "本科", 4, "硕士", 5, "博士", 6);

    private static final Map<Integer, String> DEGREE_TEXT = Map.of(
            1, "初中", 2, "高中", 3, "大专", 4, "本科", 5, "硕士", 6, "博士");

    private static final DateTimeFormatter[] DATE_FORMATS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/M/d"),
            DateTimeFormatter.ofPattern("yyyy.M.d"),
            DateTimeFormatter.ofPattern("yyyy年M月d日")
    };

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ClazzMapper clazzMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportResult importStudents(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("请选择要导入的 Excel 文件");
        }

        List<StudentImportDTO> rows;
        try {
            rows = EasyExcel.read(file.getInputStream())
                    .head(StudentImportDTO.class)
                    .sheet()
                    .doReadSync();
        } catch (Exception e) {
            log.warn("导入文件解析失败", e);
            throw new BusinessException("文件解析失败，请使用下载的模板并确认格式为 .xlsx");
        }

        if (rows == null || rows.isEmpty()) {
            throw new BusinessException("文件中没有可导入的数据");
        }
        if (rows.size() > MAX_ROWS) {
            throw new BusinessException("单次最多导入 " + MAX_ROWS + " 行，请拆分后再试");
        }

        Map<String, Integer> clazzMap = new HashMap<>();
        Set<String> duplicatedClazz = new HashSet<>();
        for (Clazz clazz : clazzMapper.findAll()) {
            if (clazzMap.containsKey(clazz.getName())) {
                duplicatedClazz.add(clazz.getName());
            } else {
                clazzMap.put(clazz.getName(), clazz.getId());
            }
        }

        List<Student> validRows = new ArrayList<>();
        List<ImportResult.RowError> errors = new ArrayList<>();
        Set<String> fileNos = new HashSet<>();
        Set<String> filePhones = new HashSet<>();

        for (int i = 0; i < rows.size(); i++) {
            StudentImportDTO dto = rows.get(i);
            int rowNo = i + 2;   // 表头占第 1 行
            Student student = new Student();
            String reason = validate(dto, student, clazzMap, duplicatedClazz, fileNos, filePhones);
            if (reason != null) {
                errors.add(new ImportResult.RowError(rowNo, text(dto.getName()), reason));
            } else {
                validRows.add(student);
            }
        }

        for (Student student : validRows) {
            studentMapper.insert(student);
        }

        ImportResult result = new ImportResult();
        result.setTotal(rows.size());
        result.setSuccessCount(validRows.size());
        result.setFailCount(errors.size());
        result.setErrors(errors);
        log.info("学员导入完成：总 {} 行，成功 {} 行，失败 {} 行",
                rows.size(), validRows.size(), errors.size());
        return result;
    }

    @Override
    public List<StudentExportVO> exportStudents(StudentExportParam param) {
        List<Student> list = studentMapper.exportList(param);
        List<StudentExportVO> result = new ArrayList<>(list.size());
        for (Student s : list) {
            StudentExportVO vo = new StudentExportVO();
            vo.setName(s.getName());
            vo.setNo(s.getNo());
            vo.setGender(s.getGender() == null ? "" : (s.getGender() == 1 ? "男" : "女"));
            vo.setPhone(s.getPhone());
            vo.setIdCard(s.getIdCard());
            vo.setIsCollege(s.getIsCollege() == null ? "" : (s.getIsCollege() == 1 ? "院校" : "其他"));
            vo.setAddress(s.getAddress());
            vo.setDegree(s.getDegree() == null ? "" : DEGREE_TEXT.getOrDefault(s.getDegree(), ""));
            vo.setGraduationDate(s.getGraduationDate() == null ? "" : s.getGraduationDate().toString());
            vo.setClazzName(s.getClazzName() == null ? "" : s.getClazzName());
            vo.setViolationCount(s.getViolationCount() == null ? 0 : s.getViolationCount());
            vo.setViolationScore(s.getViolationScore() == null ? 0 : s.getViolationScore());
            result.add(vo);
        }
        return result;
    }

    /**
     * 校验并填充实体；返回 null 表示这一行通过，否则返回失败原因。
     */
    private String validate(StudentImportDTO dto, Student student,
                            Map<String, Integer> clazzMap, Set<String> duplicatedClazz,
                            Set<String> fileNos, Set<String> filePhones) {
        String name = text(dto.getName());
        if (name.isEmpty()) {
            return "姓名不能为空";
        }
        if (name.length() > 10) {
            return "姓名不能超过 10 个字";
        }

        String no = text(dto.getNo());
        if (no.isEmpty()) {
            return "学号不能为空";
        }
        if (no.length() > 10) {
            return "学号不能超过 10 个字符";
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

        String idCard = text(dto.getIdCard());
        if (!idCard.isEmpty() && !idCard.matches("^\\d{17}[\\dXx]$")) {
            return "身份证号格式不正确（18 位）";
        }

        Integer isCollege = switch (text(dto.getIsCollege())) {
            case "院校" -> 1;
            case "其他" -> 0;
            default -> null;
        };
        if (isCollege == null) {
            return "院校性质只能填“院校”或“其他”";
        }

        String degreeText = text(dto.getDegree());
        Integer degree = DEGREE.get(degreeText);
        if (degree == null) {
            return "学历只能填：初中/高中/大专/本科/硕士/博士";
        }

        LocalDate graduationDate = null;
        String dateText = text(dto.getGraduationDate());
        if (!dateText.isEmpty()) {
            graduationDate = parseDate(dateText);
            if (graduationDate == null) {
                return "毕业时间格式应为 yyyy-MM-dd";
            }
        }

        String address = text(dto.getAddress());
        if (address.length() > 100) {
            return "联系地址不能超过 100 个字";
        }

        String clazzName = text(dto.getClazzName());
        if (clazzName.isEmpty()) {
            return "班级名称不能为空";
        }
        if (duplicatedClazz.contains(clazzName)) {
            return "班级名称重复，无法确定归属：" + clazzName;
        }
        Integer clazzId = clazzMap.get(clazzName);
        if (clazzId == null) {
            return "班级不存在：" + clazzName;
        }

        if (!fileNos.add(no)) {
            return "文件中学号重复：" + no;
        }
        if (!filePhones.add(phone)) {
            return "文件中手机号重复：" + phone;
        }
        if (studentMapper.countByNo(no) > 0) {
            return "学号已存在：" + no;
        }
        if (studentMapper.countByPhone(phone) > 0) {
            return "手机号已存在：" + phone;
        }

        student.setName(name);
        student.setNo(no);
        student.setGender(gender);
        student.setPhone(phone);
        student.setIdCard(idCard.isEmpty() ? null : idCard);
        student.setIsCollege(isCollege);
        student.setAddress(address.isEmpty() ? null : address);
        student.setDegree(degree);
        student.setGraduationDate(graduationDate);
        student.setClazzId(clazzId);
        student.setViolationCount(0);
        student.setViolationScore(0);
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        return null;
    }

    private LocalDate parseDate(String text) {
        for (DateTimeFormatter formatter : DATE_FORMATS) {
            try {
                return LocalDate.parse(text, formatter);
            } catch (DateTimeParseException ignored) {
                // 继续尝试下一种格式
            }
        }
        return null;
    }

    /**
     * 去掉首尾空格，并把 Excel 数字单元格可能产生的科学计数法还原成普通文本，
     * 例如手机号被存成数字时可能读成 1.3800138000E10。
     */
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
