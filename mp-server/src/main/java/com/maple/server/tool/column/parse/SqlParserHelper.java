/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.maple.server.tool.column.parse;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SQL 解析辅助类
 *
 * @author hubin
 * @since 2018-07-22
 */
public class SqlParserHelper {

    public static final String DELEGATE_MAPPED_STATEMENT = "delegate.mappedStatement";
    /**
     * SQL 解析缓存
     * key 可能是 mappedStatement 的 ID,也可能是 class 的 name
     */
    private static final Map<String, Boolean> SQL_PARSER_INFO_CACHE = new ConcurrentHashMap<>();


    /**
     * 获取 SqlParser 注解信息
     *
     * @param metaObject 元数据对象
     */
    public static boolean getSqlParserInfo(MetaObject metaObject) {
        String id = getMappedStatement(metaObject).getId();
        Boolean value = SQL_PARSER_INFO_CACHE.get(id);
        if (value != null) {
            return value;
        }
        String mapperName = id.substring(0, id.lastIndexOf("."));
        return SQL_PARSER_INFO_CACHE.getOrDefault(mapperName, false);
    }

    /**
     * 获取当前执行 MappedStatement
     *
     * @param metaObject 元对象
     */
    public static MappedStatement getMappedStatement(MetaObject metaObject) {
        return (MappedStatement) metaObject.getValue(DELEGATE_MAPPED_STATEMENT);
    }
}
