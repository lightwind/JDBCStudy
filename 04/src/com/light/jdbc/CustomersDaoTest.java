package com.light.jdbc;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Test;

public class CustomersDaoTest {

	CustomerDao customerDao = new CustomerDao();

	@Test
	public void testBath() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetForValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetForList() {
		fail("Not yet implemented");
	}

	@Test
	public void testGet() {
		Connection connection = null;

		try {
			connection = JDBCTools.getConnection();
			String sql = "select id, name customerName, email, birth from customers where id = ?";
			Customers customers = customerDao.get(connection, sql, 5);
			System.out.println(customers);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

}
