package infrastructure.tests;

import java.util.HashSet;
import java.util.UUID;

import junit.framework.Assert;

import models.Diagram;
import models.Entity;
import models.EntityCollection;
import models.Hierarchy;
import models.HierarchyCollection;

import org.junit.Before;
import org.junit.Test;

import infrastructure.ProjectContext;


public class ProjectContextTestCase {

	private ProjectContext projectContext;
	private Diagram diagram;
	private EntityCollection entities;
	private HierarchyCollection hierarchies;
	
	@Before
	public void setUp() {
		this.projectContext = new ProjectContext();
		this.diagram = new Diagram();
		this.entities = new EntityCollection();
		this.hierarchies = new HierarchyCollection();
	}
	

	@Test
	public void testShouldReturnZeroContent() {
		Assert.assertEquals(0, ((HashSet<Entity>) this.projectContext.getAllEntities()).size());
		Assert.assertEquals(0, ((HashSet<Hierarchy>) this.projectContext.getAllHierarchies()).size());
		Assert.assertEquals(0, ((HashSet<Entity>) this.projectContext.getFamilyEntities()).size());
		Assert.assertEquals(0, ((HashSet<Entity>) this.projectContext.getContextEntities()).size());
		Assert.assertEquals(0, ((HashSet<Hierarchy>) this.projectContext.getFamilyHierarchies()).size());
		Assert.assertEquals(0, ((HashSet<Hierarchy>) this.projectContext.getContextHierarchies()).size());
	}
	
	@Test
	public void testShouldReturnEntityAndHierarchyContent() {
		this.entities.add("entity1");
		this.entities.add("entity2");
		Hierarchy hierarchy = new Hierarchy();
		hierarchy.setGeneralEntityId(this.entities.get("entity1").getId());
		try {
			hierarchy.addChildEntity(this.entities.get("entity2").getId());
		} catch (Exception e) {
		}
		this.hierarchies.add(hierarchy);
		this.diagram.setEntities(this.entities);
		this.diagram.setHierarchies(this.hierarchies);
		
		this.projectContext.addProjectDiagram(this.diagram);
		
		Assert.assertEquals(2, ((HashSet<Entity>) this.projectContext.getAllEntities()).size());
		Assert.assertEquals(1, ((HashSet<Hierarchy>) this.projectContext.getAllHierarchies()).size());
		Assert.assertEquals(1, ((HashSet<Entity>) this.projectContext.getAllEntities(new Entity("entity1"))).size());
		Assert.assertTrue(((HashSet<Entity>) this.projectContext.getAllEntities()).contains(this.entities.get("entity1")));
		Assert.assertTrue(((HashSet<Entity>) this.projectContext.getAllEntities()).contains(this.entities.get("entity2")));
		Assert.assertTrue(((HashSet<Hierarchy>) this.projectContext.getAllHierarchies()).contains(hierarchy));
		Assert.assertSame(this.entities.get("entity1"), this.projectContext.getEntity(this.entities.get("entity1").getId()));
		Assert.assertSame(hierarchy, this.projectContext.getHierarchy(hierarchy.getId()));
		Assert.assertNull(this.projectContext.getHierarchy(UUID.randomUUID()));
		Assert.assertNull(this.projectContext.getEntity(UUID.randomUUID()));
	}
	
	@Test
	public void testShouldReturnEntityAndHierarchyContentFromContext() throws Exception{
		Diagram diag = new Diagram();
		diag.setName("parent");
		diag.getEntities().add("entity3");
		diag.getEntities().add("entity4");
		Hierarchy hierarchy2 = new Hierarchy();
		hierarchy2.setGeneralEntityId(diag.getEntities().get("entity3").getId());
		hierarchy2.addChildEntity(diag.getEntities().get("entity4").getId());
		diag.getHierarchies().add(hierarchy2);
		
		this.projectContext.addContextDiagram(diag);
		
		this.entities.add("entity1");
		this.entities.add("entity2");
		Hierarchy hierarchy = new Hierarchy();
		hierarchy.setGeneralEntityId(this.entities.get("entity1").getId());
		hierarchy.addChildEntity(this.entities.get("entity2").getId());
		this.hierarchies.add(hierarchy);
		this.diagram.setEntities(this.entities);
		this.diagram.setHierarchies(this.hierarchies);
		
		this.projectContext.addContextDiagram(this.diagram);
		
		Assert.assertEquals(2, ((HashSet<Entity>)this.projectContext.getContextEntities()).size());
		Assert.assertEquals(4, ((HashSet<Entity>)this.projectContext.getFamilyEntities()).size());
		Assert.assertEquals(3, ((HashSet<Entity>)this.projectContext.getFamilyEntities(this.entities.get("entity1"))).size());
		
		Assert.assertEquals(1, ((HashSet<Hierarchy>)this.projectContext.getContextHierarchies()).size());
		Assert.assertEquals(2, ((HashSet<Hierarchy>)this.projectContext.getFamilyHierarchies()).size());
	}
}
