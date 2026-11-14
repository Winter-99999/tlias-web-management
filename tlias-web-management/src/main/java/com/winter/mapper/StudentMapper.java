package com.winter.mapper;

import com.winter.pojo.Student;
import com.winter.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> list (StudentQueryParam studentQueryParam);

    Student findById(Integer id);

    void insert(Student student);

    void update(Student student);

    void delete(List<Integer> ids);

    void addViolation(@Param("studentId") Integer studentId, @Param("score") Integer score);

    void subViolation(@Param("studentId") Integer studentId, @Param("score") Integer score);
}
