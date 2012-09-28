package controllers.tests;


import infrastructure.visual.DiagramTreeNode;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import models.Diagram;
import models.Entity;
import models.Hierarchy;
import models.Relationship;
import models.RelationshipEntity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import controllers.IProjectController;
import controllers.ProjectController;
import controllers.factories.tests.mocks.MockDiagramControllerFactory;
import controllers.tests.mocks.MockDiagramController;
import controllers.tests.mocks.MockDiagramXmlManager;
import controllers.tests.mocks.MockFileSystemService;
import controllers.tests.mocks.MockProjectContext;
import controllers.tests.mocks.MockProjectView;
import controllers.tests.mocks.MockShell;
import controllers.tests.mocks.MockXmlFileManager;

public class ProjectControllerTestCase {

	private MockProjectContext projectContext;
	
	private MockDiagramControllerFactory diagramControllerFactory;

	private MockDiagramController diagramController;
	
	private MockProjectView projectView;
	
	private MockShell shell;
	
	private MockDiagramXmlManager diagramXmlManager;
	
	private MockXmlFileManager xmlFileManager;
	
	private MockFileSystemService fileSystemService;

	private MockProjectValidationService projectValidationService;
	
	@Before
	public void setUp() throws Exception {
		this.projectContext = new MockProjectContext();
		this.diagramControllerFactory = new MockDiagramControllerFactory();
		this.diagramController = new MockDiagramController();
		this.diagramControllerFactory.setController(this.diagramController);
		this.projectView = new MockProjectView();
		this.shell = new MockShell();
		this.diagramXmlManager = new MockDiagramXmlManager();
		this.xmlFileManager = new MockXmlFileManager();
		this.fileSystemService = new MockFileSystemService();
		this.fileSystemService.setExistsReturnValue(true);
		this.projectValidationService = new MockProjectValidationService();
	}
	
	@Test
	public void testShouldSetControllerToViewWhenConstructing(){
		Assert.assertNull(this.projectView.getController());
		IProjectController controller = this.createController(); 
		Assert.assertSame(controller, this.projectView.getController());
	}
	
	@Test
	public void testShouldReturnView(){
		IProjectController controller = this.createController(); 
		Assert.assertSame(this.projectView, controller.getView());
	}
	
	@Test
	public void testShouldCreateDirectoryForProject(){
		String projectName = UUID.randomUUID().toString();
		ProjectController controller = this.createController();
		Assert.assertFalse(fileExists(projectName));
		controller.createProject(projectName);
		Assert.assertTrue(fileExists(projectName));
		Assert.assertTrue(fileExists(projectName + "/Datos"));
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldSetNameToProjectContext(){
		String projectName = UUID.randomUUID().toString();
		Assert.assertNull(this.projectContext.getName());
		Assert.assertNull(this.projectContext.getDataDirectory());
		
		ProjectController controller = this.createController();
		controller.createProject(projectName);
		
		Assert.assertEquals(projectName, this.projectContext.getName());
		Assert.assertEquals(projectName + "/Datos", this.projectContext.getDataDirectory());
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldSetCurrentDiagramNameToPrincipal(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		
		Assert.assertNull(controller.getCurrentDiagramController());
		controller.createProject(projectName);
		
		Assert.assertSame(diagramController, controller.getCurrentDiagramController());
		Assert.assertEquals("Principal", diagramController.getDiagram().getName());
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldAddDiagramAsTreeModelRoot(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		
		Assert.assertNull(controller.getProjectTree());
		
		controller.createProject(projectName);
		
		Assert.assertSame(this.diagramController.getDiagram(), ((DefaultMutableTreeNode)controller.getProjectTree().getRoot()).getUserObject());
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldAddSelfAsListenerToDiagram(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		
		Assert.assertEquals(0, this.diagramController.getListeners().size());
		
		controller.createProject(projectName);
		
		Assert.assertEquals(1, this.diagramController.getListeners().size());
		Assert.assertSame(controller, this.diagramController.getListeners().get(0));
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldSetControllerViewAsShellRightContent(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		
		Assert.assertNull(this.shell.getRightContent());
		
		controller.createProject(projectName);
		
		Assert.assertNotNull(this.shell.getRightContent());
		Assert.assertSame(this.diagramController.getView(), this.shell.getRightContent());
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldExpandShellToFullSize(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		
		Assert.assertEquals(0, this.shell.getFullSizeCalls());
		
		controller.createProject(projectName);
		
		Assert.assertEquals(1, this.shell.getFullSizeCalls());
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldAddEntityToDiagramTreeNodeWhenEntityIsAddedToDiagram(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		controller.createProject(projectName);
		
		Entity entity = new Entity("Product");
		controller.handleEntityAdded(this.diagramController.getDiagram(), entity);

		DefaultMutableTreeNode root = (DefaultMutableTreeNode)controller.getProjectTree().getRoot();
		
		Assert.assertNotNull(this.getNodeChildWithObject(root, "Entities", entity));
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldAddRelationshipToDiagramTreeNodeWhenRelationshipIsAddedToDiagram() throws Exception{
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		controller.createProject(projectName);
		
		Relationship relationship = new Relationship(new RelationshipEntity(new Entity("E1")), new RelationshipEntity(new Entity("E2")));
		controller.handleRelationshipAdded(this.diagramController.getDiagram(), relationship);

		DefaultMutableTreeNode root = (DefaultMutableTreeNode)controller.getProjectTree().getRoot();
		
		Assert.assertNotNull(this.getNodeChildWithObject(root, "Relationships", relationship));
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldAddDiagramToParentDiagramInTree(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		controller.createProject(projectName);
		
		Diagram subDiagram = new Diagram();
		MockDiagramController childController = new MockDiagramController();
		childController.setDiagram(subDiagram);
		this.diagramControllerFactory.setController(childController);
		
		controller.handleSubDiagramCreated(controller.getCurrentDiagramController().getDiagram(), "ChildDiagram");
		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)controller.getProjectTree().getRoot();
		
		Assert.assertNotNull(this.getNodeChildWithObject(root, "Sub-Diagrams", subDiagram));
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldSetControllerViewAsShellRightContentWhenCreatingSubDiagram(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
				
		controller.createProject(projectName);
		
		Diagram subDiagram = new Diagram();
		MockDiagramController childController = new MockDiagramController();
		childController.setDiagram(subDiagram);
		this.diagramControllerFactory.setController(childController);
		
		Assert.assertNotNull(this.shell.getRightContent());
		Assert.assertSame(this.diagramController.getView(), this.shell.getRightContent());
		
		controller.handleSubDiagramCreated(controller.getCurrentDiagramController().getDiagram(), "ChildDiagram");
		
		Assert.assertEquals(subDiagram.getName(), "ChildDiagram");
		Assert.assertNotNull(this.shell.getRightContent());
		Assert.assertSame(childController.getView(), this.shell.getRightContent());
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldChangeCurrentControllerToChildController(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
				
		controller.createProject(projectName);
		
		Diagram subDiagram = new Diagram();
		MockDiagramController childController = new MockDiagramController();
		childController.setDiagram(subDiagram);
		this.diagramControllerFactory.setController(childController);
		
		Assert.assertNotSame(childController, controller.getCurrentDiagramController());
		
		controller.handleSubDiagramCreated(controller.getCurrentDiagramController().getDiagram(), "ChildDiagram");
		
		Assert.assertSame(childController, controller.getCurrentDiagramController());
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldChangeCurrentDiagramNodeWhenCreatingSubDiagram(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
				
		controller.createProject(projectName);
		
		Diagram subDiagram = new Diagram();
		MockDiagramController childController = new MockDiagramController();
		childController.setDiagram(subDiagram);
		this.diagramControllerFactory.setController(childController);
		
		controller.handleSubDiagramCreated(controller.getCurrentDiagramController().getDiagram(), "ChildDiagram");
		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)controller.getProjectTree().getRoot();
		
		DefaultMutableTreeNode subDiagramNode = this.getNodeChildWithObject(root, "Sub-Diagrams", subDiagram);
		Assert.assertNotNull(subDiagramNode);
		
		Entity entity = new Entity("Product");
		controller.handleEntityAdded(this.diagramController.getDiagram(), entity);

		Assert.assertNotNull(this.getNodeChildWithObject(subDiagramNode, "Entities", entity));
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldAddListenerToChildController(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		controller.createProject(projectName);
		
		Diagram subDiagram = new Diagram();
		MockDiagramController childController = new MockDiagramController();
		childController.setDiagram(subDiagram);
		this.diagramControllerFactory.setController(childController);
		
		controller.handleSubDiagramCreated(controller.getCurrentDiagramController().getDiagram(), "ChildDiagram");
		
		Assert.assertEquals(1, childController.getListeners().size());
		Assert.assertSame(controller, childController.getListeners().get(0));
			
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldAddDiagramToGlobalAndContextDiagramCollectionsWhenChildDiagramIsCreated(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		controller.createProject(projectName);
		
		Diagram subDiagram = new Diagram();
		MockDiagramController childController = new MockDiagramController();
		childController.setDiagram(subDiagram);
		this.diagramControllerFactory.setController(childController);
		
		Assert.assertEquals(1, this.projectContext.getGlobalDiagrams().size());
		Assert.assertEquals(1, this.projectContext.getContextDiagrams().size());
		
		controller.handleSubDiagramCreated(controller.getCurrentDiagramController().getDiagram(), "ChildDiagram");
		
		Assert.assertEquals(2, this.projectContext.getGlobalDiagrams().size());
		Assert.assertEquals(2, this.projectContext.getContextDiagrams().size());
			
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldAddDiagramAsSubDiagramOfOriginalWhenAddingNewDiagram(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		controller.createProject(projectName);
		
		Diagram parentDiagram = controller.getCurrentDiagramController().getDiagram();
		
		Diagram subDiagram = new Diagram();
		MockDiagramController childController = new MockDiagramController();
		childController.setDiagram(subDiagram);
		this.diagramControllerFactory.setController(childController);
		
		Assert.assertEquals(0, parentDiagram.getSubDiagrams().size());
		
		controller.handleSubDiagramCreated(controller.getCurrentDiagramController().getDiagram(), "ChildDiagram");
		
		Assert.assertEquals(1, parentDiagram.getSubDiagrams().size());
		Assert.assertSame(subDiagram, parentDiagram.getSubDiagrams().get(0));
	
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldAddDiagramToGlobalAndContextDiagramCollectionsWhenCreating(){
		String projectName = UUID.randomUUID().toString();
		
		Assert.assertEquals(0, this.projectContext.getGlobalDiagrams().size());
		Assert.assertEquals(0, this.projectContext.getContextDiagrams().size());
		
		ProjectController controller = this.createController();
		controller.createProject(projectName);
		
		Assert.assertEquals(1, this.projectContext.getGlobalDiagrams().size());
		Assert.assertEquals(1, this.projectContext.getContextDiagrams().size());
			
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldAddDiagramsInTreeNodeToProjectContext() {
		Diagram diagram1 = new Diagram();
		Diagram diagram2 = new Diagram();
		Diagram diagram3 = new Diagram();
		diagram1.setName("1");
		diagram2.setName("2");
		diagram3.setName("3");
		DiagramTreeNode tree1 = new DiagramTreeNode(diagram1, this.projectContext);
		DiagramTreeNode tree2 = new DiagramTreeNode(diagram2, this.projectContext);
		DiagramTreeNode tree3 = new DiagramTreeNode(diagram3, this.projectContext);
		
		DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("dato1");
		DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("dato2");
		DefaultMutableTreeNode child4 = new DefaultMutableTreeNode("dato3");
		
		Object[] path = {tree1, child1, child2, child4, tree2, tree3};
		
		ProjectController controller = this.createController();
		controller.createProject("projectName");
		controller.changeElement(new TreePath(path));
		
		List<Diagram> diagrams = this.projectContext.getContextDiagrams();
		
		Assert.assertEquals(3, diagrams.size());
		
		Assert.assertSame(diagram1, diagrams.get(0));
		Assert.assertSame(diagram2, diagrams.get(1));
		Assert.assertSame(diagram3, diagrams.get(2));
		
		deleteFile("projectName/Datos");
		deleteFile("projectName");
	}
	
	@Test
	public void testShouldGetDiagramsDiagramHierarchyAddToGlobalDiagramsAndMainToContextDiagram() throws Exception{
		List<Diagram> diagrams = new ArrayList<Diagram>();
		
		Diagram main = new Diagram();
		main.setName("Principal");
		
		Diagram level1_1 = new Diagram();
		level1_1.setName("Principal-1");
		
		Diagram level1_2 = new Diagram();
		level1_2.setName("Principal-2");
		
		Diagram level2_1_1= new Diagram();
		level2_1_1.setName("Principal-1-1");
		
		Diagram level2_1_2 = new Diagram();
		level2_1_2.setName("Principal-1-2");
		
		Diagram level2_2_1 = new Diagram();
		level2_2_1.setName("Principal-2-1");
		
		main.getSubDiagramNames().add("Principal-1");
		main.getSubDiagramNames().add("Principal-2");
		
		level1_1.getSubDiagramNames().add("Principal-1-1");
		level1_1.getSubDiagramNames().add("Principal-1-2");
		level1_2.getSubDiagramNames().add("Principal-2-1");
		
		diagrams.add(main);
		diagrams.add(level1_1);
		diagrams.add(level2_1_1);
		diagrams.add(level2_1_2);
		diagrams.add(level1_2);
		diagrams.add(level2_2_1);
		
		this.diagramXmlManager.setDiagramsToReturn(diagrams);
		
		ProjectController controller = this.createController();
		controller.openProject("projectName");
		
		Assert.assertEquals("projectName/Datos", this.projectContext.getDataDirectory());
		
		Assert.assertEquals(this.projectContext.getDataDirectory()+"/Principal-comp", this.xmlFileManager.getPathsRead().get(0));
		Assert.assertEquals(this.projectContext.getDataDirectory()+"/Principal-1-comp", this.xmlFileManager.getPathsRead().get(1));
		Assert.assertEquals(this.projectContext.getDataDirectory()+"/Principal-1-1-comp", this.xmlFileManager.getPathsRead().get(2));
		Assert.assertEquals(this.projectContext.getDataDirectory()+"/Principal-1-2-comp", this.xmlFileManager.getPathsRead().get(3));
		Assert.assertEquals(this.projectContext.getDataDirectory()+"/Principal-2-comp", this.xmlFileManager.getPathsRead().get(4));
		Assert.assertEquals(this.projectContext.getDataDirectory()+"/Principal-2-1-comp", this.xmlFileManager.getPathsRead().get(5));
		
		Assert.assertSame(this.diagramXmlManager.getElementsPassedAsParameter().get(0), this.xmlFileManager.getCreatedDocuments().get(0).getDocumentElement());
		Assert.assertSame(this.diagramXmlManager.getElementsPassedAsParameter().get(1), this.xmlFileManager.getCreatedDocuments().get(1).getDocumentElement());
		Assert.assertSame(this.diagramXmlManager.getElementsPassedAsParameter().get(2), this.xmlFileManager.getCreatedDocuments().get(2).getDocumentElement());
		Assert.assertSame(this.diagramXmlManager.getElementsPassedAsParameter().get(3), this.xmlFileManager.getCreatedDocuments().get(3).getDocumentElement());
		Assert.assertSame(this.diagramXmlManager.getElementsPassedAsParameter().get(4), this.xmlFileManager.getCreatedDocuments().get(4).getDocumentElement());
		Assert.assertSame(this.diagramXmlManager.getElementsPassedAsParameter().get(5), this.xmlFileManager.getCreatedDocuments().get(5).getDocumentElement());
		
		Assert.assertSame(main, this.projectContext.getGlobalDiagrams().get(0));
		Assert.assertSame(level1_1, this.projectContext.getGlobalDiagrams().get(1));
		Assert.assertSame(level2_1_1, this.projectContext.getGlobalDiagrams().get(2));
		Assert.assertSame(level2_1_2, this.projectContext.getGlobalDiagrams().get(3));
		Assert.assertSame(level1_2, this.projectContext.getGlobalDiagrams().get(4));
		Assert.assertSame(level2_2_1, this.projectContext.getGlobalDiagrams().get(5));
		
		Assert.assertSame(main, this.projectContext.getContextDiagrams().get(0));
	}
	
	@Test
	public void testShouldAddSubDiagramsAndMainDiagramToTree() throws Exception{
		List<Diagram> diagrams = new ArrayList<Diagram>();
		
		Diagram main = new Diagram();
		main.setName("Principal");
		
		Diagram level1_1 = new Diagram();
		level1_1.setName("Principal-1");
		
		Diagram level1_2 = new Diagram();
		level1_2.setName("Principal-2");
		
		Diagram level2_1_1= new Diagram();
		level2_1_1.setName("Principal-1-1");
		
		Diagram level2_1_2 = new Diagram();
		level2_1_2.setName("Principal-1-2");
		
		Diagram level2_2_1 = new Diagram();
		level2_2_1.setName("Principal-2-1");
		
		main.getSubDiagramNames().add("Principal-1");
		main.getSubDiagramNames().add("Principal-2");
		
		level1_1.getSubDiagramNames().add("Principal-1-1");
		level1_1.getSubDiagramNames().add("Principal-1-2");
		level1_2.getSubDiagramNames().add("Principal-2-1");
		
		diagrams.add(main);
		diagrams.add(level1_1);
		diagrams.add(level2_1_1);
		diagrams.add(level2_1_2);
		diagrams.add(level1_2);
		diagrams.add(level2_2_1);
		
		this.diagramXmlManager.setDiagramsToReturn(diagrams);
		
		ProjectController controller = this.createController();
		controller.openProject("projectName");
		
		DefaultMutableTreeNode root = ((DefaultMutableTreeNode)controller.getProjectTree().getRoot());
		Assert.assertSame(main, root.getUserObject());
		DefaultMutableTreeNode child1 = this.getNodeChildWithObject(root, "Sub-Diagrams", level1_1);
		DefaultMutableTreeNode child2 = this.getNodeChildWithObject(root, "Sub-Diagrams", level1_2);
		Assert.assertNotNull(child1);
		Assert.assertNotNull(child2);
		
		DefaultMutableTreeNode child1_1 = this.getNodeChildWithObject(child1, "Sub-Diagrams", level2_1_1);
		DefaultMutableTreeNode child1_2 = this.getNodeChildWithObject(child1, "Sub-Diagrams", level2_1_2);
		DefaultMutableTreeNode child2_1 = this.getNodeChildWithObject(child2, "Sub-Diagrams", level2_2_1);
		
		Assert.assertNotNull(child1_1);
		Assert.assertNotNull(child1_2);
		Assert.assertNotNull(child2_1);
	}
	
	@Test
	public void testShouldSaveChildDiagramWhenCreatingSubDiagram(){
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		controller.createProject(projectName);
		
		MockDiagramController parentController = (MockDiagramController) controller.getCurrentDiagramController();
		
		Diagram subDiagram = new Diagram();
		MockDiagramController childController = new MockDiagramController();
		childController.setDiagram(subDiagram);
		this.diagramControllerFactory.setController(childController);
		
		Assert.assertEquals(0, childController.getSaveCalls());
		Assert.assertEquals(0, parentController.getSaveCalls());
		
		controller.handleSubDiagramCreated(controller.getCurrentDiagramController().getDiagram(), "ChildDiagram");
		
		Assert.assertEquals(1, childController.getSaveCalls());
		Assert.assertEquals(1, parentController.getSaveCalls());
	
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldAddHierarchyToDiagramTreeNodeWhenHierarchyIsAddedToDiagram() throws Exception{
		UUID parentId = UUID.randomUUID();
		UUID child1Id = UUID.randomUUID();
		UUID child2Id = UUID.randomUUID();
		
		Hierarchy hierarchy = new Hierarchy();
		hierarchy.setGeneralEntityId(parentId);
		hierarchy.addChildEntity(child1Id);
		hierarchy.addChildEntity(child2Id);
		
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		controller.createProject(projectName);
		
		controller.handleHierarchyAdded(this.diagramController.getDiagram(), hierarchy);

		DefaultMutableTreeNode root = (DefaultMutableTreeNode)controller.getProjectTree().getRoot();
		
		Assert.assertNotNull(this.getNodeChildWithObject(root, "Hierarchies", hierarchy));
		
		deleteFile(projectName + "/Datos");
		deleteFile(projectName);
	}
	
	@Test
	public void testShouldNotOpenUnexistentProject() throws Exception {
		this.fileSystemService.setExistsReturnValue(false);
		
		String projectName = UUID.randomUUID().toString();
		ProjectController controller = this.createController();
		Assert.assertFalse(controller.openProject(projectName));
	}
	
	@Test
	public void testShouldAddSelfAsListenerToDiagramWhenIsOpened() throws Exception{
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		
		Assert.assertEquals(0, this.diagramController.getListeners().size());
		
		controller.openProject(projectName);
		
		Assert.assertEquals(1, this.diagramController.getListeners().size());
		Assert.assertSame(controller, this.diagramController.getListeners().get(0));
	}
	
	@Test
	public void testShouldRemoveOldComponentsInProjectContextWhenOpenOtherProject() throws Exception {
		Entity entity = new Entity("entity1");
		
		Diagram diagram = new Diagram();
		diagram.getEntities().add(entity);
		
		this.projectContext.addProjectDiagram(diagram);
		this.projectContext.addContextDiagram(diagram);
		
		
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		
		Assert.assertNotNull(this.projectContext.getEntity(entity.getId()));
		
		controller.openProject(projectName);
		
		Assert.assertNull(this.projectContext.getEntity(entity.getId()));
	}
	
	@Test
	public void testShouldLoadADiagram() throws Exception{
		String projectName = UUID.randomUUID().toString();
		
		ProjectController controller = this.createController();
		
		controller.openProject(projectName);
		
		Assert.assertTrue(this.diagramController.wasCalled());
		Assert.assertEquals("Principal", this.diagramController.getLoadedDiagram().getName());
	}

	@Test
	public void testShouldLoadDiagramIfItSelectedInTreeAndSaveOldOne() {
		Diagram diagram1 = new Diagram();
		Diagram diagram2 = new Diagram();
		Diagram diagram3 = new Diagram();
		diagram1.setName("1");
		diagram2.setName("2");
		diagram3.setName("3");
		DiagramTreeNode diagramNode1 = new DiagramTreeNode(diagram1, this.projectContext);
		DiagramTreeNode diagramNode2 = new DiagramTreeNode(diagram2, this.projectContext);
		DiagramTreeNode diagramNode3 = new DiagramTreeNode(diagram3, this.projectContext);
		
		DefaultMutableTreeNode diagramChild1 = new DefaultMutableTreeNode("dato1");
		DefaultMutableTreeNode diagramChild2 = new DefaultMutableTreeNode("dato2");
		
		Object[] path = {diagramNode1, diagramChild1, diagramNode2, diagramChild2, diagramNode3};
		
		MockDiagramController newController = new MockDiagramController();
		
		ProjectController controller = this.createController();
		controller.createProject("projectName");
		
		Assert.assertEquals(0, this.diagramController.getSaveCalls());
		Assert.assertNull(newController.getLoadedDiagram());
		Assert.assertEquals(1, this.diagramController.getListeners().size());
		Assert.assertSame(controller, this.diagramController.getListeners().get(0));
		Assert.assertEquals(0, newController.getListeners().size());
		
		this.diagramControllerFactory.setController(newController);
		controller.changeElement(new TreePath(path));
		
		Assert.assertEquals(1, newController.getListeners().size());
		Assert.assertSame(controller, newController.getListeners().get(0));
		Assert.assertEquals(1, this.diagramController.getSaveCalls());
		Assert.assertSame(diagram3, newController.getLoadedDiagram());
		
		Assert.assertSame(newController.getView(), this.shell.getRightContent());
		
		deleteFile("projectName/Datos");
		deleteFile("projectName");
	}
	
	@Test
	public void testShouldLoadFirstDiagramDiagramParentWhenSelectingChildSelectedInTreeAndSaveOldOne() {
		Diagram diagram1 = new Diagram();
		Diagram diagram2 = new Diagram();
		Diagram diagram3 = new Diagram();
		diagram1.setName("1");
		diagram2.setName("2");
		diagram3.setName("3");
		DiagramTreeNode diagramNode1 = new DiagramTreeNode(diagram1, this.projectContext);
		DiagramTreeNode diagramNode2 = new DiagramTreeNode(diagram2, this.projectContext);
		DiagramTreeNode diagramNode3 = new DiagramTreeNode(diagram3, this.projectContext);
		
		DefaultMutableTreeNode diagramChild1 = new DefaultMutableTreeNode("dato1");
		DefaultMutableTreeNode diagramChild2 = new DefaultMutableTreeNode("dato2");
		DefaultMutableTreeNode diagramChild3 = new DefaultMutableTreeNode("dato3");
		DefaultMutableTreeNode diagramChild4 = new DefaultMutableTreeNode("dato4");
		
		Object[] path = {diagramNode1, diagramChild1, diagramNode2, diagramChild2, diagramNode3, diagramChild3, diagramChild4};
		
		MockDiagramController newController = new MockDiagramController();
		
		ProjectController controller = this.createController();
		controller.createProject("projectName");
		
		Assert.assertEquals(0, this.diagramController.getSaveCalls());
		Assert.assertNull(newController.getLoadedDiagram());
		Assert.assertEquals(1, this.diagramController.getListeners().size());
		Assert.assertSame(controller, this.diagramController.getListeners().get(0));
		Assert.assertEquals(0, newController.getListeners().size());
		
		this.diagramControllerFactory.setController(newController);
		controller.changeElement(new TreePath(path));
		
		Assert.assertEquals(1, newController.getListeners().size());
		Assert.assertSame(controller, newController.getListeners().get(0));
		Assert.assertEquals(1, this.diagramController.getSaveCalls());
		Assert.assertSame(diagram3, newController.getLoadedDiagram());
		
		Assert.assertSame(newController.getView(), this.shell.getRightContent());
		
		deleteFile("projectName/Datos");
		deleteFile("projectName");
	}
	
	@Test
	public void testShouldNotUpdateRelationshipNorEntityNeitherHierarchyWhenSelectDiagramInTreeNode() {
		Diagram diagram1 = new Diagram();
		Diagram diagram2 = new Diagram();
		Diagram diagram3 = new Diagram();
		diagram1.setName("1");
		diagram2.setName("2");
		diagram3.setName("3");
		DiagramTreeNode diagramNode1 = new DiagramTreeNode(diagram1, this.projectContext);
		DiagramTreeNode diagramNode2 = new DiagramTreeNode(diagram2, this.projectContext);
		DiagramTreeNode diagramNode3 = new DiagramTreeNode(diagram3, this.projectContext);
		
		DefaultMutableTreeNode diagramChild1 = new DefaultMutableTreeNode("dato1");
		DefaultMutableTreeNode diagramChild2 = new DefaultMutableTreeNode("dato2");
		DefaultMutableTreeNode diagramChild3 = new DefaultMutableTreeNode("dato3");
		DefaultMutableTreeNode diagramChild4 = new DefaultMutableTreeNode("dato4");
		
		Object[] path = {diagramNode1, diagramChild1, diagramNode2, diagramChild2, diagramChild3, diagramChild4, diagramNode3};

		MockDiagramController newController = new MockDiagramController();
		
		ProjectController controller = this.createController();
		controller.createProject("projectName");		
		
		this.diagramControllerFactory.setController(newController);
		controller.changeElement(new TreePath(path));
		
		Assert.assertFalse(newController.updateEntityWasCalled());
		Assert.assertFalse(newController.updateRelationshipWasCalled());
		Assert.assertFalse(newController.updateHierarchyWasCalled());
		
		deleteFile("projectName/Datos");
		deleteFile("projectName");
	}
	
	@Test
	public void testShouldUpdateElementsSelectedInTreeNode() throws Exception{
		Diagram diagram1 = new Diagram();
		Diagram diagram2 = new Diagram();
		Diagram diagram3 = new Diagram();
		diagram1.setName("1");
		diagram2.setName("2");
		diagram3.setName("3");
		DiagramTreeNode diagramNode1 = new DiagramTreeNode(diagram1, this.projectContext);
		DiagramTreeNode diagramNode2 = new DiagramTreeNode(diagram2, this.projectContext);
		DiagramTreeNode diagramNode3 = new DiagramTreeNode(diagram3, this.projectContext);
		Entity entity1 = new Entity("entity1");
		Entity entity2 = new Entity("entity2");
		Relationship relationship = new Relationship(new RelationshipEntity(entity1), new RelationshipEntity(entity2));
		Hierarchy hierarchy = new Hierarchy();

		MockDiagramController newController = new MockDiagramController();
		
		ProjectController controller = this.createController();
		controller.createProject("projectName");		
		
		this.diagramControllerFactory.setController(newController);
		
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(relationship);
		
		Object[] path1 = {diagramNode1, entity1, diagramNode2, entity2, hierarchy, diagramNode3, node};
		controller.changeElement(new TreePath(path1));
		
		Assert.assertTrue(newController.updateRelationshipWasCalled());
		Assert.assertEquals(relationship, newController.getUpdatedRelationship());
		
		node = new DefaultMutableTreeNode(entity2);
		
		Object[] path2 = {diagramNode1, entity1, diagramNode2, hierarchy, diagramNode3, relationship, node};
		controller.changeElement(new TreePath(path2));
		
		Assert.assertTrue(newController.updateEntityWasCalled());
		Assert.assertEquals(entity2, newController.getUpdatedEntity());
		
		node = new DefaultMutableTreeNode(hierarchy);
		
		Object[] path3 = {diagramNode1, entity1, diagramNode2, diagramNode3, relationship, entity2, node};
		controller.changeElement(new TreePath(path3));
		
		Assert.assertTrue(newController.updateHierarchyWasCalled());
		Assert.assertEquals(hierarchy, newController.getUpdatedHierarchy());
		
		deleteFile("projectName/Datos");
		deleteFile("projectName");
	}
	
	private DefaultMutableTreeNode getNodeChildWithObject(DefaultMutableTreeNode node, String childName, Object object) {	
		Enumeration children = node.children();
		while (children.hasMoreElements()) {
		  DefaultMutableTreeNode element = (DefaultMutableTreeNode)children.nextElement();
		  if (String.class.isInstance(element.getUserObject())){
			  String value = (String)element.getUserObject();
			  if (value.equalsIgnoreCase(childName)){
				  Enumeration grandChildren = element.children();
				  while (grandChildren.hasMoreElements()) {
					  element = (DefaultMutableTreeNode)grandChildren.nextElement();
					  if (element.getUserObject() == object){
						  return element;
					  }
				  }
			  }
		  }
		}
		
		return null;
	}

	private void deleteFile(String name){
		File file = new File(name);
		file.delete();
	}
	
	private boolean fileExists(String name){
		File file = new File(name);
		return file.exists();
	}
	
	private ProjectController createController(){
		return new ProjectController(this.projectContext, this.projectView,
				this.shell, this.diagramControllerFactory, this.xmlFileManager,
				this.diagramXmlManager, this.fileSystemService, this.projectValidationService);
	}
}


