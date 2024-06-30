package com.yoyodiningDB.senior.dao;

import com.yoyodiningDB.senior.pojo.Employee;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/*
* EmployeeDao类对应t_emp这张表的增删改查的操作
* 接口:便于实现不同数据库
*
* */
public interface EmployeeDao {
    /*
    * 查询对应数据库所有的操作
    * */
    List<Employee> selectAll() throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
    /*
    *根据empId查询单个员工
    * */
    Employee selectByEmpId(Integer empId);

    int insert(Employee employee);

    int update(Employee employee);

    int delete(Integer empId);
}
