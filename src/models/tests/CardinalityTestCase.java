package models.tests;


import junit.framework.Assert;

import models.Cardinality;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CardinalityTestCase {

	@Test
	public void testShouldSetMinimumAndMaximum() throws Exception
	{
		Cardinality cardinality = new Cardinality(1, Double.POSITIVE_INFINITY);
		
		Assert.assertEquals(1.0, cardinality.getMinimum());
		Assert.assertEquals(Double.POSITIVE_INFINITY, cardinality.getMaximum());
	}
	
	@Test(expected=Exception.class)
	public void testMaximumShouldNotBeLowerThanMinimumWhenConstructing() throws Exception
	{
		new Cardinality(2, 1);
	}
	
	@Test(expected=Exception.class)
	public void testMaximumShouldNotBeLowerThanMinimumWhenSettingMaximum() throws Exception
	{
		Cardinality cardinality = new Cardinality(2, 2);
		cardinality.setMaximum(1.0);
	}
	
	@Test(expected=Exception.class)
	public void testMaximumShouldNotBeLowerThanMinimumWhenSettingMinimum() throws Exception
	{
		Cardinality cardinality = new Cardinality(2, 2);
		cardinality.setMinimum(3.0);
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

}
