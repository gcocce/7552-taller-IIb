package models.tests;

import models.Entity;
import models.EntityType;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EntityTestCase 
{
	@Test
	public void testCreatingEntityProvidesNameAndTypeIsNone()
	{
		Entity entity = new Entity("EntityName");
		Assert.assertEquals("EntityName", entity.getName());
		Assert.assertEquals(EntityType.None, entity.getType());
		Assert.assertEquals(0, entity.getAttributes().count());
		
	}
	
	@Test
	public void testTwoDifferentEntitiesHaveTwoDifferentIds()
	{
		Entity entity = new Entity("EntityName");
		Entity entity2 = new Entity("EntityName");
		Assert.assertTrue(entity.getId().toString() != entity2.getId().toString());
	}
	
	@Before
	public void setUp() throws Exception 
	{
	}

	@After
	public void tearDown() throws Exception 
	{
	}
}