package models;


import infrastructure.Func;
import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public class Diagram implements Iterable<Diagram>{

	public enum DiagramState {
		NotValidated,
		Invalid,
		Valid
	}

	private UUID id;
	private EntityCollection entities;
	private RelationshipCollection relationships;
	
	private HierarchyCollection hierarchies;
	private List<Diagram> subDiagrams;
	private String name;
	private List<String> subDiagramNames;
	private DiagramState state;
	
	public Diagram()
	{
		this(UUID.randomUUID(), new EntityCollection(), 
				new RelationshipCollection(), new HierarchyCollection(), 
				new ArrayList<Diagram>(), DiagramState.NotValidated);
	}

	public Diagram(UUID id)
	{
		this(id, new EntityCollection(), new RelationshipCollection(), 
				new HierarchyCollection(), new ArrayList<Diagram>(), DiagramState.NotValidated);
	}
	
	public Diagram(UUID id, DiagramState state)
	{
		this(id, new EntityCollection(), new RelationshipCollection(), 
				new HierarchyCollection(), new ArrayList<Diagram>(), state);
	}
	
	public Diagram(EntityCollection entities,
			RelationshipCollection relationships,
			HierarchyCollection hierarchies, List<Diagram> subDiagrams)
	{
		this(UUID.randomUUID(), entities, relationships, hierarchies, 
				subDiagrams, DiagramState.NotValidated);
	}
	
	public Diagram(UUID id, EntityCollection entities,
			RelationshipCollection relationships,
			HierarchyCollection hierarchies, List<Diagram> subDiagram, DiagramState state) 
	{
		this.id = id;
		this.subDiagramNames = new ArrayList<String>();
		this.state = state;
		this.setEntities(entities);
		this.setRelationships(relationships);
		this.setHierarchies(hierarchies);
		this.setSubDiagrams(subDiagram);
	}

	public void setEntities(EntityCollection entities) {
		this.entities = entities;
	}

	public EntityCollection getEntities() {
		return entities;
	}

	
	public RelationshipCollection getRelationships() {
		return relationships;
	}
	
	public void setRelationships(RelationshipCollection relationships)
	{
		this.relationships = relationships;
	}

	public void setHierarchies(HierarchyCollection hierarchies) {
		this.hierarchies = hierarchies;
	}

	public HierarchyCollection getHierarchies() {
		return hierarchies;
	}

	public void setSubDiagrams(List<Diagram> subDiagrams) {
		this.subDiagrams = subDiagrams;
	}

	public List<Diagram> getSubDiagrams() {
		return subDiagrams;
	}

	public UUID getId() {
		return id;
	}
	
	public Relationship getRelationship(UUID relationshipId) {
		return this.relationships.get(relationshipId);
	}


	public boolean existsRelationship(UUID id) {
		return (this.relationships.get(id) != null);
	}

	public void removeRelationship(UUID id) throws Exception {
		if (existsRelationship(id))
		{
			this.relationships.remove(this.getRelationship(id));
		}
		else
		{
			throw new Exception("Do not exists a relationship with id" + id);
		}
	}
	
	public void addSubDiagram(Diagram subDiagram) {
		this.subDiagrams.add(subDiagram);
	}

	public Diagram getSubDiagram(UUID id) {
		return IterableExtensions.firstOrDefault(this.subDiagrams, 
				new SubDiagramsCompFunc(), id);
	}

	public boolean existsSubDiagram(UUID id) 
	{
		return IterableExtensions.firstOrDefault(this.subDiagrams, 
				new SubDiagramsCompFunc(), id) != null;
	}

	public void removeSubDiagram(UUID id) throws Exception{		
		if (existsSubDiagram(id))
		{
			this.subDiagrams.remove(this.getSubDiagram(id));
		}
		else
		{
			throw new Exception("Do not exists an SubDiagram with id" + id);
		}
	}
	
//	private class RelationshipsCompFunc extends 
//	Func<Relationship, UUID, Boolean>
//	{
//		@Override
//		public Boolean execute(Relationship relationship, UUID id) 
//		{
//			return relationship.getId().equals(id);
//		}
//		
//	}
				
	private class SubDiagramsCompFunc extends
	Func<Diagram, UUID, Boolean>
	{
		@Override
		public Boolean execute(Diagram diagram, UUID id)
		{	
			return diagram.getId().equals(id);
		}
	}
	
	@Override
	public Iterator<Diagram> iterator() {
		return this.subDiagrams.iterator();
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

	public List<String> getSubDiagramNames() {
		return this.subDiagramNames;
	}

	public DiagramState getState() {
		return state;
	}

	public void isNotValidated() {
		this.state = DiagramState.NotValidated;
	}
	
	public void isInvalid() {
		this.state = DiagramState.Invalid;
	}
	
	public void isValid() {
		this.state = DiagramState.Valid;
	}
}
