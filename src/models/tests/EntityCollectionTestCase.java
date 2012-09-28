package models.tests;


import junit.framework.Assert;

import models.Entity;
import models.EntityCollection;
import models.EntityType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EntityCollectionTestCase {

	@Test
	public void testCreatingEntityCollectionGivesCountEqualToZero()
	{
		EntityCollection collection = new EntityCollection();
		
		Assert.assertEquals(0, collection.count());
	}
	
	@Test
	public void testAddingEntityIncreasesCountAndCreatesEntityWithName()
	{
		EntityCollection collection = new EntityCollection();
		
		Assert.assertEquals(0, collection.count());
		
		collection.add("TestName");
		Assert.assertEquals(1, collection.count());
		
		Entity entity = collection.get("TestName");
		Assert.assertEquals("TestName", entity.getName());
		Assert.assertEquals(EntityType.None, entity.getType());
	}
	
	@Test
	public void testAddingEntityWithTypeCreatesWithCorrectType()
	{
		EntityCollection collection = new EntityCollection();
		
		Assert.assertEquals(0, collection.count());
		
		collection.add("TestName", EntityType.Historic);
		Assert.assertEquals(1, collection.count());
		
		Entity entity = collection.get("TestName");
		Assert.assertEquals("TestName", entity.getName());
		Assert.assertEquals(EntityType.Historic, entity.getType());
	}
	
	@Test
	public void testAddingTwoEntitiesCanBeRetrieved()
	{
		EntityCollection collection = new EntityCollection();
		
		collection.add("TestName1", EntityType.Historic);
		collection.add("TestName2", EntityType.Programmed);
		Assert.assertEquals(2, collection.count());
		
		Entity entity1 = collection.get("TestName1");
		Entity entity2 = collection.get("TestName2");
		Assert.assertEquals("TestName1", entity1.getName());
		Assert.assertEquals("TestName2", entity2.getName());
		Assert.assertEquals(EntityType.Historic, entity1.getType());
		Assert.assertEquals(EntityType.Programmed, entity2.getType());
	}
	
	
	@Test
	public void testGettingNonExistingNameReturnsNull()
	{
		EntityCollection collection = new EntityCollection();
		
		collection.add("TestName1", EntityType.Historic);
		collection.add("TestName2", EntityType.Programmed);
		Assert.assertEquals(2, collection.count());
		
		Assert.assertNull(collection.get("TestName3"));
	}
	
	@Test
	public void testRemovingDecreasesCountAndCannotRetrieve()
	{
		EntityCollection collection = new EntityCollection();
		
		collection.add("TestName1", EntityType.Historic);
		collection.add("TestName2", EntityType.Programmed);
		Assert.assertEquals(2, collection.count());
		
		
		Assert.assertTrue(collection.remove("TestName1"));
		Assert.assertEquals(1, collection.count());
		Assert.assertNull(collection.get("TestName1"));
	}
	
	@Test
	public void testRemovingNonExistingEntityReturnsNull()
	{
		EntityCollection collection = new EntityCollection();
		
		Assert.assertFalse(collection.remove("TestName1"));
	}
	
	@Test
	public void testAddingEntityWithSameNameAsExistingOneReturnsFalse()
	{
		EntityCollection collection = new EntityCollection();
		
		Assert.assertEquals(0, collection.count());
		Assert.assertTrue(collection.add("TestName"));
		Assert.assertEquals(1, collection.count());
		Assert.assertFalse(collection.add("TestName"));
		Assert.assertEquals(1, collection.count());
		Assert.assertFalse(collection.add("TestName",EntityType.Historic));
		Assert.assertEquals(1, collection.count());
	}
	
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

}
