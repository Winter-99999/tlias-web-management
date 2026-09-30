package com.winter.service;

import com.winter.pojo.Emp;
import com.winter.pojo.EmpQueryParam;
import com.winter.pojo.PageResult;

import java.util.List;

public interface EmpService {

    PageResult<Emp> page(EmpQueryParam empQueryParam);

    void save(Emp emp);

    void delete(List<Integer> ids);

    Emp getById(Integer id);

    void update(Emp emp);
}
