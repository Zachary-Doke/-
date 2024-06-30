package com.yoyodiningDB.senior.dao;

import com.yoyodiningDB.senior.util.JDBCUtilV2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: BaseDAO
 * @Author Tanght 363993584@qq.com
 * @Date 2024/6/16 22:05
 * @description:
 */
/*
* 将共性的数据库操作代码封装在BaseDAO里
* */
public class BaseDAO {
    /*
    * 通用的增删改的方法
    * */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        //通过JDBCUtilV2获取数据库连接
        Connection connection = JDBCUtilV2.getConnection();

        //预编译SQL语句
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //为占位符赋值,执行SQL,接收返回结果
        if(params != null && params.length > 0){
            for(int i = 0; i < params.length; i++){
                //占位符从1开始;
                preparedStatement.setObject(i+1,params[i]);
            }
        }
        int row = preparedStatement.executeUpdate();
        //释放资源
        preparedStatement.close();
        if(connection.getAutoCommit()){
            JDBCUtilV2.release();
        }
        //返回结果
        return row;
    }
    /*
    * 通用的查询:多行多列,单行多列,单行单列
    *   多行多列:List<Employee>
    *   单行多列:Employee
    *   单行单列:封装的是一个结果,Double,Integer...
    * 封装过程:
    *   1.返回的类型:泛型:类型不确定,调用者知道,调用时将此次查询的结果类型告知BaseDAO就可以了 T
    *   2.返回的结果:通用,List
    *   3.结果的封装:反射,要求调用者告知BaseDAO要封装对象的类对象 Class
    * */
    public <T> List<T> executeQuery(Class<T> clazz, String sql, Object... params) throws SQLException, InstantiationException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        //获取连接
        Connection connection = JDBCUtilV2.getConnection();

        //预编译sql语句
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //设置占位符值
        if(params != null && params.length > 0){
            for(int i = 0; i < params.length; i++){
                preparedStatement.setObject(i+1, params[i]);
            }
        }

        //执行sql,并接收返回的结果集
        ResultSet resultSet = preparedStatement.executeQuery();

        //获取结果集中的元数据对象
        //包含了:列的数量、每个列的名称
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<T> list = new ArrayList<>();
        //处理结果
        while(resultSet.next()){
            //循环一次代表有一行数据,通过反射创建一个对象
            T t = clazz.newInstance();
            //循环遍历当前行的列,循环几次看有多少列
            for (int i = 1; i <= columnCount; i++){
                //通过下标获取列值
                Object value = resultSet.getObject(i);

                //获取到的列value值,这个值就是t这个对象中的某一个属性
                //获取当前拿到的列的名字 = 对象的属性名
                String fieldName = metaData.getColumnLabel(i);
                //通过类对象和fieldName获取要分装的对象的属性
                Field field = clazz.getDeclaredField(fieldName);
                //突破封装的private
                field.setAccessible(true);
                field.set(t,value);
            }
            list.add(t);
        }
        resultSet.close();
        preparedStatement.close();
        if(connection.getAutoCommit()){
            JDBCUtilV2.release();
        }
        return list;
    }

    public <T> T excuteQueryBean(Class<T> clazz, String sql, Object... params) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<T> list = this.executeQuery(clazz, sql, params);
        if(list == null || list.size() == 0)return null;
        return list.get(0);
    }
}
