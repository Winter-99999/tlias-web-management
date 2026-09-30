package com.winter.service;

import com.winter.pojo.Clazz;
import com.winter.pojo.ClazzQueryParam;
import com.winter.pojo.PageResult;

import java.util.List;

public interface ClazzService {

    //条件分页查询
    PageResult<Clazz> page(ClazzQueryParam clazzQueryParam);

    List<Clazz> findAll();

    Clazz findById(Integer id);

    void add(Clazz clazz);

    void update(Clazz clazz);

    void delete(List<Integer> ids);
}
