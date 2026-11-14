package com.winter.mapper;

import com.winter.pojo.Student;
import com.winter.pojo.StudentExportParam;
import com.winter.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    @Select("select count(*) from student where no = #{no}")
    int countByNo(String no);

    @Select("select count(*) from student where phone = #{phone}")
    int countByPhone(String phone);

    //导出：不分页，按条件取全部
    List<Student> exportList(StudentExportParam param);
}
