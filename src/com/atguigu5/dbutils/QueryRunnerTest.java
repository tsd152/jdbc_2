package com.atguigu5.dbutils;

import com.atguigu.bean.Customer;
import com.atguigu.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class QueryRunnerTest {
    @Test
    public void testInsert(){
        Connection connection = null;
        try {
            connection = com.atguigu4.util.JDBCUtils.getConnectionDruid();
            String sql = "insert into customers (name, email, birth) values(?, ?, ?)";
            QueryRunner queryRunner = new QueryRunner();
            int insertCount = queryRunner.update(connection, sql, "蔡徐坤", "caoxukun@qqq.com", "1992-03-23");
            System.out.println(insertCount);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void testQUuery(){
        Connection connection = null;
//        BeanHandler是resultHandler的具体的实现类，用于封装表中的一条记录
        try {
            connection = com.atguigu4.util.JDBCUtils.getConnectionDruid();
            String sql = "select id, name, email, birth from customers where id = ?";
            QueryRunner queryRunner = new QueryRunner();
            BeanHandler<Customer> customerBeanHandler = new BeanHandler<>(Customer.class);
            Customer query = queryRunner.query(connection, sql, customerBeanHandler, 23);
            System.out.println(query);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void testQUuery2(){
        Connection connection = null;
//        BeanHandler是resultHandler的具体的实现类，用于封装表中的一条记录
        try {
            connection = com.atguigu4.util.JDBCUtils.getConnectionDruid();
            String sql = "select id, name, email, birth from customers where id < ?";
            QueryRunner queryRunner = new QueryRunner();
            BeanListHandler<Customer> customerBeanHandler = new BeanListHandler<>(Customer.class);
            List<Customer> query = queryRunner.query(connection, sql, customerBeanHandler, 23);
            query.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void testQUuery3(){
        Connection connection = null;
//        BeanHandler是resultHandler的具体的实现类，用于封装表中的一条记录
        try {
            connection = com.atguigu4.util.JDBCUtils.getConnectionDruid();
            String sql = "select id, name, email, birth from customers where id = ?";
            QueryRunner queryRunner = new QueryRunner();
            MapHandler customerBeanHandler = new MapHandler();
            Map<String, Object> query = queryRunner.query(connection, sql, customerBeanHandler, 23);
            System.out.println(query);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
    public void testQUuery4(){
        Connection connection = null;
//        BeanHandler是resultHandler的具体的实现类，用于封装表中的一条记录
        try {
            connection = com.atguigu4.util.JDBCUtils.getConnectionDruid();
            String sql = "select id, name, email, birth from customers where id < ?";
            QueryRunner queryRunner = new QueryRunner();
            MapListHandler mapListHandler = new MapListHandler();

            List<Map<String, Object>> query = queryRunner.query(connection, sql, mapListHandler, 23);
            query.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
//    ScalarHandler用于查询特殊值
    public void testQuery5(){
        Connection connection = null;
//        BeanHandler是resultHandler的具体的实现类，用于封装表中的一条记录
        try {
            connection = com.atguigu4.util.JDBCUtils.getConnectionDruid();
            String sql = "select count(*) from customers";
            QueryRunner queryRunner = new QueryRunner();
            ScalarHandler scalarHandler = new ScalarHandler();
            Object query = queryRunner.query(connection, sql, scalarHandler);
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }

    @Test
//    自定义resultSetHandler
    public void testQUuery6(){
        Connection connection = null;
//        BeanHandler是resultHandler的具体的实现类，用于封装表中的一条记录
        try {
            connection = com.atguigu4.util.JDBCUtils.getConnectionDruid();
            String sql = "select id, name, email, birth from customers where id = ?";
            QueryRunner queryRunner = new QueryRunner();

            ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
                @Override
                public Customer handle(ResultSet resultSet) throws SQLException {
                    if(resultSet.next()){
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        Date birth = resultSet.getDate("birth");
                        Customer customer = new Customer(id, name, email, birth);
                        return customer;
                    }
                    return null;
                }
            };
            Customer query = queryRunner.query(connection, sql, handler, 23);
            System.out.println(query);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }
    }
}

