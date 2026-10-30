package com.winter.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winter.mapper.StudentMapper;
import com.winter.pojo.PageResult;
import com.winter.pojo.Student;
import com.winter.pojo.StudentQueryParam;
import com.winter.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

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
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }

    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Override
    public void delete(List<Integer> ids) {
        studentMapper.delete(ids);
    }
}
