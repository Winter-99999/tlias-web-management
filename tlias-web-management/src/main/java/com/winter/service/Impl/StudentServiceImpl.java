package com.winter.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winter.mapper.StudentMapper;
import com.winter.mapper.StudentViolationMapper;
import com.winter.pojo.PageResult;
import com.winter.pojo.Student;
import com.winter.pojo.StudentQueryParam;
import com.winter.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private static final int MAX_PAGE_SIZE = 100;

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StudentViolationMapper violationMapper;

    //条件分页查询
    @Override
    public PageResult<Student> page(StudentQueryParam studentQueryParam) {
        int page = (studentQueryParam.getPage() == null || studentQueryParam.getPage() < 1) ? 1 : studentQueryParam.getPage();
        int pageSize = (studentQueryParam.getPageSize() == null || studentQueryParam.getPageSize() < 1)
                ? 10 : Math.min(studentQueryParam.getPageSize(), MAX_PAGE_SIZE);
        PageHelper.startPage(page, pageSize);
        List<Student> rows = studentMapper.list(studentQueryParam);
        Page<Student> pageResult = (Page<Student>) rows;
        return new PageResult<>(pageResult.getTotal(), pageResult.getResult());
    }

    @Override
    public Student getById(Integer id) {
        return studentMapper.findById(id);
    }

    @Override
    public void add(Student student) {
        student.setViolationCount(0);
        student.setViolationScore(0);
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }

    @Override
    public void update(Student student) {
        // 汇总列只由违纪模块维护，忽略客户端传值
        student.setViolationCount(null);
        student.setViolationScore(null);
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Integer> ids) {
        violationMapper.deleteByStudentIds(ids);   // 先清违纪明细
        studentMapper.delete(ids);
    }
}
