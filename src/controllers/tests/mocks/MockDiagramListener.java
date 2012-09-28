package controllers.tests.mocks;

import models.Diagram;
import models.Entity;
import models.Hierarchy;
import models.Relationship;
import controllers.listeners.IDiagramEventListener;

public class MockDiagramListener implements IDiagramEventListener {

	private Diagram diagram;
	private Entity entity;
	private Relationship relationship;
	private String diagramName;
	private Hierarchy hierarchy;
	
	@Override
	public void handleEntityAdded(Diagram diagram, Entity entity) {
		this.diagram = diagram;
		this.entity = entity;
	}
	
	@Override
	public void handleRelationshipAdded(Diagram diagram, Relationship relationship) {
		this.diagram = diagram;
		this.relationship = relationship;
	}
	
	public Diagram getDiagram(){
		return this.diagram;
	}
	
	public Entity getEntity(){
		return this.entity;
	}

	public Relationship getRelationship() {
		return this.relationship;
	}

	@Override
	public void handleSubDiagramCreated(Diagram diagram, String diagramName) {
		this.diagram = diagram;
		this.diagramName = diagramName;
	}
	
	@Override
	public void handleHierarchyAdded(Diagram diagram, Hierarchy hierarchy) {
		this.diagram = diagram;
		this.hierarchy = hierarchy;
	}

	public String getDiagramName() {
		return this.diagramName;
	}

	public Hierarchy getHierarchy() {
		return this.hierarchy;
	}

	@Override
	public void handleEntityUpdated(Diagram diagram, Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRelationshipUpdated(Diagram diagram,
			Relationship relantionship) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleHierarchyUpdated(Diagram diagram, Hierarchy hierarchy) {
		// TODO Auto-generated method stub
		
	}
}
