package com.atguigu2.dao;

import com.atguigu.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//声明为abstract，说明不会构建具体的类，只是使用其中的方法
public abstract class BaseDao {
    //    增删改的通用操作 version2.0,考虑事务
    public int update(Connection connection, String sql, Object ...args){

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

    //    查询的通用操作 version2.0，考虑事务,返回多条记录组成的集合
    public <T> List<T> getForList(Connection connection,Class<T> clazz, String sql, Object ...args){
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
            ArrayList<T> ts = new ArrayList<>();
            while (resultSet.next()){
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
                ts.add(t);
            }
            return ts;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, preparedStatement, resultSet);

        }
        return null;
    }

    //    进行统计，获取数值，获取最大值啊巴拉巴拉
    public <E> E getValue(Connection connection, String sql, Object ...args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i<args.length;i++){
                preparedStatement.setObject(i+1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return (E) resultSet.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, preparedStatement, resultSet);
        }


        return null;
    }

}
