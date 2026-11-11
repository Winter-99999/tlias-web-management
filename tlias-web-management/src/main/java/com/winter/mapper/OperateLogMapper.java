package com.winter.mapper;

import com.winter.pojo.OperateLog;
import com.winter.pojo.OperateLogQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    // 条件分页查询
    List<OperateLog> list(OperateLogQueryParam param);

    // 根据 id 查询详情
    OperateLog getById(Integer id);
}