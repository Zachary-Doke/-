package com.atguigu.advanced.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Title: DruidTest
 * @Author Tanght 363993584@qq.com
 * @Date 2024/6/16 15:46
 * @description:
 */
public class DruidTest {
    @Test
    public void testHardDruid() throws SQLException {
        /*
        * 硬编码:将连接池的配置信息和java代码耦合在一起
        * 1.创建DruidDataSource连接池对象
        * 2.设置连接池的配置信息[必要|非必要]
        * 3.通过连接池获取对象
        * 4.回收连接 [不是释放连接,而是将连接归还给连接池,给其他线程进行复用]
        * */
        //1.创建DruidDataSource连接池对象
        DruidDataSource druidDataSource = new DruidDataSource();

        //2.设置连接池的配置信息[必要|非必要]
        //2.1必要设置
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql:///atguigu");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("1234");
        //2.1非必要配置
        druidDataSource.setInitialSize(10);//连接对象初始值
        druidDataSource.setMaxActive(20);//最大值

        //3.通过连接池获取对象
        Connection connection = druidDataSource.getConnection();
        System.out.println(connection);

        //4.回收连接 [不是释放连接,而是将连接归还给连接池,给其他线程进行复用]
        connection.close();
    }
    @Test
    public void testResourcesDruid() throws Exception {
        //1.创建properties集合,用于存储外部配置文件的key和value
        Properties properties = new Properties();
        //2.读取外部配置文件,获取输入流,加载到properties文件中
        InputStream resourceAsStream = DruidTest.class.getClassLoader().getResourceAsStream("db.properties");
        properties.load(resourceAsStream);
        //3.基于properties集合构建druidDataSource连接池
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        //4.通过连接池获取连接对象
        Connection connection = dataSource.getConnection();
        //5.开发crud

        //6.回收连接
        connection.close();
    }
}
