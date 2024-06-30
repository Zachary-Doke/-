package com.yoyodiningDB.senior.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Title: JDBCUtil
 * @Author Tanght 363993584@qq.com
 * @Date 2024/6/16 16:46
 * @description: jdbc工具类(V1.0)
 */
/*
* 1.维护一个连接池对象
* 2.对外提供连接池中获取连接的方法
* 3.对外提供回收连接的方法
* 注意:工具类仅对外提供共性的功能代码,所以创建为静态的
* */
public class JDBCUtil {
    //创建连接池引用,要提供给当前项目全局使用,所以创建为静态的
    private static DataSource dataSource;

    //在项目启动时创建连接池对象,赋值给dataSource
    static{
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = JDBCUtil.class.getClassLoader().getResourceAsStream("db.properties");
            properties.load(resourceAsStream);

            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //2.对外提供连接池中获取连接的方法
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    //3.对外提供回收连接的方法
    public static void release(Connection connection) throws SQLException {
        connection.close();
    }
}
