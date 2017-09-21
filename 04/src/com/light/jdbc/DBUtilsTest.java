package com.light.jdbc;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

/**
 * 测试DBUtils工具类
 * @author lightwind
 *
 */
public class DBUtilsTest {

	/**
	 * ScalarHandler把结果集转为一个数值返回，可以是任意基本数据类型返回
	 */
	@Test
	public void testScalarHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = null;

		try {
			connection = JDBCTools.getConnection();
			String sql = "select name from customers where id =?";
			String name = (String) queryRunner.query(connection, sql, new ScalarHandler(), 5);
			System.out.println(name);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}

	/**
	 * MapListHandler将结果集转化成一个Map的List
	 */
	@Test
	public void testMapListHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = null;

		try {
			connection = JDBCTools.getConnection();
			String sql = "select id, name, email, birth from customers";
			List<Map<String, Object>> mapList = queryRunner.query(connection, sql,
					new MapListHandler());
			System.out.println(mapList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}

	/**
	 * MapHandler返回SQL对应的第一条记录对应的Map对象
	 * 键：SQL查询的列名，不是列的别名
	 * 值：SQL查询的值
	 */
	@Test
	public void testMapHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = null;

		try {
			connection = JDBCTools.getConnection();
			String sql = "select id, name, email, birth from customers";
			Map<String, Object> map = queryRunner.query(connection, sql, new MapHandler());
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}

	/**
	 * BeanListHandler把结果集转为一个List，该List不为null，但可能为空集合（size()方法返回为0）
	 * 若SQL语句的确能够查询到记录，List中存放创建BeanListHandler传入的Class对象对应的对象。
	 */
	@Test
	public void testBeanListHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = null;

		try {
			connection = JDBCTools.getConnection();
			String sql = "select id, name, email, birth from customers";
			List<Customers> customers = (List<Customers>) queryRunner.query(connection, sql,
					new BeanListHandler(Customers.class));
			System.out.println(customers);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}

	/**
	 * BeanHandler把结果集的第一条记录转为创建BeanHandler对象时，传入的Class参数对应的对象
	 */
	@Test
	public void testBeanHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = null;

		try {
			connection = JDBCTools.getConnection();
			String sql = "select id, name, email, birth from customers where id =?";
			Customers customers = (Customers) queryRunner.query(connection, sql,
					new BeanHandler(Customers.class), 5);
			System.out.println(customers);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}

	class MyResultSetHandler implements ResultSetHandler {
		@Override
		public Object handle(ResultSet rs) throws SQLException {

			List<Customers> customersList = new ArrayList<>();

			while (rs.next()) {
				Integer id = rs.getInt(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				Date birth = rs.getDate(4);

				Customers customers = new Customers(id, name, email, birth);
				customersList.add(customers);
			}

			return customersList;
		}
	}

	/**
	 * QueryRunner的query()方法的返回值取决于其ResultSetHandler参数的handle方法的返回值
	 */
	@Test
	public void testQueryRunnerQuery() {
		// 1、创建QueryRunner的实现类
		QueryRunner queryRunner = new QueryRunner();

		Connection connection = null;

		try {
			connection = JDBCTools.getConnection();
			String sql = "select id, name, email, birth from customers";
			Object object = queryRunner.query(connection, sql, new MyResultSetHandler());

			System.out.println(object);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}

	/**
	 * 测试QueryRunner类的update方法
	 * 该方法适用于insert、update、delete
	 */
	@Test
	public void testQueryRunnerUpdate() {
		// 1、创建QueryRunner的实现类
		QueryRunner queryRunner = new QueryRunner();
		// 2、使用update方法
		String sql = "delete from customers where id in (?, ?)";

		Connection connection = null;

		try {
			connection = JDBCTools.getConnection();
			queryRunner.update(connection, sql, 12, 13);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}

}