package com.light.jdbc;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class JDBCTest {

	/**
	 * 插入BLOB类型的数据必须使用PreparedStatement，因为Blob类型的数据无法使用字符拼写。
	 */
	@Test
	public void testInsertBlob() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JDBCTools.getConnection();
		
			String sql = "insert into customers(name, email, birth, picture) values (?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, "lkjkjhjkhgjk");
			preparedStatement.setString(2, "lkkjhkjhkhj@qq.com");
			preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
			
//			InputStream inputStream = new FileInputStream("workbin_large.jpg");
//			preparedStatement.setBlob(4, inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, preparedStatement, connection);
		}

	}

	/**
	 * 取得数据库自动生成的主键
	 */
	@Test
	public void testGetKeyValue() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JDBCTools.getConnection();
			String sql = "insert into customers(name, email, birth) values (?, ?, ?)";
			// preparedStatement = connection.prepareStatement(sql);

			// 使用重载的PreparedStatement(sql, flag)来生成PreparedStatement对象
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, "ABCDE");
			preparedStatement.setString(2, "DASDSADSA@qq.com");

			preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));

			preparedStatement.executeUpdate();

			// 通过.getGeneratedKeys()获取包含了新生成的主键的Resultset对象
			// 在Resultset中只有一列GENERATED_KEY，用于存放新生成的主键值
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				System.out.println(resultSet.getObject(1));
			}

			ResultSetMetaData data = resultSet.getMetaData();
			for (int i = 0; i < data.getColumnCount(); i++) {
				System.out.println(data.getColumnName(i + 1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, preparedStatement, connection);
		}
	}

}