package infrastructure.visual;

import infrastructure.IProjectContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import models.Diagram;
import models.Hierarchy;
import models.Relationship;
import models.Entity;

public class DiagramTreeNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5605938989721186879L;
	
	private static String SubDiagramsNodeName = "Sub-Diagrams";
	private static String EntitiesNodeName = "Entities";
	private static String RelationshipsNodeName = "Relationships";
	private static String HierarchiesNodeName = "Hierarchies";

	private DefaultMutableTreeNode subdiagramsNode;

	private DefaultMutableTreeNode entitiesNode;

	private DefaultMutableTreeNode relationshipsNode;

	private DefaultMutableTreeNode hierarchiesNode;

	private Diagram diagram;

	private IProjectContext projectContext;
	
	public DiagramTreeNode(Diagram diagram, IProjectContext projectContext) {
		super(diagram);
		this.projectContext = projectContext;
		this.diagram = diagram;
		this.addChildFolders();
		this.populateEntities();
		this.populateRelationships();
		this.populateHierarchies();
	}
	
	private void populateEntities() {
		for (Entity entity : this.diagram.getEntities()) {
			this.entitiesNode.add(new DefaultMutableTreeNode(entity));
		}
	}
	
	public Diagram getDiagram(){
		return this.diagram;
	}
	
	private void populateRelationships() {
		for (Relationship relationship : this.diagram.getRelationships()) {
			this.relationshipsNode.add(new DefaultMutableTreeNode(relationship));
		}
	}
	
	private void populateHierarchies() {
		for (Hierarchy hierarchy : this.diagram.getHierarchies()) {
			this.hierarchiesNode.add(new HierarchyTreeNode(this.projectContext, hierarchy));
		}
	}

	public void addEntity(Entity entity, DefaultTreeModel tree){
		this.entitiesNode.add(new DefaultMutableTreeNode(entity));
		int index = this.entitiesNode.getChildCount() - 1;
		tree.nodesWereInserted(this.entitiesNode, new int[]{index});
	}
	
	public void addRelationship(Relationship relationship, DefaultTreeModel tree){
		this.relationshipsNode.add(new DefaultMutableTreeNode(relationship));
		int index = this.relationshipsNode.getChildCount() - 1;
		tree.nodesWereInserted(this.relationshipsNode, new int[]{index});
	}
	
	public void addHierarchy(Hierarchy hierarchy, DefaultTreeModel tree){
		this.hierarchiesNode.add(new HierarchyTreeNode(this.projectContext, hierarchy));
		int index = this.hierarchiesNode.getChildCount() - 1;
		tree.nodesWereInserted(this.hierarchiesNode, new int[]{index});
	}
	
	public DiagramTreeNode addSubdiagram(Diagram diagram, DefaultTreeModel tree){
		DiagramTreeNode diagramNode = new DiagramTreeNode(diagram, this.projectContext);
		this.subdiagramsNode.add(diagramNode);
		int index = this.subdiagramsNode.getChildCount() - 1;
		tree.nodesWereInserted(this.subdiagramsNode, new int[]{index});
		return diagramNode;
	}

	private void addChildFolders() {
		this.entitiesNode = new DefaultMutableTreeNode(EntitiesNodeName);
		this.relationshipsNode = new DefaultMutableTreeNode(RelationshipsNodeName);
		this.hierarchiesNode = new DefaultMutableTreeNode(HierarchiesNodeName);
		this.subdiagramsNode = new DefaultMutableTreeNode(SubDiagramsNodeName);
		this.add(this.entitiesNode);
		this.add(this.relationshipsNode);
		this.add(this.hierarchiesNode);
		this.add(this.subdiagramsNode);
	}
}
