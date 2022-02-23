package com.atguigu2.dao;

import com.atguigu.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDAOImpl extends BaseDao implements CustomerDAO{
    @Override
    public void insert(Connection connection, Customer customer) {
        String sql = "insert into customers (name, email, birth) values(?, ?, ?)";
        update(connection, sql, customer.getName(), customer.getEmail(), customer.getBirth());
    }

    @Override
    public void deleteById(Connection connection, int id) {
        String sql = "delete from customers where id = ?";
        update(connection, sql, id);
    }

    @Override
    public void updateById(Connection connection, Customer customer) {
        String sql = "update customers set name = ?, email = ?, birth = ? where id = ?";
        update(connection, sql, customer.getName(), customer.getEmail(), customer.getBirth(), customer.getId());
    }

    @Override
    public Customer getCustomerById(Connection connection, int id) {
        String sql = "select id, name, email, birth from customers where id = ?";
        Customer instance = getInstance(connection, Customer.class, sql, id);
        return instance;

    }

    @Override
    public List<Customer> getAll(Connection connection) {
        String sql = "select id, name, email, birth from customers";
        List<Customer> forList = getForList(connection, Customer.class, sql);
        return forList;
    }

    @Override
    public Long getCount(Connection connection) {
        String sql = "select count(*) from customers";
        Long value = (Long) getValue(connection, sql);
        return value;
    }

    @Override
    public Date getMaxBirth(Connection connection) {
        String sql = "select max(birth) from customers";
        Date value = (Date) getValue(connection, sql);
        return value;
    }
}
