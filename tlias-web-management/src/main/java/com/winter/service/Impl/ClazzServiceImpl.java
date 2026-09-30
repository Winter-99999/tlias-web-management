package com.winter.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winter.exception.BusinessException;
import com.winter.mapper.ClazzMapper;
import com.winter.mapper.EmpMapper;
import com.winter.pojo.Clazz;
import com.winter.pojo.ClazzQueryParam;
import com.winter.pojo.PageResult;
import com.winter.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired   
    private EmpMapper empMapper;

    //条件分页查询
    @Override
    public PageResult<Clazz> page(ClazzQueryParam clazzQueryParam) {
        //设置分页参数
        PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());
        //获取当前页数据
        List<Clazz> rows = clazzMapper.list(clazzQueryParam);
        //解析查询结果，封装PageResult并返回
        Page<Clazz> p = (Page<Clazz>) rows;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public List<Clazz> findAll() {
        return clazzMapper.findAll();
    }

    @Override
    public Clazz findById(Integer id) {
        return clazzMapper.getById(id);
    }

    @Override
    public void add(Clazz clazz) {
        //新增时班主任是必填的
        if (clazz.getMasterId() == null) {
            throw new BusinessException("班主任不能为空");
        }
        checkClazz(clazz);
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }

    @Override
    public void update(Clazz clazz) {
        checkClazz(clazz);
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }
    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        clazzMapper.delete(ids);
    }

    /**
     * 公共校验。注意这里是【条件式】校验：
     * 修改接口用的是动态SQL（<set>+<if>），允许只传部分字段，
     * 所以字段没传时就不校验它 —— 否则只改名字也会被误报"班主任不存在"。
     */
    private void checkClazz(Clazz clazz) {
        //校验班主任是否存在：传了 masterId 才校验
        if (clazz.getMasterId() != null && empMapper.getById(clazz.getMasterId()) == null) {
            throw new BusinessException("班主任不存在");
        }
        //校验日期：两个日期都传了才校验
        //（局限：只传 beginDate 时，无法和数据库里已有的 endDate 比对）
        if (clazz.getBeginDate() != null && clazz.getEndDate() != null
                && clazz.getEndDate().isBefore(clazz.getBeginDate())) {
            throw new BusinessException("结课时间不能早于开课时间");
        }
    }
}
