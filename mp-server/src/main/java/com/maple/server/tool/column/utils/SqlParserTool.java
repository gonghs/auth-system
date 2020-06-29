package com.maple.server.tool.column.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Trazen
 * @version 1.0
 * @desc
 * jsqlparser解析SQL工具类
 * PlainSelect类不支持union、union all等请使用SetOperationList接口
 * @create 2019-05-31 10:22
 **/
public class SqlParserTool {

    /**
     * 获取sql操作接口,与上面类型判断结合使用
     * example:
     * String sql = "create table a(a string)";
     * SqlType sqlType = SqlParserTool.getSqlType(sql);
     * if(sqlType.equals(SqlType.SELECT)){
     *     Select statement = (Select) SqlParserTool.getStatement(sql);
     *  }
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    public static Statement getStatement(String sql) throws JSQLParserException {
        return CCJSqlParserUtil.parse(new StringReader(sql));
    }


    /**
     * 获取tables的表名
     * @param statement
     * @return
     */
    public static List<String> getTableNameList(Select statement){
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(statement);
        return tableList;
    }

    /**
     * 获取join层级
     * @param selectBody
     * @return
     */
    public static List<Join> getJoins(SelectBody selectBody){
        if(selectBody instanceof PlainSelect){
            List<Join> joins =((PlainSelect) selectBody).getJoins();
            return joins;
        }
        return new ArrayList<Join>();
    }

    /**
     *
     * @param selectBody
     * @return
     */
    public static List<Table> getIntoTables(SelectBody selectBody){
        if(selectBody instanceof PlainSelect){
            List<Table> tables = ((PlainSelect) selectBody).getIntoTables();
            return tables;
        }
        return new ArrayList<Table>();
    }

    /**
     *
     * @param selectBody
     * @return
     */
    public static void setIntoTables(SelectBody selectBody, List<Table> tables){
        if(selectBody instanceof PlainSelect){
            ((PlainSelect) selectBody).setIntoTables(tables);
        }
    }

    /**
     * 获取limit值
     * @param selectBody
     * @return
     */
    public static Limit getLimit(SelectBody selectBody){
        if(selectBody instanceof PlainSelect){
            Limit limit = ((PlainSelect) selectBody).getLimit();
            return limit;
        }
        return null;
    }

    /**
     * 为SQL增加limit值
     * @param selectBody
     * @param l
     */
    public  static void setLimit(SelectBody selectBody, long l){
        if(selectBody instanceof PlainSelect){
            Limit limit = new Limit();
            limit.setRowCount(new LongValue(String.valueOf(l)));
            ((PlainSelect) selectBody).setLimit(limit);
        }
    }

    /**
     * 获取FromItem不支持子查询操作
     * @param selectBody
     * @return
     */
    public  static FromItem getFromItem(SelectBody selectBody){
        if(selectBody instanceof PlainSelect){
            FromItem fromItem = ((PlainSelect) selectBody).getFromItem();
            return fromItem;
        }else if(selectBody instanceof WithItem){
            SqlParserTool.getFromItem(((WithItem) selectBody).getSelectBody());
        }
        return null;
    }

    /**
     * 获取子查询
     * @param selectBody
     * @return
     */
    public  static SubSelect getSubSelect(SelectBody selectBody){
        if(selectBody instanceof PlainSelect){
            FromItem fromItem = ((PlainSelect) selectBody).getFromItem();
            if(fromItem instanceof SubSelect){
                return ((SubSelect) fromItem);
            }
        }else if(selectBody instanceof WithItem){
            SqlParserTool.getSubSelect(((WithItem) selectBody).getSelectBody());
        }
        return null;
    }

    /**
     * 判断是否为多级子查询
     * @param selectBody
     * @return
     */
    public static boolean isMultiSubSelect(SelectBody selectBody){
        if(selectBody instanceof PlainSelect){
            FromItem fromItem = ((PlainSelect) selectBody).getFromItem();
            if(fromItem instanceof SubSelect){
                SelectBody subBody = ((SubSelect) fromItem).getSelectBody();
                if(subBody instanceof PlainSelect){
                    FromItem subFromItem = ((PlainSelect) subBody).getFromItem();
                    if(subFromItem instanceof SubSelect){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取查询字段
     * @param selectBody
     * @return
     */
    public  static List<SelectItem> getSelectItems(SelectBody selectBody){
        if(selectBody instanceof PlainSelect){
            List<SelectItem> selectItems = ((PlainSelect) selectBody).getSelectItems();
            return selectItems;
        }
        return null;
    }



}
