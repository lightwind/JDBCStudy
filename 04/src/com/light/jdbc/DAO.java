package com.light.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 访问数据的DAO接口
 * 里面定义好访问数据表的各种方法
 * @param T:DAO处理的实体类的类型
 */
public interface DAO<T> {

	/**
	 * 批量处理的方法
	 * @param connection
	 * @param sql
	 * @param args:填充占位符的Object[]类型的可变参数。
	 */
	void bath(Connection connection, String sql, Object[]... args);

	/**
	 * 返回一个具体的值：总人数，平均工资，某个人的信息
	 */
	<E> E getForValue(Connection connection, String sql, Object... args);

	/**
	 * 返回T的一个集合
	 */
	List<T> getForList(Connection connection, String sql, Object... args);

	/**
	 * 返回一个T的对象
	 * @throws SQLException 
	 */
	T get(Connection connection, String sql, Object... args) throws SQLException;

	/**
	 * insert、update、delete
	 * @param connection	数据库连接
	 * @param sql	SQL语句
	 * @param args	填充占位符的可变参数
	 * */
	void update(Connection connection, String sql, Object... args);

}
