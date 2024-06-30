package com.yoyodiningDB.senior;

import com.yoyodiningDB.senior.dao.BankDao;
import com.yoyodiningDB.senior.dao.EmployeeDao;
import com.yoyodiningDB.senior.dao.impl.BankDaoImpl;
import com.yoyodiningDB.senior.dao.impl.EmployeeDaoImpl;
import com.yoyodiningDB.senior.pojo.Employee;
import com.yoyodiningDB.senior.util.JDBCUtil;
import com.yoyodiningDB.senior.util.JDBCUtilV2;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.List;

/**
 * @Title: JDBCUtilTest
 * @Author Tanght 363993584@qq.com
 * @Date 2024/6/16 16:58
 * @description:
 */
public class JDBCUtilTest {
    @Test
    public void testJDBCUtil() throws SQLException {
        Connection connection = JDBCUtil.getConnection();
        System.out.println(connection);
        JDBCUtil.release(connection);
    }
    @Test
    public void testJDBCUtilV2() throws SQLException {
        /*Connection connection1 = JDBCUtil.getConnection();
        Connection connection2 = JDBCUtil.getConnection();
        Connection connection3 = JDBCUtil.getConnection();
        System.out.println(connection1);
        System.out.println(connection2);
        System.out.println(connection3);*/
        Connection connection1 = JDBCUtilV2.getConnection();
        Connection connection2 = JDBCUtilV2.getConnection();
        Connection connection3 = JDBCUtilV2.getConnection();
        System.out.println(connection1);
        System.out.println(connection2);
        System.out.println(connection3);
    }
    @Test
    public void testEmployeeDao() throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        //1.创建DAO实现类对象
        EmployeeDao employeeDao = new EmployeeDaoImpl();

        //2.调用查询所有方法
        //List<Employee> employeeList = employeeDao.selectAll();
        //for (Employee employee : employeeList) {
        //    System.out.println(employee);
        //}

        //2.调用单个方法
        //Employee  employee = employeeDao.selectByEmpId(1);
        //System.out.println(employee);

        //2.调用插入员工方法
        //Employee employee = new Employee(null, "Jerry", 6665.5, 20);
        //employeeDao.insert(employee);

        //2.调用修改员工方法
        //Employee employee = new Employee(40010,"Jerry",656.65,38);
        //System.out.println(employeeDao.update(employee));
        //int delete = employeeDao.delete(40010);
        //System.out.println(delete);
    }
    @Test
    public void testTransaction(){
        BankDao bankDao = new BankDaoImpl();
        Connection connection = null;
        try {
            //1.将事务的自动提交改为手动
            connection = JDBCUtilV2.getConnection();
            connection.setAutoCommit(false);

            //2.操作减钱
            bankDao.subMoney(1,100);

            int i = 10 / 0;

            //3.操作加钱
            bankDao.addMoney(2,100);

            //4.没有异常提交事务
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
