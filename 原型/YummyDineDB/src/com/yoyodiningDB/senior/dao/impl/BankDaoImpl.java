package com.yoyodiningDB.senior.dao.impl;

import com.yoyodiningDB.senior.dao.BankDao;
import com.yoyodiningDB.senior.dao.BaseDAO;

import java.sql.SQLException;

/**
 * @Title: BankDaoImpl
 * @Author Tanght 363993584@qq.com
 * @Date 2024/6/17 22:51
 * @description:
 */
public class BankDaoImpl extends BaseDAO implements BankDao {

    @Override
    public int addMoney(Integer id, Integer money) {
        String sql = "update t_bank set money = money + ? where id = ?";
        try {
            return executeUpdate(sql,money,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int subMoney(Integer id, Integer money) {
        String sql = "update t_bank set money = money - ? where id = ?";
        try {
            return executeUpdate(sql,money,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
