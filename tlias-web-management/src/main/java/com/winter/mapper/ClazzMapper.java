package com.winter.mapper;

import com.winter.pojo.Clazz;
import com.winter.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClazzMapper {

    List<Clazz> findAll();

    //条件分页查询
    List<Clazz> list(ClazzQueryParam clazzQueryParam);

    Clazz getById(Integer id);

    void insert(Clazz clazz);

    void update(Clazz clazz);

    void delete(List<Integer> ids);
}
