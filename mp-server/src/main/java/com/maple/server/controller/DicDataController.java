package com.maple.server.controller;

import cn.hutool.core.util.StrUtil;
import com.maple.common.builder.Results;
import com.maple.common.entity.Result;
import com.maple.common.exception.ServiceException;
import com.maple.server.common.enums.YesOrNoEnum;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 字典控制器
 *
 * @author maple
 * @version 1.0
 * @since 2020-07-10 18:07
 */
@Slf4j
@RestController
@RequestMapping("dicData")
public class DicDataController {
    /**
     * 查询枚举数据
     *
     * @param searchKeys 用逗号隔开 key维护在{@link KeyEnumMappingEnum}中
     * @return 枚举数据(多个, 以查询参数中的值为key)
     */
    @ResponseBody
    @RequestMapping(value = "getEnum")
    @ApiOperation(value = "获取枚举数据", notes = "获取枚举数据")
    public Result<Map<String, Enum<?>[]>> get(String searchKeys) {
        try {
            List<String> searchKeyList = StrUtil.splitTrim(searchKeys, ",");
            Map<String, Enum<?>[]> map = new HashMap<>(searchKeyList.size());
            for (String searchKey : searchKeyList) {
                Class<? extends Enum<?>> enumClass = KeyEnumMappingEnum.findByKey(searchKey);
                if (Objects.isNull(enumClass)) {
                    continue;
                }
                // 统一转换返回值
                map.put(searchKey, enumClass.getEnumConstants());
            }
            return Results.success(map);
        } catch (Exception e) {
            log.error("请求数据出错了", e);
        }
        return Results.failure(new ServiceException("请求字段数据出错了"));
    }

    /**
     * 建和具体枚举映射枚举
     */
    @Getter
    @AllArgsConstructor
    private enum KeyEnumMappingEnum {
        /**
         * 枚举值
         */
        YES_OR_NO("yesOrNo", YesOrNoEnum.class);
        /**
         * key
         */
        private final String key;
        /**
         * 枚举类
         */
        private final Class<? extends Enum<?>> enumClass;

        public static Class<? extends Enum<?>> findByKey(String key) {
            if (StrUtil.isBlank(key)) {
                return null;
            }
            for (KeyEnumMappingEnum enumMappingEnum : KeyEnumMappingEnum.values()) {
                if (StrUtil.equalsIgnoreCase(key, enumMappingEnum.getKey())) {
                    return enumMappingEnum.getEnumClass();
                }
            }
            return null;
        }
    }
}
