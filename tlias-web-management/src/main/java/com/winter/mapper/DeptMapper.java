package com.winter.mapper;

import com.winter.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {
    //由于创建时间、更新时间名称与数据库字段不一致，所以需要手动映射

    /* 第一种
    @Results({
            @Result(column = "create_time",property = "createTime"),
            @Result(column = "update_time",property = "updateTime")
    })
    @Select("select id,name,create_time,update_time from dept order by update_time")
    */

    //第二种 起别名
    @Select("select id,name,create_time createTime,update_time updateTime from dept order by update_time")
    List<Dept> findAll();

    @Delete("delete from dept where id=#{id}")
    void deleteById(Integer id);

    @Insert("insert into dept(name,create_time,update_time) values(#{name},#{createTime},#{updateTime})")
    void add(Dept dept);

    @Select("select id,name,create_time createTime,update_time updateTime from dept where id=#{id}")
    Dept findById(Integer id);

    @Update("update dept set name=#{name},update_time=#{updateTime} where id=#{id}")
    void update(Dept dept);
}
