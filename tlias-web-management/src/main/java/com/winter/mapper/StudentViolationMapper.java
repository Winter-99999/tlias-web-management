package com.winter.mapper;

import com.winter.pojo.StudentViolation;
import com.winter.pojo.ViolationQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentViolationMapper {

    // 条件分页查询（PageHelper 会自动拼 limit）
    List<StudentViolation> page(ViolationQueryParam param);

    // 根据 id 查一条，撤销时要先取出来才知道学生 id 和扣分
    StudentViolation getById(Integer id);

    // 登记
    void insert(StudentViolation violation);

    // 撤销：物理删除
    int deleteById(Integer id);
}