package com.winter.service;

import com.winter.pojo.Emp;
import com.winter.pojo.EmpQueryParam;
import com.winter.pojo.PageResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface EmpService {

    PageResult<Emp> page(EmpQueryParam empQueryParam);

    void save(Emp emp);

    void delete(List<Integer> ids);
}
