package com.winter.mapper;

import com.winter.pojo.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateLogMapper {

    @Insert("insert into operate_log" +
            "(operate_emp_id, `module`, operate_type, operate_time, " +
            " class_name, method_name, method_params, return_value, " +
            " cost_time, `status`, error_msg) " +
            "values" +
            "(#{operateEmpId}, #{module}, #{operateType}, #{operateTime}, " +
            " #{className}, #{methodName}, #{methodParams}, #{returnValue}, " +
            " #{costTime}, #{status}, #{errorMsg})")
    void insert(OperateLog operateLog);
}