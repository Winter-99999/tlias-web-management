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
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StudentViolationMapper violationMapper;

    //条件分页查询
    @Override
    public PageResult<Student> page(StudentQueryParam studentQueryParam) {
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        List<Student> rows = studentMapper.list(studentQueryParam);
        Page<Student> page = (Page<Student>) rows;
        return new PageResult<>(page.getTotal(), page.getResult());
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
