package controllers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import infrastructure.IProjectContext;

import java.util.Iterator;
import java.util.UUID;

import models.Cardinality;
import models.RelationshipEntity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import views.mock.MockRelationshipEntityView;
import controllers.IRelationshipEntityController;
import controllers.RelationshipEntityController;
import controllers.tests.mocks.MockProjectContext;

public class RelationshipEntityTest {

	private IProjectContext projectContext;
	private IRelationshipEntityController controller;
	private MockRelationshipEntityView mockRelationshipEntityView;

	@Before
	public void setUp() {
		projectContext = new MockProjectContext();
		mockRelationshipEntityView = new MockRelationshipEntityView();
		controller = new RelationshipEntityController(projectContext
				, mockRelationshipEntityView);
	}

	@Test
	public void TestCreateRelationshipEntity() {
		controller.create();
		assertEquals(mockRelationshipEntityView.getController(), controller);
		assertTrue(0 == controller.getRelationshipEntities().size());
		assertTrue(mockRelationshipEntityView.visible == true);
		
	}

	@Test
	public void TestAddRelationshipEntity() {
		assertEquals(0, controller.getRelationshipEntities().size());
		try {
			String role = "ROLE1";
			Cardinality cardinality = new Cardinality(1, Double.MAX_VALUE);
			UUID entityId = UUID.randomUUID();

			controller.add(entityId, cardinality, role,false);
			assertEquals(1, controller.getRelationshipEntities().size());

			String role2 = "ROLE2";
			Cardinality cardinality2 = new Cardinality(3, 5);
			UUID entityId2 = UUID.randomUUID();

			controller.add(entityId2, cardinality2, role2,false);
			assertEquals(2, controller.getRelationshipEntities().size());

		} catch (Exception e) {
			fail();
		}
	}

	@Test(expected = Exception.class)
	public void TestRemoveRelationshipEntityWhenItDoesNotExist() throws Exception {
		try {
			String role = "ROLE1";
			Cardinality cardinality = new Cardinality(1, Double.MAX_VALUE);
			UUID entityId = UUID.randomUUID();
			controller.add(entityId, cardinality, role,false);

			String role2 = "ROLE2";
			Cardinality cardinality2 = new Cardinality(3, 5);
			UUID entityId2 = UUID.randomUUID();

			controller.add(entityId2, cardinality2, role2,false);
		} catch (Exception e) {
			fail();
		}

		// Test remove method
		controller.remove(UUID.randomUUID());
	}

	@Test
	public void TestRemoveRelationshipEntity() {
		assertEquals(0, controller.getRelationshipEntities().size());
		try {
			String role = "ROLE1";
			Cardinality cardinality = new Cardinality(1, Double.MAX_VALUE);
			UUID entityId = UUID.randomUUID();

			controller.add(entityId, cardinality, role,false);
			assertEquals(1, controller.getRelationshipEntities().size());

			String role2 = "ROLE2";
			Cardinality cardinality2 = new Cardinality(3, 5);
			UUID entityId2 = UUID.randomUUID();

			controller.add(entityId2, cardinality2, role2,false);
			assertEquals(2, controller.getRelationshipEntities().size());

			// Test remove method
			controller.remove(entityId);
			assertEquals(1, controller.getRelationshipEntities().size());
			Iterator<RelationshipEntity> ite = controller
					.getRelationshipEntities().iterator();
			assertEquals(ite.next().getEntityId(), entityId2);

		} catch (Exception e) {
			fail();
		}
	}

	@Test(expected = Exception.class)
	public void TestModifyRelationshipEntityWhenItDoesNotExist() throws Exception {
		String role3 = "ROLE-MODIFIED";
		Cardinality cardinality3 = null;
		try {
			String role = "ROLE1";
			Cardinality cardinality = new Cardinality(1, Double.MAX_VALUE);
			UUID entityId = UUID.randomUUID();

			controller.add(entityId, cardinality, role,false);
			assertEquals(1, controller.getRelationshipEntities().size());
			
			cardinality3 = new Cardinality(5, 5);
		} catch (Exception e) {
			fail();
		}
		
		// Test remove method
		controller.modify(UUID.randomUUID(), cardinality3, role3,false);
	}

	@Test
	public void TestModifyRelationshipEntity() {
		assertEquals(0, controller.getRelationshipEntities().size());
		try {
			String role = "ROLE1";
			Cardinality cardinality = new Cardinality(1, Double.MAX_VALUE);
			UUID entityId = UUID.randomUUID();

			controller.add(entityId, cardinality, role,false);
			assertEquals(1, controller.getRelationshipEntities().size());

			// Test remove method
			String role3 = "ROLE-MODIFIED";
			Cardinality cardinality3 = new Cardinality(5, 5);
			controller.modify(entityId, cardinality3, role3,true);

			Iterator<RelationshipEntity> ite = controller
					.getRelationshipEntities().iterator();
			RelationshipEntity rel = ite.next();
			assertEquals(rel.getEntityId(), entityId);
			assertEquals(rel.getRole(), role3);
			assertEquals(rel.getCardinality(), cardinality3);
			assertTrue(rel.isStrongEntity());

		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	@Ignore
	public void TestReturnAllEntitiesAreSame () {
		fail ();
	}
}
