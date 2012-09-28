package controllers.tests.mocks;

import models.*;

import infrastructure.IProjectContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import controllers.listeners.IRelationshipEventListener;



public class MockProjectContext implements IProjectContext,IRelationshipEventListener {

	private static String SubFolder = "Datos";
    private Iterable<Entity> entityCollection;
    private Iterable<INameable> attributes;
    private List<Diagram> contextDiagrams;
    private List<Diagram> globalDiagrams;

    private List<Relationship> relationships;

	private String name;
	
	public MockProjectContext(){
		this.contextDiagrams = new ArrayList<Diagram>();
		this.globalDiagrams = new ArrayList<Diagram>();
	}

    @Override
    public Iterable<Entity> getAllEntities(Entity entityToExclude) {
        return this.entityCollection;
    }

    public void setEntityCollection(Iterable<Entity> entityCollection){
        this.entityCollection = entityCollection;
    }

    public void setPossibleAttributes(Iterable<INameable> attributeIterable){
        this.attributes = attributeIterable;
    }

	public void setRelationshipCollection(List<Relationship> relationships) {
		this.relationships= relationships;
	}

	public List<Relationship> getRelationshipCollection() {
		return relationships;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getDataDirectory() {
		if (this.name != null){
			return this.name + "/" + SubFolder;
		}
		
		return null;
	}


	@Override
	public void handleCreatedEvent(Relationship relationship) {
		this.relationships.add(relationship);
		
	}

	@Override
	public Entity getEntity(UUID entityId) {
		for (Diagram diagram : this.globalDiagrams)
			for (Entity entity : diagram.getEntities())
				if (entity.getId().equals(entityId))
					return entity;
		return null;
	}


	@Override
	public void addContextDiagram(Diagram diagram) {
		this.contextDiagrams.add(diagram);
	}

	@Override
	public void addProjectDiagram(Diagram diagram) {
		this.globalDiagrams.add(diagram);
	}

	@Override
	public void clearContextDiagrams() {		
		this.contextDiagrams.clear();
	}

	@Override
	public Iterable<Hierarchy> getAllHierarchies() {
		return null;
	}

	@Override
	public Iterable<Hierarchy> getContextHierarchies() {
		return null;
	}



	@Override
	public Hierarchy getHierarchy(UUID id) {
		return null;
	}
	
	public List<Diagram> getContextDiagrams(){
		return this.contextDiagrams;
	}
	
	public List<Diagram> getGlobalDiagrams(){
		return this.globalDiagrams;
	}

	@Override
	public Diagram getContextDiagram(String diagramName) {
		Diagram diagram = new Diagram();
		diagram.setName(diagramName);
		return diagram;
	}

	@Override
	public Iterable<Entity> getContextEntities() {
		return null;
	}

	@Override
	public Iterable<Entity> getAllEntities() {
		return null;
	}

	@Override
	public Iterable<Entity> getFamilyEntities(Entity entityToExclude) {
		return null;
	}

	@Override
	public Iterable<Hierarchy> getFamilyHierarchies() {
		return null;
	}

	@Override
	public Iterable<Entity> getFamilyEntities() {
		return null;
	}

	@Override
	public void clearProjectDiagrams() {
		this.globalDiagrams.clear();
	}

	@Override
	public Iterable<Diagram> getProjectDiagrams() {
		return null;
	}
}
