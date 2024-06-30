package com.yoyodiningDB.senior.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Title: JDBCUtilV2
 * @Author Tanght 363993584@qq.com
 * @Date 2024/6/16 17:09
 * @description:
 */
/*
 * 1.维护一个连接池对象,也维护了一个线程绑定变量的ThreadLocal对象
 * 2.对外提供在ThreadLocal中获取连接的方法
 * 3.对外提供回收连接的方法,回收过程中将要回收的连接从threadlocal中移除
 * 注意:工具类仅对外提供共性的功能代码,所以创建为静态的
 *      使用threadLocal为了一个线程在多次数据库操作的过程中,使用的是同一个连接!
 * */
public class JDBCUtilV2 {
    //创建连接池引用,要提供给当前项目全局使用,所以创建为静态的
    private static DataSource dataSource;
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    //在项目启动时创建连接池对象,赋值给dataSource
    static{
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = JDBCUtilV2.class.getClassLoader().getResourceAsStream("db.properties");
            properties.load(resourceAsStream);

            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //2.对外提供连接池中获取连接的方法
    public static Connection getConnection() throws SQLException {
        //从threadLocal中获取Connection
        Connection connection = threadLocal.get();
        //判断是否为第一次获取
        if(connection==null){
            //在连接池中获取一个连接,存储在threadLocal中
            connection = dataSource.getConnection();
            threadLocal.set(connection);
        }
        return connection;

    }
    //3.对外提供回收连接的方法
    public static void release() throws SQLException {
        Connection connection = threadLocal.get();
        if(connection!=null){
            //从threadLocal中移除当前已经存储的Connection对象
            threadLocal.remove();
            //如果开启了事务的手动提交,归还连接前要将事务的自动提交改为true
            connection.setAutoCommit(true);
            //将Connection对象归还给连接池
            connection.close();
        }
    }
}
