package com.atguigu2.dao;

import com.atguigu.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

//用于定义针对于customer表的常用操作
public interface CustomerDAO {
//    将customer对象添加到数据库中
    void insert(Connection connection, Customer customer);

    void deleteById(Connection connection, int id);

    void updateById(Connection connection, Customer customer);

    Customer getCustomerById(Connection connection, int id);

    List<Customer> getAll(Connection connection);

//    返回数据表中的全部数
    Long getCount(Connection connection);

    //    返回数据表中的最大生日
    Date getMaxBirth(Connection connection);
}
