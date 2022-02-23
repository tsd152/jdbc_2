package com.atguigu1.transaction;

import com.atguigu.bean.User;
import com.atguigu.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

public class ConnectionTest {
    @Test
    public void testGetConnection() throws SQLException, IOException, ClassNotFoundException {
        Connection connection = JDBCUtils.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testBaseUpdate(){
        String sql = "update user_table set balance = balance - 100 where user =?";
        String sql1 = "update user_table set balance = balance + 100 where user =?";
        update(sql, "AA");
        System.out.println(10/0);
        update(sql1, "BB");
        System.out.println("转账成功");
    }

    @Test
    public void testTxUpdate() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            connection.setAutoCommit(false);
            String sql = "update user_table set balance = balance - 100 where user =?";
            String sql1 = "update user_table set balance = balance + 100 where user =?";
            update(connection, sql, "AA");
            System.out.println(10/0);
            update(connection, sql1, "BB");
            connection.commit();
            System.out.println("转账成功");
            connection.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }


    }

    //    增删改的通用操作 version1.0,未考虑事务
    public int update(String sql, Object ...args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0;i < args.length;i++){
                preparedStatement.setObject(i+1, args[i]);
            }

            return preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, preparedStatement);
        }
        return 0;
    }

    //    增删改的通用操作 version2.0,考虑事务
    public int update(Connection connection,String sql, Object ...args){

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0;i < args.length;i++){
                preparedStatement.setObject(i+1, args[i]);
            }

            return preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, preparedStatement);
        }
        return 0;
    }

    //    查询的通用操作 version1.0，未考虑事务
    public <T> T getInstance(Class<T> clazz, String sql, Object ...args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();

            preparedStatement = connection.prepareStatement(sql);

            for(int i=0;i< args.length; i++){
                preparedStatement.setObject(i+1, args[i]);
            }

            resultSet = preparedStatement.executeQuery();
//        获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
//        通过metaData获取结果集的列数
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()){
                T t = clazz.newInstance();
                for (int i = 0; i< columnCount; i++){
                    Object object = resultSet.getObject(i + 1);
                    //                首先使用空参数的构造器将对象造出来，然后根据结果集中的列名查出什么设置什么
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //                给对象指定的属性，赋值为object，通过反射
                    //                调用运行时类的指定属性
                    Field declaredField = clazz.getDeclaredField(columnLabel);
                    declaredField.setAccessible(true);
                    declaredField.set(t, object);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);

        }
        return null;
    }

    //    查询的通用操作 version2.0，考虑事务
    public <T> T getInstance(Connection connection,Class<T> clazz, String sql, Object ...args){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            preparedStatement = connection.prepareStatement(sql);

            for(int i=0;i< args.length; i++){
                preparedStatement.setObject(i+1, args[i]);
            }

            resultSet = preparedStatement.executeQuery();
//        获取结果集的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
//        通过metaData获取结果集的列数
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()){
                T t = clazz.newInstance();
                for (int i = 0; i< columnCount; i++){
                    Object object = resultSet.getObject(i + 1);
                    //                首先使用空参数的构造器将对象造出来，然后根据结果集中的列名查出什么设置什么
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //                给对象指定的属性，赋值为object，通过反射
                    //                调用运行时类的指定属性
                    Field declaredField = clazz.getDeclaredField(columnLabel);
                    declaredField.setAccessible(true);
                    declaredField.set(t, object);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, preparedStatement, resultSet);

        }
        return null;
    }

    @Test
    public void testTXSelect() throws SQLException, IOException, ClassNotFoundException {
        Connection connection = JDBCUtils.getConnection();
//        不管其他事务怎么样，我这里避免脏读
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        System.out.println(connection.getTransactionIsolation());
        connection.setAutoCommit(false);
        String sql = "select * from user_table where user = ?";
        User cc = getInstance(connection, User.class, sql, "CC");
        System.out.println(cc);
    }

    @Test
    public void testTXUpdate(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            connection.setAutoCommit(false);
            String sql = "update user_table set balance = ? where user =?";
            update(connection, sql, 5000, "CC");
            Thread.sleep(15000);
            System.out.println("修改结束");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }

}
