package com.winter.mapper;

import com.winter.pojo.Student;
import com.winter.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> list (StudentQueryParam studentQueryParam);

    Student findById(Integer id);

    void insert(Student student);

    void update(Student student);

    void delete(List<Integer> ids);
}
