package infrastructure.visual;

import infrastructure.IProjectContext;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import models.domain.DomainClass;
import models.domain.DomainDiagram;
import models.domain.DomainRelationship;

public class DomainDiagramTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -5605938989721186879L;
	
	private static String SubDiagramsNodeName = "Sub-Diagrams";
	private static String ClassesNodeName = "Classes";
	private static String RELATIONSHIPS_NODE_NAME = "Relationships";

	private DefaultMutableTreeNode subdiagramsNode;

	private DefaultMutableTreeNode classesNode;
	
	private DefaultMutableTreeNode relationshipsNode;

	private DomainDiagram diagram;

	private IProjectContext projectContext;
	
	public DomainDiagramTreeNode(DomainDiagram diagram, IProjectContext projectContext) {
		super(diagram);
		this.projectContext = projectContext;
		this.diagram = diagram;
		addChildFolders();
		populateClasses();
		populateRelationships();
	}
	
	private void populateRelationships() {
		for (DomainRelationship domainRelationship : this.diagram.getDomainRelationships()) {
			DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(domainRelationship);
			this.relationshipsNode.add(rootNode);
			//rootNode.add(new DefaultMutableTreeNode(domainRelationship.getRelatedClass1()));
			//rootNode.add(new DefaultMutableTreeNode(domainRelationship.getRelatedClass2()));

		}
		
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
		relationshipsNode = new DefaultMutableTreeNode(RELATIONSHIPS_NODE_NAME);
		add(this.classesNode);
		add(this.relationshipsNode);
		add(this.subdiagramsNode);
	}
}
