package infrastructure.visual;

import infrastructure.IProjectContext;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import models.domain.DomainClass;
import models.domain.DomainDiagram;

public class DomainDiagramTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -5605938989721186879L;
	
	private static String SubDiagramsNodeName = "Sub-Diagrams";
	private static String ClassesNodeName = "Classes";

	private DefaultMutableTreeNode subdiagramsNode;

	private DefaultMutableTreeNode classesNode;

	private DomainDiagram diagram;

	private IProjectContext projectContext;
	
	public DomainDiagramTreeNode(DomainDiagram diagram, IProjectContext projectContext) {
		super(diagram);
		this.projectContext = projectContext;
		this.diagram = diagram;
		addChildFolders();
		populateClasses();
	}
	
	private void populateClasses() {
		for (DomainClass domainClass : this.diagram.getDomainClasses()) {
			this.classesNode.add(new DefaultMutableTreeNode(domainClass));
		}
	}
	
	public DomainDiagram getDiagram(){
		return diagram;
	}
	
	/**
	 * TODO: See if we need this
	 */
	public void addClass(DomainClass domainClass, DefaultTreeModel tree){
		classesNode.add(new DefaultMutableTreeNode(domainClass));
		int index = classesNode.getChildCount() - 1;
		tree.nodesWereInserted(this.classesNode, new int[]{index});
	}
	
	public DomainDiagramTreeNode addSubdiagram(DomainDiagram diagram, DefaultTreeModel tree){
		DomainDiagramTreeNode diagramNode = new DomainDiagramTreeNode(diagram, this.projectContext);
		this.subdiagramsNode.add(diagramNode);
		int index = this.subdiagramsNode.getChildCount() - 1;
		tree.nodesWereInserted(this.subdiagramsNode, new int[]{index});
		return diagramNode;
	}

	private void addChildFolders() {
		classesNode = new DefaultMutableTreeNode(ClassesNodeName);
		subdiagramsNode = new DefaultMutableTreeNode(SubDiagramsNodeName);
		add(this.classesNode);
		add(this.subdiagramsNode);
	}
}
