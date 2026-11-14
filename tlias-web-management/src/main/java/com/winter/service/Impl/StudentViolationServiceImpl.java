package com.winter.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winter.exception.BusinessException;
import com.winter.mapper.EmpMapper;
import com.winter.mapper.StudentMapper;
import com.winter.mapper.StudentViolationMapper;
import com.winter.pojo.PageResult;
import com.winter.pojo.Student;
import com.winter.pojo.StudentViolation;
import com.winter.pojo.ViolationQueryParam;
import com.winter.service.StudentViolationService;
import com.winter.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentViolationServiceImpl implements StudentViolationService {

    @Autowired
    private StudentViolationMapper violationMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private EmpMapper empMapper;

    @Override
    public PageResult<StudentViolation> page(ViolationQueryParam param) {
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<StudentViolation> rows = violationMapper.page(param);
        Page<StudentViolation> p = (Page<StudentViolation>) rows;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentViolation register(StudentViolation violation) {
        check(violation);

        Student student = studentMapper.findById(violation.getStudentId());
        if (student == null) {
            throw new BusinessException("学员不存在");
        }

        // 处理人 = 当前登录人，前端传什么都会被覆盖
        Integer loginEmpId = UserContext.getUserId();
        if (loginEmpId != null) {
            violation.setHandler(empMapper.getNameById(loginEmpId));
        }

        violation.setCreateTime(LocalDateTime.now());
        violationMapper.insert(violation);                                   // ① 明细
        studentMapper.addViolation(violation.getStudentId(), violation.getScore()); // ② 汇总

        return violationMapper.getById(violation.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revoke(Integer id) {
        StudentViolation violation = violationMapper.getById(id);
        if (violation == null) {
            throw new BusinessException("违纪记录不存在");
        }

        int affected = violationMapper.deleteById(id);// ① 删明细
        if (affected == 0) {
            throw new BusinessException("违纪记录不存在或已被撤销");
        }
        studentMapper.subViolation(violation.getStudentId(), violation.getScore()); // ② 回滚汇总
    }

    private void check(StudentViolation v) {
        if (v.getStudentId() == null) {
            throw new BusinessException("学员不能为空");
        }
        if (v.getScore() == null || v.getScore() <= 0) {
            throw new BusinessException("扣分必须大于 0");
        }
        if (v.getScore() > 100) {
            throw new BusinessException("单次扣分不能超过 100");
        }
        if (v.getReason() == null || v.getReason().isBlank()) {
            throw new BusinessException("违纪原因不能为空");
        }
        if (v.getReason().length() > 100) {
            throw new BusinessException("违纪原因不能超过 100 字");
        }
        if (v.getOccurDate() == null) {
            v.setOccurDate(LocalDate.now());
        }
        if (v.getOccurDate().isAfter(LocalDate.now())) {
            throw new BusinessException("违纪日期不能晚于今天");
        }
        if (v.getHandler() != null && v.getHandler().length() > 20) {
            throw new BusinessException("处理人姓名不能超过 20 字");
        }
        if (v.getRemark() != null && v.getRemark().length() > 255) {
            throw new BusinessException("备注不能超过 255 字");
        }
    }
}