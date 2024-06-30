package com.yoyodiningDB.advanced;

import com.yoyodiningDB.advanced.pojo.Employee;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: JDBCAdvanced
 * @Author Tanght 363993584@qq.com
 * @Date 2024/6/15 10:47
 * @description:
 */
public class JDBCAdvanced {
    @Test
    public void testORM() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "1234");
        PreparedStatement prestmt = conn.prepareStatement("select * from t_emp where emp_id = ?");
        prestmt.setInt(1,1);
        ResultSet resultSet = prestmt.executeQuery();

        Employee employee = null;
        if(resultSet.next()){
            int empId = resultSet.getInt("emp_id");
            String empName = resultSet.getString("emp_name");
            double empSalary = resultSet.getDouble("emp_salary");
            int empAge = resultSet.getInt("emp_age");
            //为对象属性赋值
            employee = new Employee(empId,empName,empSalary,empAge);
        }
        System.out.println(employee.toString());
        resultSet.close();
        prestmt.close();
        conn.close();
    }
    @Test
    public void testORMList() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "1234");
        PreparedStatement prestmt = conn.prepareStatement("select * from t_emp ");
        ResultSet resultSet = prestmt.executeQuery();

        Employee employee = null;
        List<Employee> employeeList = new ArrayList<>();
        while(resultSet.next()){
            int empId = resultSet.getInt("emp_id");
            String empName = resultSet.getString("emp_name");
            double empSalary = resultSet.getDouble("emp_salary");
            int empAge = resultSet.getInt("emp_age");
            //为对象属性赋值
            employee = new Employee(empId,empName,empSalary,empAge);
            employeeList.add(employee);
        }
        for (Employee employee1 : employeeList) {
            System.out.println(employee1);
        }
        resultSet.close();
        prestmt.close();
        conn.close();
    }
    @Test
    public void testReturnPK() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "1234");
        String sql = "insert into t_emp(emp_name,emp_salary,emp_age) values(?,?,?)";
        //告知preparedStatement 返回新增数据的主键列的值
        PreparedStatement preparedStatement = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

        Employee employee = new Employee(null,"jack",1919.1,29);
        preparedStatement.setString(1, employee.getEmpName());
        preparedStatement.setDouble(2,employee.getEmpSalary());
        preparedStatement.setInt(3,employee.getEmpAge());
        int result = preparedStatement.executeUpdate();
        //如果放在if结构中不利于资源释放
        ResultSet generatedKeys = null;
        if(result > 0) {
            System.out.println("插入成功");
            //获取当前新增数据主键 回填到employee主键属性上
            //返回的主键值是一个单行单列结果
            generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                int empId =generatedKeys.getInt(1);
                employee.setEmpId(empId);
            }
        }
        else {
            System.out.println("插入失败");
        }
        System.out.println(employee);
        //没有插入成功时 可以防止空指针异常
        if(generatedKeys != null)
        generatedKeys.close();
        preparedStatement.close();
        conn.close();
    }
    @Test
    public void testMoreInsert() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "1234");
        String sql = "insert into t_emp(emp_name,emp_salary,emp_age) values(?,?,?)";
        //告知preparedStatement 返回新增数据的主键列的值
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        long start = System.currentTimeMillis();
        //java 和数据库交互多次效率低
        for(int i = 0; i < 10000; i++){
            preparedStatement.setString(1, "marry"+i);
            preparedStatement.setDouble(2,100.0+i);
            preparedStatement.setInt(3,20+i);
            preparedStatement.executeUpdate();
        }
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end - start)/1000);
        preparedStatement.close();
        conn.close();
    }
    @Test
    public void testBatch() throws Exception {
        //默认java和数据库不能批量操作 只能一条语句一条语句的处理
        //获取连接时可以添加参数允许批处理 rewriteBatchedStatements=true
        Connection conn = DriverManager.getConnection("jdbc:mysql:///atguigu?rewriteBatchedStatements=true", "root", "1234");

        /*
        * 注意:
        * 1.必须在连接数据库时 添加参数允许批处理 rewriteBatchedStatements=true
        * 2.新增数据必须使用values 且语句后不能追加;
        * 3.调用addBatch()方法 将sql语句进行批量添加操作
        * 4.统一执行executeBatch()方法
        * */
        String sql = "insert into t_emp(emp_name,emp_salary,emp_age) values(?,?,?)";
        //告知preparedStatement 返回新增数据的主键列的值
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        long start = System.currentTimeMillis();
        //java 和数据库交互多次效率低
        for(int i = 0; i < 10000; i++){
            preparedStatement.setString(1, "marry"+i);
            preparedStatement.setDouble(2,100.0+i);
            preparedStatement.setInt(3,20+i);
            preparedStatement.addBatch();
        }
        //统一执行executeBatch()方法
        preparedStatement.executeBatch();
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end - start)/1000);
        preparedStatement.close();
        conn.close();
    }
}
