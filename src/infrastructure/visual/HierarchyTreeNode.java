package infrastructure.visual;

import java.util.UUID;

import infrastructure.IProjectContext;

import javax.swing.tree.DefaultMutableTreeNode;

import models.Hierarchy;

public class HierarchyTreeNode extends DefaultMutableTreeNode {
	private IProjectContext projectContext;

	public HierarchyTreeNode(IProjectContext projectContext, Hierarchy hierarchy){
		super(hierarchy);
		this.projectContext = projectContext;
	}
	
	@Override
	public String toString() {
		Hierarchy hierarchy = (Hierarchy) this.userObject;
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("G:");
		builder.append(this.projectContext.getEntity(hierarchy.getGeneralEntityId()).getName());
		builder.append(" E:");
		
		for (UUID id : hierarchy.getChildren()) {
			builder.append(this.projectContext.getEntity(id));
			builder.append(";");
		}
		
		return builder.toString();
	}
}
