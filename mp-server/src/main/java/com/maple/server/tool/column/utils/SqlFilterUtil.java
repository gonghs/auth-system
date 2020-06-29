package com.maple.server.tool.column.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Trazen
 * @version 1.0
 * @desc
 * @create 2019-05-31 10:16
 **/
public class SqlFilterUtil {


    /**
     * 初始化查询白名单   不需要加租户id过滤的
     */
    //定义需要过滤的表
    private static final List<String> blackList;

    /**
     * 初始化公共配置表
     */
    static{
        blackList = new ArrayList<String>() ;
        /*blackList.add("HEC_UPO_PRJ_DIC_TYPE");
        blackList.add("HEC_UPO_PRJ_DIC_DATA");
        blackList.add("HEC_UPO_COMM_PARAM");
        blackList.add("HEC_UPO_PRJ_TENANT");
        blackList.add("DUAL");
        blackList.add("HEC_UPO_PRJ_PUBLICITY");
        blackList.add("DUP_MQD_ORG_DO_STATUS");*/

        blackList.add("HEC_UPO_PROJECT");
    }

    public static void addBlackList(String args []) {
        blackList.addAll(Arrays.asList(args));
    }

    /**
     * 判断 是否 在黑名单里面
     *
     * @param tableName
     * @return 是否需要过滤
     */

    public static boolean blackFilter(String tableName) {
        return (blackList.contains(tableName.toUpperCase()));
    }

    /**
     * 判断 是否 在黑名单里面 如果多个表有一个表非黑名单 返回true
     *
     * @param tableNames
     * @return 是否需要过滤
     */
    public static boolean blackOrFilter(List<String> tableNames) {
        return  (tableNames.stream().anyMatch((v)->
                blackList.stream().anyMatch(t-> t.equals(v.toUpperCase()))));
    }

    /**
     * 判断 是否 在黑名单里面 如果多个表有一个表非黑名单 返回false
     *
     * @param tableNames
     * @return 是否需要过滤
     */
    public static boolean blackAndFilter(List<String> tableNames) {
        return  (tableNames.stream().allMatch((v)->
                blackList.stream().anyMatch(t->t.equals(v.toUpperCase()))));
    }

}
