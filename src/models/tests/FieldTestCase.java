package models.tests;


import junit.framework.Assert;
import models.Attribute;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FieldTestCase 
{
	@Test
	public void testShouldSetNameWhenCreating()
	{
		Attribute attribute = new Attribute("FieldName");
		
		Assert.assertEquals("FieldName", attribute.getName());
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

}
