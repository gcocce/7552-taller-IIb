package controllers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import models.AttributeCollection;
import models.Entity;
import models.EntityCollection;
import models.EntityType;
import models.Relationship;
import models.RelationshipEntity;

import org.junit.Before;
import org.junit.Test;

import views.mock.MockRelationshipView;
import controllers.IRelationshipController;
import controllers.RelationshipController;
import controllers.factories.mock.MockRelationshipEntityControllerFactory;
import controllers.tests.mocks.MockAttributeController;
import controllers.tests.mocks.MockAttributeControllerFactory;
import controllers.tests.mocks.MockProjectContext;
import controllers.tests.mocks.MockRelationshipEntityController;


public class RelationshipControllerTest {

	private MockAttributeController attController;
	private IRelationshipController relController;
	private EntityCollection entCollection;
	private MockRelationshipView view;
	private List<Relationship> relationships;
	private MockProjectContext pContext;
	private MockAttributeControllerFactory mockAttributeControllerFactory;
	
	private MockRelationshipEntityControllerFactory mockRelationshipEntityControllerFactory;
	private MockRelationshipEntityController mockRelationshipEntityController;

	@Before
	public void setUp() throws Exception {
		entCollection = new EntityCollection();
		
		Entity entity1 = new Entity("Entity1");
		Entity entity2 = new Entity("Entity2");
		Entity entity3 = new Entity("Entity3");
		Entity entity4 = new Entity("Entity4");

		AttributeCollection attCol = entity1.getAttributes();
		attCol.addAttribute("Attribute1" );
		
		attCol = entity2.getAttributes();
		attCol.addAttribute("Attribute1" );
		attCol.addAttribute("Attribute2" );
		
		attCol = entity3.getAttributes();
		attCol.addAttribute("Attribute1" );
		attCol.addAttribute("Attribute2" );
		attCol.addAttribute("Attribute3" );
		
		attCol = entity4.getAttributes();
		attCol.addAttribute("Attribute1" );
		attCol.addAttribute("Attribute2" );
		attCol.addAttribute("Attribute3" );
		attCol.addAttribute("Attribute4" );
		
		entCollection.add(entity1);
		entCollection.add(entity2);
		entCollection.add(entity3);
		entCollection.add(entity4);
		relationships = new ArrayList<Relationship> ();
		

		
		// Set up the views
		
				
		// Controllers

		
		
		attController = new MockAttributeController();
		
		pContext = new MockProjectContext();
		pContext.setRelationshipCollection(relationships);
		mockRelationshipEntityController = new MockRelationshipEntityController(
				pContext);
		
		
		//Initialize parameters
		// Create the Factorories
		mockAttributeControllerFactory = new MockAttributeControllerFactory();
		
		mockRelationshipEntityControllerFactory = new MockRelationshipEntityControllerFactory();
	
		// Set up the factories
		mockAttributeControllerFactory	.setAttributeController(attController);
		
		mockRelationshipEntityControllerFactory.setRelationshipEntityController(mockRelationshipEntityController);
		
		view = new MockRelationshipView();
		
		relController = new RelationshipController(pContext,
				view,
				mockAttributeControllerFactory,
				mockRelationshipEntityControllerFactory);
		
		mockAttributeControllerFactory = new MockAttributeControllerFactory();
		mockAttributeControllerFactory.setAttributeController(attController);
	}



	@Test
	public void TestCreateRelationshipController() {
						
		
		//Test to create the controller
		relController.create();
		assertTrue(view.getController() == relController);
	}
	
	@Test
    public void TestAddRelationship()  {
		
		//There are no relationships at first in the project context
		assertTrue (this.pContext.getRelationshipCollection().size()==0);
		relController.create();
		relController.addCreateListener(pContext);
		assertTrue(0 == mockRelationshipEntityController.getRelationshipEntities().size());
		UUID uuid1 = UUID.randomUUID();
		UUID uuid2 = UUID.randomUUID();
		
		
		mockRelationshipEntityController.add(uuid1, null, null,false);
		assertTrue(1 == mockRelationshipEntityController.getRelationshipEntities().size());

		mockRelationshipEntityController.add(uuid2, null, null,false);
		assertTrue(2 == mockRelationshipEntityController.getRelationshipEntities().size());

		relController.setName("Relationship");
		try {
			relController.add();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		
		//The relationship should be added to the project context
		assertTrue (this.pContext.getRelationshipCollection().size()==1);
		Relationship aux =  pContext.getRelationshipCollection().iterator().next();
		
		assertEquals (aux.getName(),"Relationship");
		assertTrue (aux.isComposition()==false);
		assertTrue (IterableExtensions.count(aux.getRelationshipEntities()) == 2);
		Iterator <RelationshipEntity> ite = aux.getRelationshipEntities().iterator();
		RelationshipEntity relEnt1 = ite.next();
		RelationshipEntity relEnt2 = ite.next();
		assertTrue (relEnt1.getEntityId().equals(uuid1));
		assertTrue (relEnt2.getEntityId().equals(uuid2));
		
	}

		
	@Test
    public void TestAddRelationshipWithLessThanTwoRelationshipEntities() throws Exception {
		
		//There are no relationships at first in the project context
		assertTrue (this.pContext.getRelationshipCollection().size()==0);
		relController.create();
		relController.setName("Relationship");
		try {
			relController.add();
			fail();
		}catch (Exception e) {
			
		}
		
		try {
			mockRelationshipEntityController.add(UUID.randomUUID(), null, null,false);
			relController.add();
			fail();
		} catch (Exception e) {
			
		}
		
		mockRelationshipEntityController.add(UUID.randomUUID(), null, null,false);
		relController.add();
				
	}

	
	@Test
	public void TestValidateComposeEntitiesOfSameTypes() throws Exception {
			
		relController.create();
		
		Entity entity1 = new Entity ("Entity1");
		entity1.setType(EntityType.Domain);
		Entity entity2 = new Entity ("Entity2");
		entity2.setType(EntityType.Domain);
		Entity entity3 = new Entity ("Entity3");
		entity3.setType(EntityType.Domain);
		
		EntityCollection entCol = new EntityCollection ();
		entCol.add(entity1);
		entCol.add(entity2);
		entCol.add(entity3);
		mockRelationshipEntityController.setAreAllSame(true);
		pContext.setEntityCollection(entCol);
		mockRelationshipEntityController.add(entity1.getId(),null,null,false);
		mockRelationshipEntityController.add(entity2.getId(),null,null,false);
		mockRelationshipEntityController.add(entity3.getId(),null,null,false);
		
		
		relController.setName("Relationship");
		relController.isComposition(true);
		try {
			relController.add();
		}catch (Exception e) {
			e.printStackTrace();
			fail ();
		}
		
		assertTrue (relController.isComposition());
		
	}
	
	@Test (expected=Exception.class)
	public void TestValidateComposeEntitiesOfDiferentTypes() throws Exception {
					
		relController.create();
		
		Entity entity1 = new Entity ("Entity1");
		entity1.setType(EntityType.Domain);
		Entity entity2 = new Entity ("Entity2");
		entity2.setType(EntityType.Domain);
		Entity entity3 = new Entity ("Entity3");
		entity3.setType(EntityType.Historic);
		
		EntityCollection entCol = new EntityCollection ();
		entCol.add(entity1);
		entCol.add(entity2);
		entCol.add(entity3);
		
		pContext.setEntityCollection(entCol);
		mockRelationshipEntityController.add(entity1.getId(),null,null,false);
		mockRelationshipEntityController.add(entity2.getId(),null,null,false);
		mockRelationshipEntityController.add(entity3.getId(),null,null,false);
		mockRelationshipEntityController.setAreAllSame(false);
		
		relController.setName("Relationship");
		
		relController.isComposition(true);
		
	}
	
}
