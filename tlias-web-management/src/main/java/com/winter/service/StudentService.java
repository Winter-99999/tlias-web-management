package com.winter.service;

import com.winter.pojo.PageResult;
import com.winter.pojo.Student;
import com.winter.pojo.StudentQueryParam;

import java.util.List;

public interface StudentService {
    PageResult<Student> page(StudentQueryParam studentQueryParam);

    Student getById(Integer id);

    void add(Student student);

    void update(Student student);

    void delete(List<Integer> ids);
}
