package controllers.tests;


import java.awt.Point;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;

import controllers.tests.mocks.*;
import infrastructure.Func;
import infrastructure.IterableExtensions;
import models.Attribute;
import models.AttributeCollection;
import models.AttributeType;
import models.Cardinality;
import models.Diagram;
import models.Entity;
import models.Hierarchy;
import models.IdGroup;
import models.IdGroupCollection;
import models.Relationship;
import models.RelationshipEntity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import persistence.tests.TestUtilities;
import styling.StyleConstants;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEventObject;

import controllers.DiagramController;

public class DiagramControllerTestCase {

	private MockProjectContext projectContext;
	private MockDiagramView diagramView;
	private MockEntityController entityController;
	private MockEntityControllerFactory entityControllerFactory;
	private MockRelationshipControllerFactory relationshipControllerFactory;
	private MockRelationshipController relationshipController;
	private MockXmlFileManager xmlFileManager;
	private MockDiagramXmlManager diagramXmlManager;
	private MockHierarchyController hierarchyController;
	private MockHierarchyControllerFactory hierarchyControllerFactory;
	private MockGraphPersistenceService graphPersistenceService;
    private MockFileSystemService fileSystemService;

    private MockProjectValidationService projectValidationService;


    @Before
	public void setUp() throws Exception {
		this.projectContext = new MockProjectContext();
		this.diagramView = new MockDiagramView();
		this.entityController = new MockEntityController();
		this.entityControllerFactory = new MockEntityControllerFactory();
		this.entityControllerFactory.setController(this.entityController);
		this.relationshipController = new MockRelationshipController();
		this.relationshipControllerFactory = new MockRelationshipControllerFactory();
		this.relationshipControllerFactory.setController(this.relationshipController);
		this.xmlFileManager = new MockXmlFileManager();
		this.diagramXmlManager = new MockDiagramXmlManager();
		this.hierarchyController = new MockHierarchyController();
		this.hierarchyControllerFactory = new MockHierarchyControllerFactory();
		this.hierarchyControllerFactory.setController(this.hierarchyController);
		this.graphPersistenceService = new MockGraphPersistenceService();
        this.fileSystemService = new MockFileSystemService();
        this.fileSystemService.setExistsReturnValue(true);
        this.projectValidationService = new MockProjectValidationService();
	}
	
	@Test
	public void testShouldSetControllerToView()
	{
		DiagramController diagramController = this.createController();
		
		Assert.assertSame(diagramController, this.diagramView.getController());
		Assert.assertSame(diagramController.getGraph(), this.diagramView.getGraph());
	}
	
	@Test
	public void testShouldCreateEntityThroughEntityControllerWhenCreatingEntityWithoutPosition(){
		Entity entity = new Entity("Product");
				
		DiagramController diagramController = this.createController();
		
		Assert.assertEquals(0, this.entityController.getCreateCallsCount());
		Assert.assertFalse(diagramController.hasPendingEntity());
		
		diagramController.createEntity();
		diagramController.handleCreatedEvent(entity);
		
		Assert.assertEquals(1, this.entityController.getCreateCallsCount());
		Assert.assertTrue(diagramController.hasPendingEntity());
	}
	
	@Test
	public void testShouldNotCreateEntityIfThereIsAPendingEntity(){
		Entity entity = new Entity("Product");
		
		DiagramController diagramController = this.createController();
		
		Assert.assertEquals(0, this.entityController.getCreateCallsCount());
		Assert.assertFalse(diagramController.hasPendingEntity());
		
		diagramController.createEntity();
		diagramController.handleCreatedEvent(entity);
		
		Assert.assertEquals(1, this.entityController.getCreateCallsCount());
		Assert.assertTrue(diagramController.hasPendingEntity());
		
		diagramController.createEntity();
		
		Assert.assertEquals(1, this.entityController.getCreateCallsCount());
		Assert.assertTrue(diagramController.hasPendingEntity());
	}
	
	@Test
	public void testShouldCreateCellsForEntityAttributesAndLinksWhenAddingEntity() throws Exception
	{
		Entity entity = new Entity("Product");
		entity.getAttributes().addAttribute("Stock");
		entity.getAttributes().addAttribute("Name");
		entity.getAttributes().addAttribute("Price");
				
		DiagramController diagramController = this.createController();
		
		diagramController.createEntity();
		diagramController.handleCreatedEvent(entity);
		
		Assert.assertTrue(diagramController.hasPendingEntity());
		
		diagramController.addEntity(20, 30);
		
		Assert.assertFalse(diagramController.hasPendingEntity());
		
		Assert.assertSame(entity, IterableExtensions.firstOrDefault
				(diagramController.getDiagram().getEntities(), new Func<Entity, String, Boolean>() {

					@Override
					public Boolean execute(Entity entity, String name) {
						return entity.getName().equalsIgnoreCase(name);
					}
				}, "Product"));
		
		mxCell entityCell = diagramController.getEntityCell(entity.getId().toString());
		Assert.assertEquals("Product", diagramController.getGraph().getLabel(entityCell));
		Assert.assertTrue(diagramController.getGraph().getModel().isVertex(entityCell));
		Assert.assertEquals(20.0, entityCell.getGeometry().getX(), 0);
		Assert.assertEquals(30.0, entityCell.getGeometry().getY(), 0);
		
		mxCell stockCell = diagramController.getAttributeCell(entity.getId().toString()+"Stock");
		Assert.assertEquals("Stock", diagramController.getGraph().getLabel(stockCell));
		Assert.assertTrue(diagramController.getGraph().getModel().isVertex(stockCell));
		
		mxCell nameCell = diagramController.getAttributeCell(entity.getId().toString()+"Name");
		Assert.assertEquals("Name", diagramController.getGraph().getLabel(nameCell));
		Assert.assertTrue(diagramController.getGraph().getModel().isVertex(nameCell));
		
		mxCell priceCell = diagramController.getAttributeCell(entity.getId().toString()+"Price");
		Assert.assertEquals("Price", diagramController.getGraph().getLabel(priceCell));
		Assert.assertTrue(diagramController.getGraph().getModel().isVertex(priceCell));
		
		mxCell entityStockCell = diagramController.getAttributeConnectorCell(entity.getId().toString()+"Stock");
		Assert.assertTrue(diagramController.getGraph().getModel().isEdge(entityStockCell));
		Object[] entityStockConnectors = diagramController.getGraph().getEdgesBetween(entityCell, stockCell);
		Assert.assertEquals(1, entityStockConnectors.length);
		Assert.assertSame(entityStockCell, entityStockConnectors[0]);
		
		mxCell entityNameCell = diagramController.getAttributeConnectorCell(entity.getId().toString()+"Name");
		Assert.assertTrue(diagramController.getGraph().getModel().isEdge(entityNameCell));
		Object[] entityNameConnectors = diagramController.getGraph().getEdgesBetween(entityCell, nameCell);
		Assert.assertEquals(1, entityNameConnectors.length);
		Assert.assertSame(entityNameCell, entityNameConnectors[0]);
		
		mxCell entityPriceCell = diagramController.getAttributeConnectorCell(entity.getId().toString()+"Price");
		Assert.assertTrue(diagramController.getGraph().getModel().isEdge(entityPriceCell));
		Object[] entityPriceConnectors = diagramController.getGraph().getEdgesBetween(entityCell, priceCell);
		Assert.assertEquals(1, entityPriceConnectors.length);
		Assert.assertSame(entityPriceCell, entityPriceConnectors[0]);
	}

	@Test
	public void testShouldCreateRelationshipThroughRelationshipControllerWhenCreateRelationshipIsCalled(){
		DiagramController controller = this.createController();
		
		Assert.assertFalse(this.relationshipController.createWasCalled());
		
		controller.createRelationship();
		
		Assert.assertTrue(this.relationshipController.createWasCalled());
	}
	
	@Test
	public void testShouldRegisterToHandleRelationshipCreatedEvents(){
		DiagramController controller = this.createController();
		
		Assert.assertEquals(0, this.relationshipController.getListeners().size());
		
		controller.createRelationship();
		
		Assert.assertEquals(1, this.relationshipController.getListeners().size());
		Assert.assertSame(controller, this.relationshipController.getListeners().toArray()[0]);
	}
	
	@Test
	public void testShouldRegisterToHandleEntityCreatedEvents(){
		DiagramController diagramController = this.createController();
		Assert.assertEquals(0, this.entityController.getListeners().size());
		
		diagramController.createEntity();
		
		Assert.assertEquals(1, this.entityController.getListeners().size());
		Assert.assertSame(diagramController, this.entityController.getListeners().toArray()[0]);
	}
		
	@Test
	public void testShouldCreateCellsForRelationshipWhenAddingRelationship() throws Exception{
		Entity entity1 = new Entity("Entity1");
		Entity entity2 = new Entity("Entity2");
		Entity entity3 = new Entity("Entity3");
		
		DiagramController diagramController = this.createController();
		
		this.addEntityToDiagram(diagramController, entity1, 20, 30);
		this.addEntityToDiagram(diagramController, entity2, 60, 30);
		this.addEntityToDiagram(diagramController, entity3, 20, 100);
		
		RelationshipEntity relationshipEntity11 = 
			new RelationshipEntity(entity1, new Cardinality(0, 1), "Role1");
		RelationshipEntity relationshipEntity12 = 
			new RelationshipEntity(entity1, new Cardinality(1, 1), "Role2");
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(entity2, new Cardinality(0, Double.POSITIVE_INFINITY), "");
		RelationshipEntity relationshipEntity3 = 
			new RelationshipEntity(entity3, new Cardinality(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), "");
		
		Relationship relationship = new Relationship(relationshipEntity11, relationshipEntity12);
		relationship.setName("Relationship");
		relationship.addRelationshipEntity(relationshipEntity2);
		relationship.addRelationshipEntity(relationshipEntity3);
		
		relationship.getAttributes().addAttribute("Attribute1");
		relationship.getAttributes().addAttribute("Attribute2");
		
		diagramController.createRelationship();
		diagramController.handleCreatedEvent(relationship);
		
		Func<Entity, String, Boolean> cmpFunc = new Func<Entity, String, Boolean>() {
			@Override
			public Boolean execute(Entity entity, String name) {
				return entity.getName().equalsIgnoreCase(name);
			};
		};
		
		Assert.assertSame(entity1, IterableExtensions.firstOrDefault
				(diagramController.getDiagram().getEntities(), cmpFunc, "Entity1"));
		
		Assert.assertSame(entity2, IterableExtensions.firstOrDefault
				(diagramController.getDiagram().getEntities(), cmpFunc, "Entity2"));
		
		Assert.assertSame(entity3, IterableExtensions.firstOrDefault
				(diagramController.getDiagram().getEntities(), cmpFunc, "Entity3"));
		
		Assert.assertSame(relationship, IterableExtensions.firstOrDefault
				(diagramController.getDiagram().getRelationships(), new Func<Relationship, String, Boolean>() {
					@Override
					public Boolean execute(Relationship relationship, String name) {
						return relationship.getName().equalsIgnoreCase(name);
					};}, "Relationship"));
		
		mxCell relationshipCell = diagramController.getRelationshipCell(relationship.getId().toString());
		
		Assert.assertEquals("Relationship", diagramController.getGraph().getLabel(relationshipCell));
		Assert.assertTrue(diagramController.getGraph().getModel().isVertex(relationshipCell));
		Assert.assertEquals(40.0, relationshipCell.getGeometry().getX(), 0);
		Assert.assertEquals(65.0, relationshipCell.getGeometry().getY(), 0);
		
		mxCell entity1Cell = diagramController.getEntityCell(entity1.getId().toString());
		mxCell relationshipEntity11Cell = 
			diagramController.getRelationshipConnectorCell
				(relationship.getId().toString()+entity1.getId().toString()+"Role1"+"1");
		mxCell relationshipEntity12Cell = 
			diagramController.getRelationshipConnectorCell
				(relationship.getId().toString()+entity1.getId().toString()+"Role2"+"2");
		Assert.assertTrue(diagramController.getGraph().getModel().isEdge(relationshipEntity11Cell));
		Object[] entity1RelationshipConnectors = diagramController.getGraph().getEdgesBetween(entity1Cell, relationshipCell);
		Assert.assertEquals(2, entity1RelationshipConnectors.length);
		Assert.assertSame(relationshipEntity11Cell, entity1RelationshipConnectors[0]);
		Assert.assertSame(relationshipEntity12Cell, entity1RelationshipConnectors[1]);
		Assert.assertEquals("Role1 (0,1)", relationshipEntity11Cell.getValue());
		Assert.assertEquals("Role2 (1,1)", relationshipEntity12Cell.getValue());
		
		mxCell entity2Cell = diagramController.getEntityCell(entity2.getId().toString());
		mxCell relationshipEntity2Cell = 
			diagramController.getRelationshipConnectorCell
				(relationship.getId().toString()+entity2.getId().toString()+""+"1");
		Assert.assertTrue(diagramController.getGraph().getModel().isEdge(relationshipEntity2Cell));
		Object[] entity2RelationshipConnectors = diagramController.getGraph().getEdgesBetween(entity2Cell, relationshipCell);
		Assert.assertEquals(1, entity2RelationshipConnectors.length);
		Assert.assertSame(relationshipEntity2Cell, entity2RelationshipConnectors[0]);
		Assert.assertEquals("(0,*)", relationshipEntity2Cell.getValue());
		
		mxCell entity3Cell = diagramController.getEntityCell(entity3.getId().toString());
		mxCell relationshipEntity3Cell = 
			diagramController.getRelationshipConnectorCell
				(relationship.getId().toString()+entity3.getId().toString()+""+"1");
		Assert.assertTrue(diagramController.getGraph().getModel().isEdge(relationshipEntity3Cell));
		Object[] entity3RelationshipConnectors = diagramController.getGraph().getEdgesBetween(entity3Cell, relationshipCell);
		Assert.assertEquals(1, entity3RelationshipConnectors.length);
		Assert.assertSame(relationshipEntity3Cell, entity3RelationshipConnectors[0]);
		Assert.assertEquals("(*,*)", relationshipEntity3Cell.getValue());
		
		mxCell attribute1Cell = diagramController.getAttributeCell(relationship.getId().toString()+"Attribute1");
		Assert.assertTrue(diagramController.getGraph().getModel().isVertex(attribute1Cell));
		Assert.assertEquals("Attribute1", attribute1Cell.getValue());
		
		mxCell relationshipAttribute1Cell = diagramController.getAttributeConnectorCell(relationship.getId().toString()+"Attribute1");
		Assert.assertTrue(diagramController.getGraph().getModel().isEdge(relationshipAttribute1Cell));
		
		Object[] attribute1Connectors = diagramController.getGraph().getEdgesBetween(attribute1Cell, relationshipCell);
		Assert.assertEquals(1, attribute1Connectors.length);
		Assert.assertSame(relationshipAttribute1Cell, attribute1Connectors[0]);
		
		mxCell attribute2Cell = diagramController.getAttributeCell(relationship.getId().toString()+"Attribute2");
		Assert.assertTrue(diagramController.getGraph().getModel().isVertex(attribute2Cell));
		Assert.assertEquals("Attribute2", attribute2Cell.getValue());
		
		mxCell relationshipAttribute2Cell = diagramController.getAttributeConnectorCell(relationship.getId().toString()+"Attribute2");
		Assert.assertTrue(diagramController.getGraph().getModel().isEdge(relationshipAttribute2Cell));
		
		Object[] attribute2Connectors = diagramController.getGraph().getEdgesBetween(attribute2Cell, relationshipCell);
		Assert.assertEquals(1, attribute2Connectors.length);
		Assert.assertSame(relationshipAttribute2Cell, attribute2Connectors[0]);
	}

	@Test
	public void testShouldCallSaveWithDiagramNameDashCompWhenSaveIsCalled() throws ParserConfigurationException{
		this.projectContext.setName("projectName");
		
		Document document = TestUtilities.createDocument();
		this.xmlFileManager.setDocumentToCreate(document);
		this.diagramXmlManager.setElementNameOfRoot("diagram");
		
		DiagramController controller = this.createController();
		controller.getDiagram().setName("Diagram");
		
		Assert.assertNull(this.xmlFileManager.getDocumentToSave());
		Assert.assertNull(this.xmlFileManager.getPathToSave());
		Assert.assertFalse(this.xmlFileManager.wasCreateDocumentCalled());
		Assert.assertEquals(0, this.graphPersistenceService.getSaveCalls());
		Assert.assertNull(this.graphPersistenceService.getGraphToSave());
		
		controller.save();
		
		Assert.assertTrue(this.xmlFileManager.wasCreateDocumentCalled());
		Assert.assertNotNull(this.xmlFileManager.getDocumentToSave());
		Assert.assertNotNull(this.xmlFileManager.getPathToSave());
		
		Assert.assertEquals(1, this.graphPersistenceService.getSaveCalls());
		Assert.assertSame(controller.getGraph(), this.graphPersistenceService.getGraphToSave());
		Assert.assertEquals("projectName/Datos/Diagram-rep", this.graphPersistenceService.getSavePath());
		
		Assert.assertSame(controller.getDiagram(), this.diagramXmlManager.getDiagramRelatedToElement());
		Assert.assertSame(document, this.xmlFileManager.getDocumentToSave());
		Assert.assertEquals("diagram", ((Element)document.getFirstChild()).getTagName());
		Assert.assertEquals("projectName/Datos/Diagram-comp", this.xmlFileManager.getPathToSave());
	}
	
	@Test
	public void testShouldMoveAttributesWhenMovingRelationshipNodeOrEntity() throws Exception
	{
		Entity entity1 = new Entity("Entity1");
		entity1.getAttributes().addAttribute("Attribute1");
		
		Entity entity2 = new Entity("Entity2");
		entity2.getAttributes().addAttribute("Attribute1");
		
		DiagramController diagramController = this.createController();
		
		this.addEntityToDiagram(diagramController, entity1, 20, 30);
		this.addEntityToDiagram(diagramController, entity2, 60, 30);
		
		RelationshipEntity relationshipEntity11 = 
			new RelationshipEntity(entity1, new Cardinality(0, 1), "Role1");
		RelationshipEntity relationshipEntity12 = 
			new RelationshipEntity(entity1, new Cardinality(1, 1), "Role2");
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(entity2, new Cardinality(0, Double.POSITIVE_INFINITY), "");
		
		Relationship relationship = new Relationship(relationshipEntity11, relationshipEntity12);
		relationship.setName("Relationship");
		relationship.addRelationshipEntity(relationshipEntity2);
		
		relationship.getAttributes().addAttribute("Attribute1");
		
		diagramController.handleCreatedEvent(relationship);
		
		mxCell entity1Cell = diagramController.getEntityCell(entity1.getId().toString());
		mxCell entity2Cell = diagramController.getEntityCell(entity2.getId().toString());
		mxCell relationshipCell = diagramController.getRelationshipCell(relationship.getId().toString());
		
		mxEventObject eventObject = new mxEventObject("name");
		
		// there is a bug in JGraph. "removed" means added to selection, and "added" means removed from selection
		ArrayList<Object> added = new ArrayList<Object>();
		ArrayList<Object> removed = new ArrayList<Object>();
		removed.add(entity1Cell);
		removed.add(entity2Cell);
		eventObject.getProperties().put("removed", removed);
		diagramController.invoke(null, eventObject);
		
		added.add(entity2Cell);
		removed.clear();
		removed.add(relationshipCell);
		eventObject.getProperties().put("removed", removed);
		eventObject.getProperties().put("added", added);
		diagramController.invoke(null, eventObject);
		
		Point start = new Point(20, 30);
		
		Point end = new Point(80, 40);
		
		mxCell entity1Attribute = diagramController.getAttributeCell(entity1.getId().toString()+"Attribute1");
		mxCell entity2Attribute = diagramController.getAttributeCell(entity2.getId().toString()+"Attribute1");
		mxCell relationshipAttribute = diagramController.getAttributeCell(relationship.getId().toString()+"Attribute1");
		
		double entity1AttributeStartX = entity1Attribute.getGeometry().getX();
		double entity1AttributeStartY = entity1Attribute.getGeometry().getY();
		
		double entity2AttributeStartX = entity2Attribute.getGeometry().getX();
		double entity2AttributeStartY = entity2Attribute.getGeometry().getY();
		
		double relationshipAttributeStartX = relationshipAttribute.getGeometry().getX();
		double relationshipAttributeStartY = relationshipAttribute.getGeometry().getY();
		
		diagramController.handleDragStart(start);
		
		diagramController.handleDrop(end);
		
		Assert.assertEquals(entity1AttributeStartX + 60, entity1Attribute.getGeometry().getX(), 0);
		Assert.assertEquals(entity1AttributeStartY + 10, entity1Attribute.getGeometry().getY(), 0);
		
		Assert.assertEquals(entity2AttributeStartX, entity2Attribute.getGeometry().getX(), 0);
		Assert.assertEquals(entity2AttributeStartY, entity2Attribute.getGeometry().getY(), 0);
		
		Assert.assertEquals(relationshipAttributeStartX + 60, relationshipAttribute.getGeometry().getX(), 0);
		Assert.assertEquals(relationshipAttributeStartY + 10, relationshipAttribute.getGeometry().getY(), 0);
	}
	
	@Test
	public void testShouldOpenADiagram() throws Exception
	{
		String path = "diagrama.xml";
		DiagramController controller = this.createController();
		
		controller.openDiagram(path);
		
		Assert.assertEquals(path, this.xmlFileManager.getPathsRead().get(0));
		Assert.assertSame(this.xmlFileManager.getCreatedDocuments().get(0).getDocumentElement(), this.diagramXmlManager.getElementsPassedAsParameter().get(0));
		Assert.assertSame(this.diagramXmlManager.getDiagramRelatedToElement(), controller.getDiagram());
	}
	
	@Test
	public void testShouldCreateHierarchyThroughHierarchyControllerWhenCreatingHierarchy(){
		DiagramController diagramController = this.createController();
		
		Assert.assertEquals(0, this.hierarchyController.getCreateCallsCount());
		Assert.assertEquals(0, this.hierarchyControllerFactory.getCreateCallsCount());
		
		diagramController.createHierarchy();
		
		Assert.assertEquals(1, this.hierarchyControllerFactory.getCreateCallsCount());
		Assert.assertEquals(1, this.hierarchyController.getCreateCallsCount());
	}
	
	@Test
	public void testShouldAddSubscriptionToHierarchyCreatedToController(){
		DiagramController diagramController = this.createController();
		
		Assert.assertEquals(0, this.hierarchyController.getListeners().size());
				
		diagramController.createHierarchy();
		
		Assert.assertEquals(1, this.hierarchyController.getListeners().size());
		Assert.assertSame(diagramController, this.hierarchyController.getListeners().get(0));
	}
	
	@Test
	public void testShouldAddHierarchyConnectorsWhenHandlingHierarchyCreated() throws Exception
	{
		Entity entity1 = new Entity("Entity1");
		Entity entity2 = new Entity("Entity2");
		Entity entity3 = new Entity("Entity3");
		Entity entity4 = new Entity("Entity4");
		
		DiagramController diagramController = this.createController();
		
		this.addEntityToDiagram(diagramController, entity1, 20, 30);
		this.addEntityToDiagram(diagramController, entity2, 60, 30);
		this.addEntityToDiagram(diagramController, entity3, 20, 100);
		this.addEntityToDiagram(diagramController, entity4, 20, 100);
		
		Hierarchy hierarchy = new Hierarchy();
		hierarchy.setGeneralEntityId(entity1.getId());
		hierarchy.addChildEntity(entity2.getId());
		hierarchy.addChildEntity(entity3.getId());
		hierarchy.addChildEntity(entity4.getId());
		
		hierarchy.setExclusive(true);
		hierarchy.setTotal(false);
		
		Assert.assertEquals(0, diagramController.getDiagram().getHierarchies().count());
		
		diagramController.createHierarchy();
		diagramController.handleCreatedEvent(hierarchy);
		
		Assert.assertEquals(1, diagramController.getDiagram().getHierarchies().count());
		Assert.assertNotNull(diagramController.getDiagram().getHierarchies().getHierarchy(hierarchy.getId()));
		
		mxCell nodeCell = diagramController.getHierarchyNodeCell(hierarchy.getId().toString());
		mxCell entity1Cell = diagramController.getEntityCell(entity1.getId().toString());
		mxCell entity2Cell = diagramController.getEntityCell(entity2.getId().toString());
		mxCell entity3Cell = diagramController.getEntityCell(entity3.getId().toString());
		mxCell entity4Cell = diagramController.getEntityCell(entity4.getId().toString());
		
		mxCell entity1HierarchyConnectorCell = diagramController.getHierarchyConnectorCell(hierarchy.getId().toString() + entity1.getId().toString());
		Object[] entity1HierarchyConnectors = diagramController.getGraph().getEdgesBetween(nodeCell, entity1Cell);
		Assert.assertEquals(1, entity1HierarchyConnectors.length);
		Assert.assertSame(entity1HierarchyConnectorCell, entity1HierarchyConnectors[0]);
		Assert.assertEquals("(P,E)", entity1HierarchyConnectorCell.getValue());
		
		mxCell entity2HierarchyConnectorCell = diagramController.getHierarchyConnectorCell(hierarchy.getId().toString() + entity2.getId().toString());
		Object[] entity2HierarchyConnectors = diagramController.getGraph().getEdgesBetween(nodeCell, entity2Cell);
		Assert.assertEquals(1, entity2HierarchyConnectors.length);
		Assert.assertSame(entity2HierarchyConnectorCell, entity2HierarchyConnectors[0]);
		
		mxCell entity3HierarchyConnectorCell = diagramController.getHierarchyConnectorCell(hierarchy.getId().toString() + entity3.getId().toString());
		Object[] entity3HierarchyConnectors = diagramController.getGraph().getEdgesBetween(nodeCell, entity3Cell);
		Assert.assertEquals(1, entity3HierarchyConnectors.length);
		Assert.assertSame(entity3HierarchyConnectorCell, entity3HierarchyConnectors[0]);

		mxCell entity4HierarchyConnectorCell = diagramController.getHierarchyConnectorCell(hierarchy.getId().toString() + entity4.getId().toString());
		Object[] entity4HierarchyConnectors = diagramController.getGraph().getEdgesBetween(nodeCell, entity4Cell);
		Assert.assertEquals(1, entity4HierarchyConnectors.length);
		Assert.assertSame(entity4HierarchyConnectorCell, entity4HierarchyConnectors[0]);
	}
	
	@Test
	public void shouldRaiseEntityAddedEventWhenEntityIsAdded() throws Exception{
		Entity entity = new Entity("Product");
		
		MockDiagramListener listener = new MockDiagramListener();
		
		DiagramController diagramController = this.createController();
		diagramController.addListener(listener);
		
		diagramController.createEntity();
		diagramController.handleCreatedEvent(entity);
		
		Assert.assertNull(listener.getDiagram());
		Assert.assertNull(listener.getEntity());
		
		diagramController.addEntity(0, 0);
		
		Assert.assertSame(entity, listener.getEntity());
		Assert.assertSame(diagramController.getDiagram(), listener.getDiagram());
	}
	
	@Test
	public void shouldRaiseSubdiagramCreatedWhenSubdiagramIsCreated() throws Exception{	
		Document document = TestUtilities.createDocument();
		this.xmlFileManager.setDocumentToCreate(document);
		this.diagramXmlManager.setElementNameOfRoot("diagram");
		
		MockDiagramListener listener = new MockDiagramListener();
		
		DiagramController diagramController = this.createController();
		diagramController.getDiagram().setName("Diagram");
		diagramController.addListener(listener);
			
		Assert.assertNull(listener.getDiagram());
		Assert.assertNull(listener.getDiagramName());
		
		diagramController.createSubDiagram("ChildDiagram");
		
		Assert.assertSame(diagramController.getDiagram(), listener.getDiagram());
		Assert.assertEquals("ChildDiagram", listener.getDiagramName());
	}
	
	@Test
	public void shouldSaveDiagramWhenCreatingSubDiagram() throws Exception{	
		this.projectContext.setName("projectName");
		
		Document document = TestUtilities.createDocument();
		this.xmlFileManager.setDocumentToCreate(document);
		this.diagramXmlManager.setElementNameOfRoot("diagram");
		
		DiagramController diagramController = this.createController();
		diagramController.getDiagram().setName("Diagram");
		
		Assert.assertNull(this.xmlFileManager.getDocumentToSave());
		Assert.assertNull(this.xmlFileManager.getPathToSave());
		Assert.assertFalse(this.xmlFileManager.wasCreateDocumentCalled());
		Assert.assertEquals(0, this.graphPersistenceService.getSaveCalls());
		Assert.assertNull(this.graphPersistenceService.getGraphToSave());
				
		diagramController.createSubDiagram("ChildDiagram");
		
		Assert.assertTrue(this.xmlFileManager.wasCreateDocumentCalled());
		Assert.assertNotNull(this.xmlFileManager.getDocumentToSave());
		Assert.assertNotNull(this.xmlFileManager.getPathToSave());
		Assert.assertEquals(1, this.graphPersistenceService.getSaveCalls());
		Assert.assertSame(diagramController.getGraph(), this.graphPersistenceService.getGraphToSave());
		Assert.assertEquals("projectName/Datos/Diagram-rep", this.graphPersistenceService.getSavePath());
		
		Assert.assertSame(diagramController.getDiagram(), this.diagramXmlManager.getDiagramRelatedToElement());
		Assert.assertSame(document, this.xmlFileManager.getDocumentToSave());
		Assert.assertEquals("diagram", ((Element)document.getFirstChild()).getTagName());
		Assert.assertEquals("projectName/Datos/Diagram-comp", this.xmlFileManager.getPathToSave());
	}
	
	@Test
	public void shouldRaiseRelationshipAddedEventWhenRelationshipIsAdded() throws Exception{
		Entity entity1 = new Entity("Entity1");
		Entity entity2 = new Entity("Entity2");
		
		DiagramController diagramController = this.createController();
		
		this.addEntityToDiagram(diagramController, entity1, 20, 30);
		this.addEntityToDiagram(diagramController, entity2, 60, 30);
		
		RelationshipEntity relationshipEntity1 = 
			new RelationshipEntity(entity1, new Cardinality(0, 1), "Role1");
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(entity2, new Cardinality(0, Double.POSITIVE_INFINITY), "");
		
		Relationship relationship = new Relationship(relationshipEntity1, relationshipEntity2);
		relationship.setName("Relationship");
		
		MockDiagramListener listener = new MockDiagramListener();
		
		diagramController.addListener(listener);

		Assert.assertNull(listener.getDiagram());
		Assert.assertNull(listener.getRelationship());
		
		diagramController.createRelationship();
		diagramController.handleCreatedEvent(relationship);
		
		Assert.assertSame(relationship, listener.getRelationship());
		Assert.assertSame(diagramController.getDiagram(), listener.getDiagram());
	}
	
	@Test
	public void testShouldRaiseHierarchyCreatedWhenHierarchyIsAdded() throws Exception
	{
		Entity entity1 = new Entity("Entity1");
		Entity entity2 = new Entity("Entity2");
		Entity entity3 = new Entity("Entity3");
		Entity entity4 = new Entity("Entity4");
		
		DiagramController diagramController = this.createController();
		
		this.addEntityToDiagram(diagramController, entity1, 20, 30);
		this.addEntityToDiagram(diagramController, entity2, 60, 30);
		this.addEntityToDiagram(diagramController, entity3, 20, 100);
		this.addEntityToDiagram(diagramController, entity4, 20, 100);
		
		Hierarchy hierarchy = new Hierarchy();
		hierarchy.setGeneralEntityId(entity1.getId());
		hierarchy.addChildEntity(entity2.getId());
		hierarchy.addChildEntity(entity3.getId());
		hierarchy.addChildEntity(entity4.getId());
		
		hierarchy.setExclusive(true);
		hierarchy.setTotal(false);

		MockDiagramListener listener = new MockDiagramListener();
		
		diagramController.addListener(listener);

		Assert.assertNull(listener.getDiagram());
		Assert.assertNull(listener.getHierarchy());
		
		diagramController.createHierarchy();
		diagramController.handleCreatedEvent(hierarchy);
		
		Assert.assertSame(hierarchy, listener.getHierarchy());
		Assert.assertSame(diagramController.getDiagram(), listener.getDiagram());
	}
	
	@Test
	public void testShouldCreateCompositeAttributeCellsForEntity() throws Exception
	{
		Entity entity = new Entity("Product");
		
		entity.getAttributes().addAttribute("Level1", new Cardinality(0, 1), new IdGroupCollection(), AttributeType.characterization, null);
		Attribute level1 = entity.getAttributes().getAttribute("Level1");
		
		level1.getAttributes().addAttribute("Level2", new Cardinality(0, 1), new IdGroupCollection(), AttributeType.characterization, null);
		Attribute level2 = level1.getAttributes().getAttribute("Level2");
		
		level2.getAttributes().addAttribute("Level3", new Cardinality(0, 1), new IdGroupCollection(), AttributeType.characterization, null);
		Attribute level3 = level2.getAttributes().getAttribute("Level3");
		
		DiagramController diagramController = this.createController();
		
		diagramController.createEntity();
		diagramController.handleCreatedEvent(entity);
		
		Assert.assertTrue(diagramController.hasPendingEntity());
		
		diagramController.addEntity(20, 30);
		
		mxCell level1Cell = diagramController.getAttributeCell(entity.getId().toString()+level1.getName());
		mxCell level2Cell = diagramController.getAttributeCell(entity.getId().toString()+level2.getName());
		mxCell level3Cell = diagramController.getAttributeCell(entity.getId().toString()+level3.getName());
		
		mxCell level1And2ConnectorCell = diagramController.getAttributeConnectorCell(entity.getId().toString()+level2.getName());
		mxCell level2And3ConnectorCell = diagramController.getAttributeConnectorCell(entity.getId().toString()+level3.getName());
		
		Assert.assertEquals("Level1", diagramController.getGraph().getLabel(level1Cell));
		Assert.assertEquals(StyleConstants.COMPOSED_ATTRIBUTE_STYLE, level1Cell.getStyle());
		
		Assert.assertEquals("Level2", diagramController.getGraph().getLabel(level2Cell));
		Assert.assertEquals(StyleConstants.COMPOSED_ATTRIBUTE_STYLE, level2Cell.getStyle());
		
		Assert.assertEquals("Level3", diagramController.getGraph().getLabel(level3Cell));
		Assert.assertEquals(StyleConstants.ATTRIBUTE_STYLE, level3Cell.getStyle());
		
		Object[] level1To2Connectors = diagramController.getGraph().getEdgesBetween(level1Cell, level2Cell);
		Object[] level2To3Connectors = diagramController.getGraph().getEdgesBetween(level2Cell, level3Cell);

		Assert.assertEquals(1, level1To2Connectors.length);
		Assert.assertSame(level1And2ConnectorCell, level1To2Connectors[0]);
		Assert.assertEquals(StyleConstants.COMPOSED_ATTRIBUTE_LINK_STYLE, level1And2ConnectorCell.getStyle());
		
		Assert.assertEquals(1, level2To3Connectors.length);
		Assert.assertSame(level2And3ConnectorCell, level2To3Connectors[0]);
		Assert.assertEquals(StyleConstants.ATTRIBUTE_LINK_STYLE, level2And3ConnectorCell.getStyle());
	}
	
	@Test
	public void testShouldCreateCompositeAttributeCellsForRelationship() throws Exception
	{
		Entity entity1 = new Entity("Entity1");
		
		Entity entity2 = new Entity("Entity2");
		
		DiagramController diagramController = this.createController();
		
		this.addEntityToDiagram(diagramController, entity1, 20, 30);
		this.addEntityToDiagram(diagramController, entity2, 60, 30);
		
		RelationshipEntity relationshipEntity1 = 
			new RelationshipEntity(entity1, new Cardinality(0, 1), "Role1");
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(entity2, new Cardinality(0, Double.POSITIVE_INFINITY), "");
		
		Relationship relationship = new Relationship(relationshipEntity1, relationshipEntity2);
		relationship.setName("Relationship");
		
		relationship.getAttributes().addAttribute("Level1", new Cardinality(0, 1), new IdGroupCollection(), AttributeType.characterization, null);
		Attribute level1 = relationship.getAttributes().getAttribute("Level1");
		
		level1.getAttributes().addAttribute("Level2", new Cardinality(0, 1), new IdGroupCollection(), AttributeType.characterization, null);
		Attribute level2 = level1.getAttributes().getAttribute("Level2");
		
		level2.getAttributes().addAttribute("Level3", new Cardinality(0, 1), new IdGroupCollection(), AttributeType.characterization, null);
		Attribute level3 = level2.getAttributes().getAttribute("Level3");
		
		diagramController.handleCreatedEvent(relationship);
		
		mxCell level1Cell = diagramController.getAttributeCell(relationship.getId().toString()+level1.getName());
		mxCell level2Cell = diagramController.getAttributeCell(relationship.getId().toString()+level2.getName());
		mxCell level3Cell = diagramController.getAttributeCell(relationship.getId().toString()+level3.getName());
		
		mxCell level1And2ConnectorCell = diagramController.getAttributeConnectorCell(relationship.getId().toString()+level2.getName());
		mxCell level2And3ConnectorCell = diagramController.getAttributeConnectorCell(relationship.getId().toString()+level3.getName());
		
		Assert.assertEquals("Level1", diagramController.getGraph().getLabel(level1Cell));
		Assert.assertEquals(StyleConstants.COMPOSED_ATTRIBUTE_STYLE, level1Cell.getStyle());
		
		Assert.assertEquals("Level2", diagramController.getGraph().getLabel(level2Cell));
		Assert.assertEquals(StyleConstants.COMPOSED_ATTRIBUTE_STYLE, level2Cell.getStyle());
		
		Assert.assertEquals("Level3", diagramController.getGraph().getLabel(level3Cell));
		Assert.assertEquals(StyleConstants.ATTRIBUTE_STYLE, level3Cell.getStyle());
		
		Object[] level1To2Connectors = diagramController.getGraph().getEdgesBetween(level1Cell, level2Cell);
		Object[] level2To3Connectors = diagramController.getGraph().getEdgesBetween(level2Cell, level3Cell);

		Assert.assertEquals(1, level1To2Connectors.length);
		Assert.assertSame(level1And2ConnectorCell, level1To2Connectors[0]);
		Assert.assertEquals(StyleConstants.COMPOSED_ATTRIBUTE_LINK_STYLE, level1And2ConnectorCell.getStyle());
		
		Assert.assertEquals(1, level2To3Connectors.length);
		Assert.assertSame(level2And3ConnectorCell, level2To3Connectors[0]);
		Assert.assertEquals(StyleConstants.ATTRIBUTE_LINK_STYLE, level2And3ConnectorCell.getStyle());
	}
	
	@Test
	public void testShouldUseKeyAttributeStyleForAttributesWhichAreKeysOnTheirOwn() throws Exception
	{
		Entity entity = new Entity("Entity");
		
		IdGroupCollection collection1 = new IdGroupCollection();
		collection1.addIdGroup(new IdGroup("1"));
		
		IdGroupCollection collection2 = new IdGroupCollection();
		collection2.addIdGroup(new IdGroup("2"));
		collection2.addIdGroup(new IdGroup("3"));
		
		IdGroupCollection collection3 = new IdGroupCollection();
		collection3.addIdGroup(new IdGroup("4"));
		
		IdGroupCollection collection4 = new IdGroupCollection();
		collection4.addIdGroup(new IdGroup("2"));
		
		IdGroupCollection collection5 = new IdGroupCollection();
		collection5.addIdGroup(new IdGroup("3"));
		
		entity.getAttributes().addAttribute("Attribute1", new Cardinality(0, 1), collection1, AttributeType.copy, null);
		entity.getAttributes().addAttribute("Attribute2", new Cardinality(0, 1), collection2, AttributeType.copy, null);
		entity.getAttributes().addAttribute("Attribute3", new Cardinality(0, 1), collection3, AttributeType.calculated, "expression");
		entity.getAttributes().addAttribute("Attribute4", new Cardinality(0, 1), collection4, AttributeType.copy, null);
		entity.getAttributes().addAttribute("Attribute5", new Cardinality(0, 1), collection5, AttributeType.copy, null);
		
		DiagramController diagramController = this.createController();
		
		this.addEntityToDiagram(diagramController, entity, 10, 20);
		
		mxCell attribute1ConnectorCell = diagramController.getAttributeConnectorCell(entity.getId()+"Attribute1");
		mxCell attribute2ConnectorCell = diagramController.getAttributeConnectorCell(entity.getId()+"Attribute2");
		mxCell attribute3ConnectorCell = diagramController.getAttributeConnectorCell(entity.getId()+"Attribute3");
		mxCell attribute4ConnectorCell = diagramController.getAttributeConnectorCell(entity.getId()+"Attribute4");
		mxCell attribute5ConnectorCell = diagramController.getAttributeConnectorCell(entity.getId()+"Attribute5");
		
		Assert.assertTrue(attribute1ConnectorCell.getStyle().contains(StyleConstants.KEY_ATTRIBUTE_LINK_STYLE));
		Assert.assertTrue(attribute2ConnectorCell.getStyle().contains(StyleConstants.ATTRIBUTE_LINK_STYLE));
		Assert.assertTrue(attribute3ConnectorCell.getStyle().contains(StyleConstants.CALCULATED_KEY_ATTRIBUTE_LINK_STYLE));
		Assert.assertTrue(attribute4ConnectorCell.getStyle().contains(StyleConstants.ATTRIBUTE_LINK_STYLE));
		Assert.assertTrue(attribute5ConnectorCell.getStyle().contains(StyleConstants.ATTRIBUTE_LINK_STYLE));
	}
	
	@Test
	public void testShouldJoinAttributesThatMakeUpEachIdGroup() throws Exception
	{
		Entity entity = new Entity("Entity");
		
		IdGroupCollection collection1 = new IdGroupCollection();
		collection1.addIdGroup(new IdGroup("1"));
		
		IdGroupCollection collection2 = new IdGroupCollection();
		collection2.addIdGroup(new IdGroup("1"));
		collection2.addIdGroup(new IdGroup("2"));
		
		IdGroupCollection collection3 = new IdGroupCollection();
		collection3.addIdGroup(new IdGroup("2"));
		
		entity.getAttributes().addAttribute("Attribute1", new Cardinality(0, 1), collection1, AttributeType.copy, null);
		entity.getAttributes().addAttribute("Attribute2", new Cardinality(0, 1), collection2, AttributeType.copy, null);
		entity.getAttributes().addAttribute("Attribute3", new Cardinality(0, 1), collection3, AttributeType.calculated, "expression");
		entity.getAttributes().addAttribute("Attribute4", new Cardinality(0, 1), collection3, AttributeType.copy, null);
		
		DiagramController diagramController = this.createController();
		
		this.addEntityToDiagram(diagramController, entity, 10, 20);
		
		mxCell attribute1To2ConnectorCell = diagramController.getIdGroupConnectorCell(entity.getId()+"_Attribute1_Attribute2_1");
		mxCell attribute2To3ConnectorCell = diagramController.getIdGroupConnectorCell(entity.getId()+"_Attribute2_Attribute3_2");
		mxCell attribute3To4ConnectorCell = diagramController.getIdGroupConnectorCell(entity.getId()+"_Attribute3_Attribute4_2");
		
		mxCell attribute1ConnectorCell = diagramController.getAttributeConnectorCell(entity.getId()+"Attribute1");
		mxCell attribute2ConnectorCell = diagramController.getAttributeConnectorCell(entity.getId()+"Attribute2");
		mxCell attribute3ConnectorCell = diagramController.getAttributeConnectorCell(entity.getId()+"Attribute3");
		mxCell attribute4ConnectorCell = diagramController.getAttributeConnectorCell(entity.getId()+"Attribute4");
		
		Object[] attribute1To2ConnectorCells = diagramController.getGraph().getEdgesBetween(attribute1ConnectorCell, attribute2ConnectorCell);
		Assert.assertEquals(1, attribute1To2ConnectorCells.length);
		Assert.assertSame(attribute1To2ConnectorCell, attribute1To2ConnectorCells[0]);
		
		Object[] attribute2To3ConnectorCells = diagramController.getGraph().getEdgesBetween(attribute2ConnectorCell, attribute3ConnectorCell);
		Assert.assertEquals(1, attribute2To3ConnectorCells.length);
		Assert.assertSame(attribute2To3ConnectorCell, attribute2To3ConnectorCells[0]);
		
		Object[] attribute3To4ConnectorCells = diagramController.getGraph().getEdgesBetween(attribute2To3ConnectorCell, attribute4ConnectorCell);
		Assert.assertEquals(1, attribute3To4ConnectorCells.length);
		Assert.assertSame(attribute3To4ConnectorCell, attribute3To4ConnectorCells[0]);		
	}
	
	@Test
	public void testShouldJoinAttributesThatMakeUpEachIdGroupWithLinesFromStrongEntity() throws Exception
	{
		Entity entity1 = new Entity("Entity1");
		
		Entity entity2 = new Entity("Entity2");
		
		DiagramController diagramController = this.createController();

		IdGroupCollection collection1 = new IdGroupCollection();
		collection1.addIdGroup(new IdGroup("1"));
		
		IdGroupCollection collection2 = new IdGroupCollection();
		collection2.addIdGroup(new IdGroup("2"));
		
		IdGroupCollection collection3 = new IdGroupCollection();
		collection3.addIdGroup(new IdGroup("2"));
		
		entity1.getAttributes().addAttribute("Attribute1", new Cardinality(0, 1), collection1, AttributeType.copy, null);
		entity1.getAttributes().addAttribute("Attribute2", new Cardinality(0, 1), collection2, AttributeType.copy, null);
		entity1.getAttributes().addAttribute("Attribute3", new Cardinality(0, 1), collection3, AttributeType.calculated, "expression");
		entity1.getAttributes().addAttribute("Attribute4", new Cardinality(0, 1), collection3, AttributeType.copy, null);
		
		this.addEntityToDiagram(diagramController, entity1, 20, 30);
		this.addEntityToDiagram(diagramController, entity2, 60, 30);
		
		mxCell attribute1To2ConnectorCell = diagramController.getIdGroupConnectorCell(entity1.getId()+"_Attribute1_Attribute2_1");
		mxCell attribute2To3ConnectorCell = diagramController.getIdGroupConnectorCell(entity1.getId()+"_Attribute2_Attribute3_2");
		mxCell attribute3To4ConnectorCell = diagramController.getIdGroupConnectorCell(entity1.getId()+"_Attribute3_Attribute4_2");
		
		mxCell attribute1ConnectorCell = diagramController.getAttributeConnectorCell(entity1.getId()+"Attribute1");
		mxCell attribute2ConnectorCell = diagramController.getAttributeConnectorCell(entity1.getId()+"Attribute2");
		mxCell attribute3ConnectorCell = diagramController.getAttributeConnectorCell(entity1.getId()+"Attribute3");
		mxCell attribute4ConnectorCell = diagramController.getAttributeConnectorCell(entity1.getId()+"Attribute4");
		
		RelationshipEntity relationshipEntity1 = 
			new RelationshipEntity(entity1, new Cardinality(0, 1), "Role1", false);
		RelationshipEntity relationshipEntity2 = 
			new RelationshipEntity(entity2, new Cardinality(0, Double.POSITIVE_INFINITY), "", true);
		
		Relationship relationship = new Relationship(relationshipEntity1, relationshipEntity2);
		relationship.setName("Relationship");
		
		Object[] attribute2To3ConnectorCells = diagramController.getGraph().getEdgesBetween(attribute2ConnectorCell, attribute3ConnectorCell);
		Assert.assertEquals(1, attribute2To3ConnectorCells.length);
		Assert.assertSame(attribute2To3ConnectorCell, attribute2To3ConnectorCells[0]);
		
		Object[] attribute3To4ConnectorCells = diagramController.getGraph().getEdgesBetween(attribute2To3ConnectorCell, attribute4ConnectorCell);
		Assert.assertEquals(1, attribute3To4ConnectorCells.length);
		Assert.assertSame(attribute3To4ConnectorCell, attribute3To4ConnectorCells[0]);
		
		diagramController.handleCreatedEvent(relationship);
		
		mxCell attribute1ToEntity2ConnectorCell = diagramController.getWeakEntityConnectorCell(entity1.getId().toString() + "_" + relationship.getId().toString() +"_Attribute1_1");
		mxCell attribute2ToEntity2ConnectorCell = diagramController.getWeakEntityConnectorCell(entity1.getId().toString() + "_" + relationship.getId().toString() +"_Attribute2_2");
		
		mxCell entity2Cell = diagramController.getEntityCell(entity2.getId().toString());
		
		Object[] attribute1ToEntity2ConnectorCells = diagramController.getGraph().getEdgesBetween(attribute1ConnectorCell, entity2Cell);
		Assert.assertEquals(1, attribute1ToEntity2ConnectorCells.length);
		Assert.assertSame(attribute1ToEntity2ConnectorCell, attribute1ToEntity2ConnectorCells[0]);
		
		Object[] attribute2ToEntity2ConnectorCells = diagramController.getGraph().getEdgesBetween(attribute2To3ConnectorCell, entity2Cell);
		Assert.assertEquals(1, attribute2ToEntity2ConnectorCells.length);
		Assert.assertSame(attribute2ToEntity2ConnectorCell, attribute2ToEntity2ConnectorCells[0]);
	}
	
	@Test
	public void testShouldCallToLoadWithCorrectDiagramName() {
		Diagram diagram = new Diagram();
		diagram.setName("diagram1");
		
		this.graphPersistenceService.setCellsToLoad(new String[]{});
		
		DiagramController controller = this.createController();
		
		controller.load(diagram);
		
		Assert.assertEquals(this.projectContext.getDataDirectory() + "/" + diagram.getName() + "-rep", this.graphPersistenceService.getDiagramName());
	}
	
	@Test
	public void testShouldSaveCorrectlyCellsFromMxGraphInDiagramController() {
		Diagram diagram = new Diagram();
		diagram.setName("diagram1");
		
		this.graphPersistenceService.setCellsToLoad(new String[] {
				DiagramController.CellConstants.EntityPrefix + "1",
				DiagramController.CellConstants.WeakEntityConnectorPrefix + "1",
				DiagramController.CellConstants.IdGroupConnectorPrefix + "1",
				DiagramController.CellConstants.RelationshipPrefix + "1",
				DiagramController.CellConstants.AttributePrefix + "1",
				DiagramController.CellConstants.AttributeConnectorPrefix + "1",
				DiagramController.CellConstants.RelationshipConnectorPrefix + "1",
				DiagramController.CellConstants.HierarchyNodePrefix + "1",
				DiagramController.CellConstants.HierarchyConnectorPrefix + "1",
				});
		
		DiagramController controller = this.createController();
		
		controller.load(diagram);
		Assert.assertEquals(9, controller.getGraph().getChildCells(controller.getGraph().getDefaultParent()).length);
		
		Assert.assertNotNull(controller.getEntityCell("1"));
		Assert.assertNotNull(controller.getWeakEntityConnectorCell("1"));
		Assert.assertNotNull(controller.getIdGroupConnectorCell("1"));
		Assert.assertNotNull(controller.getRelationshipCell("1"));
		Assert.assertNotNull(controller.getAttributeCell("1"));
		Assert.assertNotNull(controller.getAttributeConnectorCell("1"));
		Assert.assertNotNull(controller.getRelationshipConnectorCell("1"));
		Assert.assertNotNull(controller.getHierarchyNodeCell("1"));
		Assert.assertNotNull(controller.getHierarchyConnectorCell("1"));
	}
	
	@Test
	public void testShouldRemoveHierarchyAndHierarchyConnectorCellsWhenUpdatingHierarchy() throws Exception {
		Diagram diagram = new Diagram();
		diagram.setName("diagram1");
		
		Entity gEntity = new Entity("gEntity");
		Entity entity1 = new Entity("entity1");
		Entity entity2 = new Entity("entity2");
		Entity entity3 = new Entity("entity3");
		
		Hierarchy hierarchy = new Hierarchy();
		hierarchy.setGeneralEntityId(gEntity.getId());
		hierarchy.addChildEntity(entity1.getId());
		hierarchy.addChildEntity(entity2.getId());
		hierarchy.addChildEntity(entity3.getId());
		
		this.graphPersistenceService.setCellsToLoad(new String[] {
				DiagramController.CellConstants.HierarchyNodePrefix + hierarchy.getId().toString(),
				DiagramController.CellConstants.HierarchyConnectorPrefix + hierarchy.getId().toString() + gEntity.getId().toString(),
				DiagramController.CellConstants.HierarchyConnectorPrefix + hierarchy.getId().toString() + entity1.getId().toString(),
				DiagramController.CellConstants.HierarchyConnectorPrefix + hierarchy.getId().toString() + entity2.getId().toString(),
				DiagramController.CellConstants.HierarchyConnectorPrefix + hierarchy.getId().toString() + entity3.getId().toString(),
				});
		
		DiagramController controller = this.createController();
		controller.load(diagram);
		
		Assert.assertNotNull(controller.getHierarchyNodeCell(hierarchy.getId().toString()));
		Assert.assertNotNull(controller.getHierarchyConnectorCell(hierarchy.getId().toString() + gEntity.getId().toString()));
		Assert.assertNotNull(controller.getHierarchyConnectorCell(hierarchy.getId().toString() + entity1.getId().toString()));
		Assert.assertNotNull(controller.getHierarchyConnectorCell(hierarchy.getId().toString() + entity2.getId().toString()));
		Assert.assertNotNull(controller.getHierarchyConnectorCell(hierarchy.getId().toString() + entity3.getId().toString()));
		
		controller.updateHierarchy(hierarchy);
		
		Assert.assertNull(controller.getHierarchyNodeCell(hierarchy.getId().toString()));
		Assert.assertNull(controller.getHierarchyConnectorCell(hierarchy.getId().toString() + gEntity.getId().toString()));
		Assert.assertNull(controller.getHierarchyConnectorCell(hierarchy.getId().toString() + entity1.getId().toString()));
		Assert.assertNull(controller.getHierarchyConnectorCell(hierarchy.getId().toString() + entity2.getId().toString()));
		Assert.assertNull(controller.getHierarchyConnectorCell(hierarchy.getId().toString() + entity3.getId().toString()));
	}
	
	@Test
	public void testShouldRemoveAttributesFromRelationshipWhenUpdatingRelationship() throws Exception {
		Diagram diagram = new Diagram();
		diagram.setName("diagram1");
		
		Attribute attribute1 = new Attribute("attribute 1");
		Attribute attribute2 = new Attribute("attribute 2");
		Attribute attribute3 = new Attribute("attribute 3");
		AttributeCollection attributes = new AttributeCollection(); 
		attributes.addAttribute(attribute1);
		attributes.addAttribute(attribute2);
		attributes.addAttribute(attribute3);
		
		Relationship relationship = new Relationship();
		relationship.setAttributes(attributes);
		
		this.graphPersistenceService.setCellsToLoad(new String[] {
				DiagramController.CellConstants.AttributePrefix + relationship.getId().toString() + attribute1.getName(),
				DiagramController.CellConstants.AttributePrefix + relationship.getId().toString() + attribute2.getName(),
				DiagramController.CellConstants.AttributePrefix + relationship.getId().toString() + attribute3.getName(),
				DiagramController.CellConstants.AttributeConnectorPrefix + relationship.getId().toString() + attribute1.getName(),
				DiagramController.CellConstants.AttributeConnectorPrefix + relationship.getId().toString() + attribute2.getName(),
				DiagramController.CellConstants.AttributeConnectorPrefix + relationship.getId().toString() + attribute3.getName()
				});
		
		DiagramController controller = this.createController();
		controller.load(diagram);
		
		Assert.assertNotNull(controller.getAttributeCell(relationship.getId().toString() + attribute1.getName()));
		Assert.assertNotNull(controller.getAttributeCell(relationship.getId().toString() + attribute2.getName()));
		Assert.assertNotNull(controller.getAttributeCell(relationship.getId().toString() + attribute3.getName()));
		Assert.assertNotNull(controller.getAttributeConnectorCell(relationship.getId().toString() + attribute1.getName()));
		Assert.assertNotNull(controller.getAttributeConnectorCell(relationship.getId().toString() + attribute2.getName()));
		Assert.assertNotNull(controller.getAttributeConnectorCell(relationship.getId().toString() + attribute3.getName()));
		
		controller.updateRelationship(relationship);
		
		Assert.assertNull(controller.getAttributeCell(relationship.getId().toString() + attribute1.getName()));
		Assert.assertNull(controller.getAttributeCell(relationship.getId().toString() + attribute2.getName()));
		Assert.assertNull(controller.getAttributeCell(relationship.getId().toString() + attribute3.getName()));
		Assert.assertNull(controller.getAttributeConnectorCell(relationship.getId().toString() + attribute1.getName()));
		Assert.assertNull(controller.getAttributeConnectorCell(relationship.getId().toString() + attribute2.getName()));
		Assert.assertNull(controller.getAttributeConnectorCell(relationship.getId().toString() + attribute3.getName()));
	}
	
	@Test
	public void testShouldRemoveWeakEntitiesWhenUpdatingRelationship() {
		Diagram diagram = new Diagram();
		diagram.setName("diagram1");
		
		RelationshipEntity relEntity1 = new RelationshipEntity(new Entity("entity1"));
		RelationshipEntity relEntity2 = new RelationshipEntity(new Entity("entity2"));
		relEntity1.setStrongEntity(true);
		
		Relationship relationship = new Relationship();
		relationship.addRelationshipEntity(relEntity1);
		relationship.addRelationshipEntity(relEntity2);

		this.graphPersistenceService.setCellsToLoad(new String[] {
				DiagramController.CellConstants.WeakEntityConnectorPrefix + relEntity2.getEntityId().toString() + "_" + relationship.getId().toString()
				});
		
		DiagramController controller = this.createController();
		controller.load(diagram);
		
		Assert.assertNotNull(controller.getWeakEntityConnectorCell(relEntity2.getEntityId().toString() + "_" + relationship.getId().toString()));
		
		controller.updateRelationship(relationship);
		
		Assert.assertNull(controller.getWeakEntityConnectorCell(relEntity2.getEntityId().toString() + "_" + relationship.getId().toString()));
	}
	
	@Test
	public void testShouldRemoveRelationshipConnectorsAndRelationshipCellsWhenUpdatingRelationship() {
		Diagram diagram = new Diagram();
		diagram.setName("diagram1");
		
		RelationshipEntity relEntity1 = new RelationshipEntity(new Entity("entity1"));
		RelationshipEntity relEntity2 = new RelationshipEntity(new Entity("entity2"));
		RelationshipEntity relEntity3 = new RelationshipEntity(new Entity("entity3"));
		
		Relationship relationship = new Relationship();
		relationship.addRelationshipEntity(relEntity1);
		relationship.addRelationshipEntity(relEntity1);
		relationship.addRelationshipEntity(relEntity2);
		relationship.addRelationshipEntity(relEntity3);
		
		this.graphPersistenceService.setCellsToLoad(new String[] {
				DiagramController.CellConstants.RelationshipPrefix + relationship.getId().toString(),
				DiagramController.CellConstants.RelationshipConnectorPrefix + relationship.getId().toString() + relEntity1.getEntityId().toString() +"1",
				DiagramController.CellConstants.RelationshipConnectorPrefix + relationship.getId().toString() + relEntity1.getEntityId().toString() +"2",
				DiagramController.CellConstants.RelationshipConnectorPrefix + relationship.getId().toString() + relEntity2.getEntityId().toString() +"1",
				DiagramController.CellConstants.RelationshipConnectorPrefix + relationship.getId().toString() + relEntity3.getEntityId().toString() +"1"
				});
		
		DiagramController controller = this.createController();
		controller.load(diagram);
		
		Assert.assertNotNull(controller.getRelationshipCell(relationship.getId().toString()));
		Assert.assertNotNull(controller.getRelationshipConnectorCell(relationship.getId().toString() + relEntity1.getEntityId().toString() +"1"));
		Assert.assertNotNull(controller.getRelationshipConnectorCell(relationship.getId().toString() + relEntity1.getEntityId().toString() +"2"));
		Assert.assertNotNull(controller.getRelationshipConnectorCell(relationship.getId().toString() + relEntity2.getEntityId().toString() +"1"));
		Assert.assertNotNull(controller.getRelationshipConnectorCell(relationship.getId().toString() + relEntity3.getEntityId().toString() +"1"));
		
		controller.updateRelationship(relationship);
		
		Assert.assertNull(controller.getRelationshipCell(relationship.getId().toString()));
		Assert.assertNull(controller.getRelationshipConnectorCell(relationship.getId().toString() + relEntity1.getEntityId().toString() +"1"));
		Assert.assertNull(controller.getRelationshipConnectorCell(relationship.getId().toString() + relEntity1.getEntityId().toString() +"2"));
		Assert.assertNull(controller.getRelationshipConnectorCell(relationship.getId().toString() + relEntity2.getEntityId().toString() +"1"));
		Assert.assertNull(controller.getRelationshipConnectorCell(relationship.getId().toString() + relEntity3.getEntityId().toString() +"1"));
	}
	
	@Test
	public void testShouldUpdateHierarchyThroughHierarchyControllerWhenUpdatingHierarchy() throws Exception {
		Entity entity1 = new Entity("entity1");
		Entity entity2 = new Entity("entity2");
		
		Hierarchy hierarchy = new Hierarchy();
		hierarchy.setGeneralEntityId(entity1.getId());
		hierarchy.addChildEntity(entity2.getId());
		
		DiagramController diagramController = this.createController();
		
		Assert.assertEquals(0, this.hierarchyController.getUpdateCallsCount());
		Assert.assertEquals(0, this.hierarchyControllerFactory.getCreateCallsCount());
		
		diagramController.updateHierarchy(hierarchy);
		
		Assert.assertEquals(1, this.hierarchyControllerFactory.getCreateCallsCount());
		Assert.assertEquals(1, this.hierarchyController.getUpdateCallsCount());
	}
		
	@Test
	public void testShouldUpdateEntityThroughEntityControllerWhenUpdatingEntity() {
		Entity entity = new Entity("enitty1");
		DiagramController diagramController = this.createController();
		
		Assert.assertEquals(0, this.entityController.getUpdateCallsCount());
		Assert.assertEquals(0, this.entityControllerFactory.getCreateCallsCount());
		
		diagramController.updateEntity(entity);
		
		Assert.assertEquals(1, this.entityControllerFactory.getCreateCallsCount());
		Assert.assertEquals(1, this.entityController.getUpdateCallsCount());
	}
	
	@Test
	public void testShouldUpdateRelationshipThroughRelationshipControllerWhenUpdatingRelationship() {
		Relationship relationship = new Relationship();
		DiagramController diagramController = this.createController();
		
		Assert.assertEquals(0, this.relationshipController.getUpdateCallsCount());
		Assert.assertEquals(0, this.relationshipControllerFactory.getCreateCallsCount());
		
		diagramController.updateRelationship(relationship);
		
		Assert.assertEquals(1, this.relationshipControllerFactory.getCreateCallsCount());
		Assert.assertEquals(1, this.relationshipController.getUpdateCallsCount());
	}
	
	
	private void addEntityToDiagram(DiagramController diagramController, 
			Entity entity, double x, double y) throws Exception {
		diagramController.createEntity();
		diagramController.handleCreatedEvent(entity);
		diagramController.addEntity(x, y);
	}
	
	private DiagramController createController() {
		return new DiagramController(this.projectContext, this.diagramView,
				this.entityControllerFactory, this.relationshipControllerFactory, 
				this.hierarchyControllerFactory, this.xmlFileManager, this.diagramXmlManager,
                this.graphPersistenceService , this.projectValidationService,this.fileSystemService);
	}

}

