package controllers.tests;


import junit.framework.Assert;

import models.Entity;
import models.EntityCollection;
import models.Hierarchy;

import org.junit.Before;
import org.junit.Test;

import controllers.HierarchyController;
import controllers.tests.mocks.MockHierarchyEventListener;
import controllers.tests.mocks.MockHierarchyView;
import controllers.tests.mocks.MockProjectContext;

public class HierarchyControllerTestCase {

	private MockHierarchyEventListener hierarchyCreatedListener;
	private MockProjectContext projectContext;
	private MockHierarchyView hierarchyView;
	private HierarchyController hierarchyController;
	
	@Before
	public void setUp() throws Exception {
		this.hierarchyCreatedListener = new MockHierarchyEventListener();
		this.projectContext = new MockProjectContext();
		this.hierarchyView = new MockHierarchyView();
		this.hierarchyController = new HierarchyController(this.projectContext, this.hierarchyView);
	}
	
	@Test
	public void testShouldSetControllerToView()
	{
		Assert.assertSame(this.hierarchyController, this.hierarchyView.getController());
	}
	
	@Test
	public void testShouldAddNullGeneralEntity()
	{
		Assert.assertTrue(this.hierarchyController.setGeneralEntity(null));
		Assert.assertFalse(this.hierarchyController.isGeneralEntity(new Entity("")));
		Assert.assertFalse(this.hierarchyController.isGeneralEntity(null));
	}
	
	@Test
	public void testShouldCreateAnHierarchyWithTotalAndExclusiveEqualFalse()
	{
		this.hierarchyController.create();
		Assert.assertTrue(hierarchyView.isVisible());
		Hierarchy hierarchy = this.hierarchyController.getPendingHierarchy();
		Assert.assertTrue(hierarchy.isExclusive());
		Assert.assertTrue(hierarchy.isTotal());
	}
	
	@Test
	public void testShouldNotAddHierarchyWithoutSpecificEntities() throws Exception
	{
		Entity entity = new Entity("TestName1");
		EntityCollection entityCollection = new EntityCollection();
		entityCollection.add(entity);
		
		this.projectContext.setEntityCollection(entityCollection);
			
		this.hierarchyController = new HierarchyController(this.projectContext, this.hierarchyView);
		this.hierarchyController.addSuscriber(this.hierarchyCreatedListener);
		this.hierarchyController.create();
		
		Assert.assertFalse(this.hierarchyController.addHierarchy());
		Assert.assertFalse(this.hierarchyCreatedListener.called);
		
		Assert.assertTrue(this.hierarchyController.setGeneralEntity(entity));
		
		Assert.assertEquals(entity.getId().toString(), this.hierarchyController.getPendingHierarchy().getGeneralEntityId().toString());
		Assert.assertFalse(this.hierarchyController.addHierarchy());
		Assert.assertFalse(this.hierarchyController.addSpecificEntity(entity));	
	}
	
	@Test
	public void testShouldCanAddAndRemoveSpecificEntities() throws Exception
	{
		Entity entity = new Entity("TestName1");
		EntityCollection entityCollection = new EntityCollection();
		entityCollection.add(entity);
		
		this.hierarchyController = new HierarchyController(this.projectContext, this.hierarchyView);
		
		this.hierarchyController.addSpecificEntity(entity);
		Assert.assertFalse(this.hierarchyController.addSpecificEntity(entity));
		Hierarchy hierarchy = this.hierarchyController.getPendingHierarchy();
		Assert.assertEquals(1, hierarchy.count());
		Assert.assertTrue(hierarchy.hasChild(entity.getId()));
		
		Assert.assertTrue(this.hierarchyController.removeSpecificEntity(entity));
		Assert.assertEquals(0, this.hierarchyController.getPendingHierarchy().count());
		Assert.assertFalse(this.hierarchyController.removeSpecificEntity(entity));
		
		this.hierarchyController.addSpecificEntity(entity);
		Assert.assertEquals(1, this.hierarchyController.getPendingHierarchy().count());
	}
	
	@Test
	public void testShouldNotAddHerarchyWithoutGeneralEntity() throws Exception
	{
		Entity entity1 = new Entity("TestName1");
		Entity entity2 = new Entity("TestName2");
		EntityCollection entityCollection = new EntityCollection();
		entityCollection.add(entity1);
		entityCollection.add(entity2);
		
		this.projectContext.setEntityCollection(entityCollection);
			
		this.hierarchyController = new HierarchyController(this.projectContext, this.hierarchyView);
		this.hierarchyController.addSpecificEntity(entity1);
		this.hierarchyController.addSpecificEntity(entity2);
		
		Assert.assertFalse(this.hierarchyController.setGeneralEntity(entity1));
		Assert.assertFalse(this.hierarchyController.addHierarchy());
	}
	

	@Test
	public void testShouldAddAnCompletedHierarchy() throws Exception
	{
		Entity entity1 = new Entity("TestName1");
		Entity entity2 = new Entity("TestName2");
		Entity entity3 = new Entity("TestName3");
		EntityCollection entityCollection = new EntityCollection();
		entityCollection.add(entity1);
		entityCollection.add(entity2);
		entityCollection.add(entity3);
		
		this.projectContext.setEntityCollection(entityCollection);
		this.hierarchyController = new HierarchyController(this.projectContext, this.hierarchyView);
		this.hierarchyController.addSuscriber(this.hierarchyCreatedListener);
		this.hierarchyController.create();
		
		this.hierarchyController.setGeneralEntity(entity1);
		this.hierarchyController.addSpecificEntity(entity2);
		this.hierarchyController.addSpecificEntity(entity3);
		this.hierarchyController.setTotal(true);
		this.hierarchyController.setExclusive(true);
		
		Assert.assertTrue(this.hierarchyController.addHierarchy());
		Assert.assertTrue(this.hierarchyCreatedListener.called);
		Hierarchy hierarchy = this.hierarchyCreatedListener.getHierarchy();
		
		Assert.assertTrue(hierarchy.isTotal());
		Assert.assertTrue(hierarchy.isExclusive());
		Assert.assertEquals(entity1.getId().toString(), hierarchy.getGeneralEntityId().toString());
		Assert.assertTrue(hierarchy.hasChild(entity2.getId()));
		Assert.assertTrue(hierarchy.hasChild(entity3.getId()));
	}
}
