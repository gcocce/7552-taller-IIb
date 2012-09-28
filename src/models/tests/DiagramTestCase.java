package models.tests;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import junit.framework.Assert;
import models.Diagram;
import models.EntityCollection;
import models.HierarchyCollection;
import models.Relationship;
import models.RelationshipCollection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DiagramTestCase {
	
	@Test
	public void testCreatingDiagramProvidesContentsIsNone()
	{
		Diagram diagram = new Diagram();
		Assert.assertEquals(0, diagram.getEntities().count());
		Assert.assertEquals(0, diagram.getRelationships().count());
		Assert.assertEquals(0, diagram.getHierarchies().count());
		Assert.assertEquals(0, diagram.getSubDiagrams().size());
	}
	
	@Test
	public void testCreateAnCompleteDiagram()
	{
		Diagram diagram = new Diagram(new EntityCollection(), 
				new RelationshipCollection(), new HierarchyCollection(), 
				new ArrayList<Diagram>());
		
		Assert.assertEquals(0, diagram.getEntities().count());
		Assert.assertEquals(0, diagram.getRelationships().count());			
		Assert.assertEquals(0, diagram.getHierarchies().count()); 			
		Assert.assertEquals(0, diagram.getEntities().count());
		Assert.assertNotNull(diagram.getSubDiagrams());
		Assert.assertEquals(0, diagram.getSubDiagrams().size());		
	}
	
	@Test
	public void testTwoDiagramProvidesDifferentsIdNumbers()
	{
		Diagram diagram = new Diagram();
		Diagram diagram1 = new Diagram();
		Assert.assertTrue(diagram.getId().toString() != diagram1.getId().toString());
	}
	
	@Test
	public void testAddingAnRelationshipCanBeRetrived() throws Exception
	{
		Diagram diagram = new Diagram();
		
		UUID uid1 = UUID.randomUUID();
		diagram.getRelationships().add(new Relationship(uid1, "TestName", false));
		Assert.assertEquals(1, diagram.getRelationships().count());
		Assert.assertEquals("TestName", diagram.getRelationship(uid1).getName());
	}
	
	@Test
	public void testAddingTwoRelationshipsCanBeRetrived() throws Exception
	{
		Diagram diagram = new Diagram();
		
		UUID uid1 = UUID.randomUUID();
		UUID uid2 = UUID.randomUUID();
		diagram.getRelationships().add(new Relationship(uid1, "TestName", false));
		diagram.getRelationships().add(new Relationship(uid2, "TestName1", false));
		Assert.assertEquals(2, diagram.getRelationships().count());
		Assert.assertEquals("TestName", diagram.getRelationship(uid1).getName());
		Assert.assertEquals("TestName1", diagram.getRelationship(uid2).getName());
	}
	
	@Test
	public void testDelettingOneRelationshipAndANonExistentRelationship() throws Exception
	{
		Diagram diagram = new Diagram();
		
		UUID id = UUID.randomUUID();
		diagram.getRelationships().add(new Relationship(id, "TestName", true));
		Assert.assertTrue(diagram.existsRelationship(id));
		
		try {
			diagram.removeRelationship(id);
		} catch (Exception e) {
			fail();
		}
		Assert.assertFalse(diagram.existsRelationship(id));
		
		id = UUID.randomUUID();
		try {
			diagram.removeRelationship(id);
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testAddingASubdiagramCanBeRetrived()
	{
		Diagram diagram = new Diagram();
		Diagram diagram2 = new Diagram();
		
		diagram.addSubDiagram(diagram2);
		Assert.assertTrue(diagram.existsSubDiagram(diagram2.getId()));
		Assert.assertEquals(diagram2.getId().toString(), diagram.getSubDiagram
				(diagram2.getId()).getId().toString());
	}
	
	@Test
	public void testAddingTwoSubDiagrams()
	{
		Diagram diagram = new Diagram();
		Diagram diagram2 = new Diagram();
		Diagram diagram3 = new Diagram();
		
		diagram.addSubDiagram(diagram2);
		diagram.addSubDiagram(diagram3);
		Assert.assertEquals(diagram2.getId().toString(), diagram.getSubDiagram
				(diagram2.getId()).getId().toString());
		Assert.assertEquals(diagram3.getId().toString(), diagram.getSubDiagram
				(diagram3.getId()).getId().toString());
		Assert.assertTrue(diagram2.getId().toString() != diagram3.getId().toString());
	}
	
	@Test
	public void testRemovingExistentAndNonExistentSubDiagrams()
	{
		Diagram diagram = new Diagram();
		UUID id = UUID.randomUUID();
		try 
		{
			diagram.removeSubDiagram(id);
		} catch (Exception e) 
		{
		}
				
		Diagram diagram2 = new Diagram();
		diagram.addSubDiagram(diagram2);
		try 
		{
			diagram.removeSubDiagram(diagram2.getId());
		} catch (Exception e) 
		{
			fail();
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

}
