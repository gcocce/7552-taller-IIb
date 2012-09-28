package controllers;

import infrastructure.Func;
import infrastructure.IFileSystemService;
import infrastructure.IProjectContext;
import infrastructure.IterableExtensions;
import infrastructure.visual.DiagramTreeNode;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;

import models.Diagram;
import models.Entity;
import models.Hierarchy;
import models.Relationship;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import persistence.IXmlFileManager;
import persistence.IXmlManager;
import validation.IProjectValidationService;
import views.IProjectView;
import application.IShell;
import controllers.factories.IDiagramControllerFactory;
import controllers.listeners.IDiagramEventListener;

public class ProjectController implements IProjectController, IDiagramEventListener {
	private static String DefaultDiagramName = "Principal";
	
	private IProjectContext projectContext;
	private IDiagramControllerFactory diagramControllerFactory;
	private IDiagramController diagramController;
	private DefaultTreeModel projectTree;
	private IProjectView projectView;
	private IShell shell;
	private DiagramTreeNode currentDiagramNode;
	private TreeNode editedNode;

	private IXmlFileManager xmlFileManager;

	private IXmlManager<Diagram> diagramXmlManager;

	private IFileSystemService fileSystemService;

	private IProjectValidationService validationService;
	
	public ProjectController(IProjectContext projectContext, IProjectView projectView, 
			IShell shell, IDiagramControllerFactory diagramControllerFactory,
			IXmlFileManager xmlFileManager, IXmlManager<Diagram> diagramXmlManager, 
			IFileSystemService fileSystemService, IProjectValidationService validationService) {
		this.projectContext = projectContext;
		this.diagramControllerFactory = diagramControllerFactory;
		this.shell = shell;
		this.projectView = projectView;
		this.projectView.setController(this);
		this.xmlFileManager = xmlFileManager;
		this.diagramXmlManager = diagramXmlManager;
		this.fileSystemService = fileSystemService;
		this.validationService = validationService;
	}

	public void createProject(String projectName) {
		this.projectContext.clearContextDiagrams();
		this.projectContext.clearProjectDiagrams();
		this.projectContext.setName(projectName);
		new File(this.projectContext.getDataDirectory()).mkdirs();
		
		this.diagramController = this.diagramControllerFactory.create();
		this.diagramController.getDiagram().setName(DefaultDiagramName);
		this.diagramController.addListener(this);
		
		Diagram mainDiagram = this.diagramController.getDiagram();
		
		this.currentDiagramNode = new DiagramTreeNode(this.diagramController.getDiagram(), this.projectContext);
		this.projectTree = new DefaultTreeModel(this.currentDiagramNode);
		
		this.projectContext.addContextDiagram(mainDiagram);
		this.projectContext.addProjectDiagram(mainDiagram);
		
		this.shell.setRightContent(this.diagramController.getView());
		this.shell.activateFullSize();
	}

	public IDiagramController getCurrentDiagramController() {
		return this.diagramController;
	}

	public TreeModel getProjectTree() {
		return this.projectTree;
	}

	@Override
	public IProjectView getView() {
		return this.projectView;
	}

	@Override
	public void handleEntityAdded(Diagram diagram, Entity entity) {
		this.currentDiagramNode.addEntity(entity, this.projectTree);
	}

	@Override
	public void handleRelationshipAdded(Diagram diagram, Relationship relationship) {
		this.currentDiagramNode.addRelationship(relationship, this.projectTree);
	}

	@Override
	public void handleSubDiagramCreated(Diagram diagram, String diagramName) {
		Diagram parentDiagram = this.diagramController.getDiagram();
		
		IDiagramController childDiagramController = this.diagramControllerFactory.create();
		Diagram childDiagram = childDiagramController.getDiagram();
		
		childDiagram.setName(diagramName);
		
		parentDiagram.addSubDiagram(childDiagram);
		try {
			this.diagramController.save();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		DiagramTreeNode childDiagramNode = this.currentDiagramNode.addSubdiagram(childDiagram, this.projectTree);
		
		this.projectContext.addContextDiagram(childDiagram);
		this.projectContext.addProjectDiagram(childDiagram);
		
		this.shell.setRightContent(childDiagramController.getView());
		this.diagramController = childDiagramController;
		
		try {
			this.diagramController.save();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		this.diagramController.addListener(this);
		this.currentDiagramNode = childDiagramNode;
	}

	@Override
	public void changeElement(TreePath treePath) {
		if (treePath == null)
			return;
		this.projectContext.clearContextDiagrams();
		Diagram diagramToLoad = null;
		for (Object o : treePath.getPath()) {
			if (o instanceof DiagramTreeNode) {
				DiagramTreeNode node = (DiagramTreeNode) o;
				Diagram diagram = (Diagram) node.getUserObject();
				diagramToLoad = diagram;
				this.currentDiagramNode = node;
				this.projectContext.addContextDiagram(diagram);
			}
		}
		
		IDiagramController newController = this.diagramControllerFactory.create();
		
		try {
			this.diagramController.save();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		this.diagramController = newController;
		this.diagramController.addListener(this);
		this.diagramController.load(diagramToLoad);
		this.shell.setRightContent(this.diagramController.getView());
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
		Object o = node.getUserObject();
		
		if (o instanceof Entity) {
			this.diagramController.updateEntity((Entity) o);
			this.editedNode = node;
		}
		else if (o instanceof Relationship) {
			this.diagramController.updateRelationship((Relationship) o);
			this.editedNode = node;
		}
		else if (o instanceof Hierarchy) {
			this.diagramController.updateHierarchy((Hierarchy) o);
			this.editedNode = node;
		}
	}

	@Override
	public void deleteElement(TreePath treePath) {
		if (treePath == null)
			return;
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
		Object o = node.getUserObject();
		
		if (o instanceof Entity) {
			if (this.diagramController.deleteEntity((Entity) o))
				this.projectTree.removeNodeFromParent(node);
		}
		else if (o instanceof Relationship) {
			if (this.diagramController.deleteRelationship((Relationship) o))
				this.projectTree.removeNodeFromParent(node);
		}
		else if (o instanceof Hierarchy) {
			if (this.diagramController.deleteHierarchy((Hierarchy) o))
				this.projectTree.removeNodeFromParent(node);
		}
	}
	
	@Override
	public boolean openProject(String projectName) throws Exception {
		this.projectContext.clearContextDiagrams();
		this.projectContext.clearProjectDiagrams();
		
		this.projectContext.setName(projectName);
		
		if (!this.fileSystemService.exists(this.projectContext.getDataDirectory(), DefaultDiagramName)) {
			return false;
		}
		this.loadDiagram(DefaultDiagramName, null);
		this.diagramController = this.diagramControllerFactory.create();
		this.diagramController.addListener(this);
		
		this.diagramController.load(this.projectContext.getContextDiagram(DefaultDiagramName));
		
		this.shell.setRightContent(this.diagramController.getView());
		this.shell.activateFullSize();
		return true;
	}
	
	private void loadDiagram(String diagramName, DiagramTreeNode parentTreeNode) throws Exception{
		Document document = this.xmlFileManager.read(this.projectContext.getDataDirectory() + "/" + diagramName + "-comp");
		Element documentElement = document.getDocumentElement();
		Diagram diagram = this.diagramXmlManager.getItemFromXmlElement(documentElement);
		diagram.setName(diagramName);
		
		DiagramTreeNode currentTreeNode;
		
		if (diagramName.equalsIgnoreCase(DefaultDiagramName)){
			this.projectContext.addContextDiagram(diagram);
			currentTreeNode = new DiagramTreeNode(diagram, this.projectContext);
			this.currentDiagramNode = currentTreeNode;
			this.projectTree = new DefaultTreeModel(this.currentDiagramNode);
		}
		else
		{
			currentTreeNode = parentTreeNode.addSubdiagram(diagram, this.projectTree);
		}
		
		this.projectContext.addProjectDiagram(diagram);
		
		for (String childDiagramName : diagram.getSubDiagramNames()) {
			this.loadDiagram(childDiagramName, currentTreeNode);
		}
	}

	@Override
	public void handleHierarchyAdded(Diagram diagram, Hierarchy hierarchy) {
		this.currentDiagramNode.addHierarchy(hierarchy, this.projectTree);
	}

	@Override
	public void handleEntityUpdated(Diagram diagram, Entity entity) {
		Func<TreeNode, Boolean, Boolean> cmp = new Func<TreeNode, Boolean, Boolean>(){

			@Override
			public Boolean execute(TreeNode node, Boolean notUsed) {
				return node instanceof DiagramTreeNode;
			}
		};
		
		Iterable<TreeNode> ancestors = IterableExtensions.where(
				IterableExtensions.getIterableOf(this.projectTree.getPathToRoot(this.currentDiagramNode)),
				cmp,
				false);
		Iterable<DiagramTreeNode> descendants = this.getDiagramTreeNodeDescendants(this.currentDiagramNode);
		
		for (DiagramTreeNode diagramTreeNode : descendants) {
			this.updateEntityInDiagram(diagramTreeNode.getDiagram(), entity);
		}
		
		for (TreeNode treeNode : ancestors) {
			DiagramTreeNode diagramTreeNode = (DiagramTreeNode)treeNode;
			this.updateEntityInDiagram(diagramTreeNode.getDiagram(), entity);
		}
		
		this.projectTree.nodeChanged(this.editedNode);
		this.editedNode = null;
	}

	private void updateEntityInDiagram(Diagram diagram, Entity entity) {
		IDiagramController controller = this.diagramControllerFactory.create();
		controller.load(diagram);
		controller.deleteEntityPeripherals(entity);
		controller.handleCreatedEvent(entity);
		try {
			controller.save();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private Iterable<DiagramTreeNode> getDiagramTreeNodeDescendants(
			TreeNode currentNode) {
		List<DiagramTreeNode> diagramTreeNodes = new ArrayList<DiagramTreeNode>();
		
		int childCount = currentNode.getChildCount();
		for (int i = 0; i < childCount; i++) {
			TreeNode node = currentNode.getChildAt(i);
			
			if (node instanceof DiagramTreeNode){
				diagramTreeNodes.add((DiagramTreeNode) node);
			}
			
			for (DiagramTreeNode diagramTreeNode : this.getDiagramTreeNodeDescendants(node)) {
				diagramTreeNodes.add(diagramTreeNode);
			}
		}		
		
		return diagramTreeNodes;
	}

	@Override
	public void handleRelationshipUpdated(Diagram diagram,
			Relationship relantionship) {
		this.projectTree.nodeChanged(this.editedNode);
		this.editedNode = null;
	}

	@Override
	public void handleHierarchyUpdated(Diagram diagram, Hierarchy hierarchy) {
		this.projectTree.nodeChanged(this.editedNode);
		this.editedNode = null;
	}

	@Override
	public void validateProject(int toleranceLevel) {
		String reportHtml = this.validationService.generateGlobalReport(this.projectContext.getName(), this.projectContext.getProjectDiagrams(), toleranceLevel);
		Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String reportName = this.projectContext.getDataDirectory() + "/" + this.projectContext.getName() + "_" + sdf.toString() + ".html";
		this.fileSystemService.save(reportName, reportHtml);
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open(new File(reportName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
