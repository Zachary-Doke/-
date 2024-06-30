package com.yoyodiningDB.senior.dao;

/**
 * @Title: BankDao
 * @Author Tanght 363993584@qq.com
 * @Date 2024/6/17 22:50
 * @description:
 */
public interface BankDao {
    int addMoney(Integer id, Integer money);
    int subMoney(Integer id, Integer money);
}
