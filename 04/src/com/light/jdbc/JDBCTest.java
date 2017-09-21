package com.light.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCTest {
	
	@Test
	public void testJDBCTools(){
		try {
			Connection connection = JDBCTools.getConnection();
			System.out.println(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 1、创建c3p0-config.xml配置文件
	 * 2、创建DataSource实例
	 * 		DataSource dataSource = new ComboPooledDataSource("helloc3p0");
	 * 3、从DataSource实例中获取数据库连接
	 */
	@Test
	public void testC3P0WithConfig() throws Exception {
		DataSource dataSource = new ComboPooledDataSource("helloc3p0");

		System.out.println(dataSource.getConnection());

		ComboPooledDataSource comboPooledDataSource = (ComboPooledDataSource) dataSource;

		System.out.println(comboPooledDataSource.getMaxStatements());
	}

	@Test
	public void testC3p0() throws Exception {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl("jdbc:mysql:///light");
		cpds.setUser("light");
		cpds.setPassword("12345");

		System.out.println(cpds.getConnection());
	}

	/**
	 * 1、加载properties配置文件：配置文件中的键需要来自BasicDataSource的属性。
	 * 2、调用BasicDataSourceFactory的createDataSource()方法创建DataSource实例
	 * 3、从DataSource实例中获取数据库连接
	 */
	@Test
	public void testDBCPWithDataSourceFactory() throws Exception {

		Properties properties = new Properties();

		InputStream in = JDBCTest.class.getClassLoader().getResourceAsStream("dbcp.properties");
		properties.load(in);

		DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);

		System.out.println(dataSource.getConnection());

		BasicDataSource basicDataSource = (BasicDataSource) dataSource;

		System.out.println(basicDataSource.getMaxWait());
	}

	/**
	 * 使用DBCP数据库连接池
	 * @throws SQLException 
	 */
	@Test
	public void testDBCP() throws SQLException {
		BasicDataSource dataSource = null;
		// 1、创建DBCP数据源实例
		dataSource = new BasicDataSource();

		// 2、为数据源实例指定必须的属性
		dataSource.setUsername("light");
		dataSource.setPassword("12345");
		dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/light");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");

		// 指定数据库连接池初始化连接数
		dataSource.setInitialSize(10);

		// 指定最大连接数：同一时刻可以同时向数据库申请的连接数量
		dataSource.setMaxActive(50);

		// 指定最小连接数：在数据库连接池中保存的最少的空闲连接数量
		dataSource.setMinIdle(5);

		// 等待数据库连接池分配连接的最长时间，超过该时间，抛出异常
		dataSource.setMaxWait(5000);

		// 3、从数据源中获取数据库连接
		Connection connection = dataSource.getConnection();

		System.out.println(connection);
	}

}
