package com.maple.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 设置新增和更新时填充时间
 *
 * @author maple
 * @date 2019-9-7
 */
@Component
public class MybatisPlusObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        setFieldValByName("createTime", now, metaObject);
        setFieldValByName("modifyTime", now, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("modifyTime", new Date(), metaObject);
    }
}