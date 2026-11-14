package com.winter.service;

import com.winter.pojo.PageResult;
import com.winter.pojo.StudentViolation;
import com.winter.pojo.ViolationQueryParam;

public interface StudentViolationService {
    PageResult<StudentViolation> page(ViolationQueryParam param);

    StudentViolation register(StudentViolation violation);

    void revoke(Integer id);
}