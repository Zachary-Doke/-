package com.yoyodiningDB.senior.dao.impl;

import com.yoyodiningDB.senior.dao.BaseDAO;
import com.yoyodiningDB.senior.dao.EmployeeDao;
import com.yoyodiningDB.senior.pojo.Employee;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * @Title: EmployeeDaoImpl
 * @Author Tanght 363993584@qq.com
 * @Date 2024/6/16 21:59
 * @description:
 */
public class EmployeeDaoImpl extends BaseDAO implements EmployeeDao {

    @Override
    public List<Employee> selectAll() throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String sql = "Select emp_id empId, emp_name empName, emp_salary empSalary, emp_age  empAge from t_emp";
        return executeQuery(Employee.class, sql);
    }

    @Override
    public Employee selectByEmpId(Integer empId) {
        String sql = "Select emp_id empId, emp_name empName, emp_salary empSalary, emp_age  empAge from t_emp where emp_id = ?";
        Employee employee = new Employee();
        try {
            employee = excuteQueryBean(Employee.class,sql,1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return employee;
    }

    @Override
    public int insert(Employee employee) {
        String sql = "insert into t_emp(emp_name, emp_salary, emp_age) values(?,?,?)";
        try {
            return executeUpdate(sql,employee.getEmpName(),employee.getEmpSalary(),employee.getEmpAge());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Employee employee) {
        String sql = "update t_emp set emp_age = ? where emp_name = ?";
        try {
            return executeUpdate(sql,employee.getEmpAge(),employee.getEmpName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(Integer empId) {
        String sql = "delete from t_emp where emp_id = ?";
        try {
            return  executeUpdate(sql,empId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
