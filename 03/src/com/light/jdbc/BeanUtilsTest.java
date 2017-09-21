package com.light.jdbc;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

public class BeanUtilsTest {

	@Test
	public void testgetProperty() throws Exception {
		Object object = new Student();
		System.out.println(object);
		BeanUtils.setProperty(object, "idCard", "11144458961265454");
		System.out.println(object);
		Object value = BeanUtils.getProperty(object, "idCard");
		System.out.println(value);
	}

	@Test
	public void testsetProperty() throws Exception {
		Object object = new Student();
		System.out.println(object);
		BeanUtils.setProperty(object, "idCard", "11144458961265454");
		System.out.println(object);
	}
}
