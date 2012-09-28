package models.tests;

import java.util.UUID;

import infrastructure.Func;
import infrastructure.IterableExtensions;
import models.Cardinality;
import models.Entity;
import models.Relationship;
import models.RelationshipEntity;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RelationshipTestCase 
{
	private Entity firstEntity;
	private Entity secondEntity;
	private Entity thirdEntity;

	@Test
	public void testCreatingRelationshipSetsId() throws Exception
	{
		RelationshipEntity relationshipEntity = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"");
		
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(this.secondEntity, new Cardinality(0, 1),"");
		
		Relationship relationship = new Relationship(relationshipEntity, relationshipEntity2);
		
		Assert.assertTrue(relationship.getId() instanceof UUID);
		Assert.assertEquals(0, relationship.getAttributes().count());
	}
	
	@Test
	public void testCanCreateRelationshipWithDifferentEntities() throws Exception
	{
		RelationshipEntity relationshipEntity = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"");
		
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(this.secondEntity, new Cardinality(0, 1),"");
		
		Relationship relationship = new Relationship(relationshipEntity, relationshipEntity2);
		
		Func<RelationshipEntity, UUID, Boolean> cmpFunc = new Func<RelationshipEntity, UUID, Boolean>() 
		{
			@Override
			public Boolean execute(RelationshipEntity relEntity,
					UUID id) {
				return relEntity.getEntityId() == id;
			}
		};
		
		Iterable<RelationshipEntity> relationshipEntities = relationship.getRelationshipEntities();
		Assert.assertEquals(2, IterableExtensions.count(relationshipEntities));
		Assert.assertSame(relationshipEntity, IterableExtensions.firstOrDefault(relationshipEntities,
				cmpFunc, this.firstEntity.getId()));
		
		Assert.assertSame(relationshipEntity2, IterableExtensions.firstOrDefault(relationshipEntities,
				cmpFunc, this.secondEntity.getId()));
	}
	
	@Test
	public void testCanCreateRelationshipWithSameEntitiesAndDifferentRoles() throws Exception
	{
		RelationshipEntity relationshipEntity = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"role1");
		
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(this.secondEntity, new Cardinality(0, 1),"role2");
		
		Relationship relationship = new Relationship(relationshipEntity, relationshipEntity2);
		
		Func<RelationshipEntity, UUID, Boolean> cmpFunc = new Func<RelationshipEntity, UUID, Boolean>() 
		{
			@Override
			public Boolean execute(RelationshipEntity relEntity,
					UUID id) {
				return relEntity.getEntityId() == id;
			}
		};
		
		Iterable<RelationshipEntity> relationshipEntities = relationship.getRelationshipEntities();
		Assert.assertEquals(2, IterableExtensions.count(relationshipEntities));
		Assert.assertSame(relationshipEntity, IterableExtensions.firstOrDefault(relationshipEntities,
				cmpFunc, this.firstEntity.getId()));
		
		Assert.assertSame(relationshipEntity2, IterableExtensions.firstOrDefault(relationshipEntities,
				cmpFunc, this.secondEntity.getId()));
	}
	
	@Test(expected=Exception.class)
	public void testCreatingRelationshipWithSameEntityTwiceWithoutRoleThrows() throws Exception
	{
		RelationshipEntity relationshipEntity = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"");
		
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"role");
		
		new Relationship(relationshipEntity, relationshipEntity2);
	}
	
	@Test(expected=Exception.class)
	public void testCreatingRelationshipWithSameEntityTwiceWithSameRoleThrows() throws Exception
	{
		RelationshipEntity relationshipEntity = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"role");
		
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"role");
		
		new Relationship(relationshipEntity, relationshipEntity2);
	}
	
	@Test
	public void testCanAddRelationshipEntityWithRelatedToDifferentEntity() throws Exception
	{
		RelationshipEntity relationshipEntity = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"");
		
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(this.secondEntity, new Cardinality(0, 1),"");
		
		RelationshipEntity relationshipEntity3 = 
			new RelationshipEntity(this.thirdEntity, new Cardinality(0, 1),"");
		
		Relationship relationship = new Relationship(relationshipEntity, relationshipEntity2);
		
		Func<RelationshipEntity, UUID, Boolean> cmpFunc = new Func<RelationshipEntity, UUID, Boolean>() 
		{
			@Override
			public Boolean execute(RelationshipEntity relEntity,
					UUID id) {
				return relEntity.getEntityId() == id;
			}
		};
		
		relationship.addRelationshipEntity(relationshipEntity3);
		
		Iterable<RelationshipEntity> relationshipEntities = relationship.getRelationshipEntities();
		Assert.assertEquals(3, IterableExtensions.count(relationshipEntities));
		Assert.assertSame(relationshipEntity, IterableExtensions.firstOrDefault(relationshipEntities,
				cmpFunc, this.firstEntity.getId()));
		
		Assert.assertSame(relationshipEntity2, IterableExtensions.firstOrDefault(relationshipEntities,
				cmpFunc, this.secondEntity.getId()));
		
		Assert.assertSame(relationshipEntity3, IterableExtensions.firstOrDefault(relationshipEntities,
				cmpFunc, this.thirdEntity.getId()));
	}
	
	@Test
	public void testCanNotAddRelationshipWithSameEntityWithoutRoleInFirstOne() throws Exception
	{
		RelationshipEntity relationshipEntity = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"");
		
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(this.secondEntity, new Cardinality(0, 1),"");
		
		RelationshipEntity relationshipEntity3 = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"role");
		
		Relationship relationship = new Relationship(relationshipEntity, relationshipEntity2);
		Assert.assertFalse(relationship.addRelationshipEntity(relationshipEntity3));
	}
	
	@Test
	public void testCanNotAddRelationshipWithSameEntityWithoutRoleInSecondOne() throws Exception
	{
		RelationshipEntity relationshipEntity = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"role");
		
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(this.secondEntity, new Cardinality(0, 1),"");
		
		RelationshipEntity relationshipEntity3 = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"");
		
		Relationship relationship = new Relationship(relationshipEntity, relationshipEntity2);
		Assert.assertFalse(relationship.addRelationshipEntity(relationshipEntity3));
	}
	
	@Test
	public void testCanNotAddRelationshipWithSameEntityWithSameRole() throws Exception
	{
		RelationshipEntity relationshipEntity = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"role");
		
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(this.secondEntity, new Cardinality(0, 1),"");
		
		RelationshipEntity relationshipEntity3 = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"role");
		
		Relationship relationship = new Relationship(relationshipEntity, relationshipEntity2);
		Assert.assertFalse(relationship.addRelationshipEntity(relationshipEntity3));
	}
	
	@Test
	public void testCanAddRelationshipWithSameEntityWithoutRoleInSecondOne() throws Exception
	{
		RelationshipEntity relationshipEntity = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"role");
		
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(this.secondEntity, new Cardinality(0, 1),"");
		
		RelationshipEntity relationshipEntity3 = 
			new RelationshipEntity(this.firstEntity, new Cardinality(0, 1),"role2");
		
		Relationship relationship = new Relationship(relationshipEntity, relationshipEntity2);
		Assert.assertTrue(relationship.addRelationshipEntity(relationshipEntity3));
		Assert.assertEquals(3, IterableExtensions.count(relationship.getRelationshipEntities()));
	}
	
	@Before
	public void setUp() throws Exception 
	{
		this.firstEntity = new Entity("RelatedEntity1");
		this.secondEntity = new Entity("RelatedEntity2");
		this.thirdEntity = new Entity("RelatedEntity3");
	}

	@After
	public void tearDown() throws Exception {
	}
}