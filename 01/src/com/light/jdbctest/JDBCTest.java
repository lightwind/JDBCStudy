package com.light.jdbctest;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

public class JDBCTest {

	@Test
	public void testResultset() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = JDBCTools.getConnection();
			statement = connection.createStatement();
			String sql = "select * from customers";

			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString("name");
				String email = resultSet.getString(3);
				Date birth = resultSet.getDate(4);

				System.out.println(id);
				System.out.println(name);
				System.out.println(email);
				System.out.println(birth);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 通用的更新方法：包括INSERT、UPDATE、DELETE
	 * 版本1
	 */
	public void upDate(String sql) {

		Connection connection = null;
		Statement statement = null;

		try {

			connection = getConnection2();
			statement = connection.createStatement();
			statement.executeUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			JDBCTools.release(statement, connection);
		}
	}

	/**
	 * 向指定的数据表中插入一条记录
	 */
	@Test
	public void testStatement() throws Exception {
		// 1、获取数据库连接
		Connection connection = getConnection2();

		// 2、准备插入的sql语句
		String sql = "INSERT INTO customers (name,email,birth) VALUES ('ZXC','zxc@123.com','1992-12-12')";

		// 3、执行插入
		// a、获取操作sql语句的Statement对象
		Statement statement = connection.createStatement();
		// b、调用Statement对象的executeUpdate(sql)执行sql语句，进行插入
		statement.executeUpdate(sql);

		// 4、关闭Statement
		statement.close();

		// 5、关闭连接
		connection.close();
	}

	@Test
	public void testGetConnection2() throws Exception {
		System.out.println(getConnection2());
	}

	public Connection getConnection2() throws Exception {
		// 1、准备连接数据库的参数
		// a、创建Properties对象
		Properties properties = new Properties();
		// b、获取jdbc.properties对应的输入流
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		// c、加载b对应的输入流
		properties.load(in);
		// d、具体确定user等4个参数
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		String jdbcUrl = properties.getProperty("jdbcUrl");
		String driverClass = properties.getProperty("driver");

		// 2、加载驱动
		Class.forName(driverClass);

		// 3、通过DriverManager获取数据库连接
		return DriverManager.getConnection(jdbcUrl, user, password);

	}

	/**
	 * DriverManager是驱动的管理类
	 * 1、可以通过重载的getConnection()方法获取数据库连接
	 * 2、可以同时管理多个驱动程序：若注册多个数据库连接，则调用getConnection()时，传入不同的参数即可。
	 */
	@Test
	public void testDriverManager() throws Exception {
		// 1、准备连接数据库的参数
		// 驱动的全类名
		String driverClass = null;
		// JDBC URL
		String jdbcurl = null;
		String user = null;
		String password = null;

		// 读取类路径下的jdbc.properties文件
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(in);
		driverClass = properties.getProperty("driver");
		jdbcurl = properties.getProperty("jdbcUrl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");

		// 2、加载数据库驱动程序（对应的Driver实现类中有注册驱动的静态代码块）
		Class.forName(driverClass);

		// 3、通过DriverManager的getConnection()方法获取数据库连接
		Connection connection = DriverManager.getConnection(jdbcurl, user, password);

		System.out.println(connection);
	}

	/**
	 * Driver是一个接口：数据库厂商必须提供实现的接口，能从其中获取数据库链接。
	 * 可以通过Driver的实现类对象获取数据库连接。
	 */
	@Test
	public void testDriver() throws SQLException {
		// 1、创建Driver实现类的对象
		Driver driver = new com.mysql.jdbc.Driver();

		// 2、准备数据库连接的基本信息
		String url = "jdbc:mysql://127.0.0.1:3306/test";
		Properties info = new Properties();
		info.put("user", "light");
		info.put("password", "12345");

		// 3、调用Driver接口的connect(url,info)获取数据库连接
		Connection connection = driver.connect(url, info);
		System.out.println(connection);
	}

	/**
	 * 编写一个通用的方法，在不修改源程序的情况下，可以获取任何数据库的连接
	 * 解决方法：把数据库驱动Driver实现类的全类名、url、user、password放入一个配置文件中，通过修改配置文件的方式，
	 * 实现和具体的数据库解耦
	 */
	public Connection getConnection() throws Exception {
		String driverClass = null;
		String jdbcurl = null;
		String user = null;
		String password = null;

		// 读取类路径下的jdbc.properties文件
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(in);
		driverClass = properties.getProperty("driver");
		jdbcurl = properties.getProperty("jdbcUrl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");

		// 通过反射创建Driver对象
		Driver driver = (Driver) Class.forName(driverClass).newInstance();

		Properties info = new Properties();
		info.put("user", user);
		info.put("password", password);
		Connection connection = driver.connect(jdbcurl, info);

		return connection;
	}

	@Test
	public void testGetConnection() throws Exception {
		System.out.println(getConnection());
	}
}