package models.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public class DomainDiagram implements Iterable<DomainDiagram> {
	private UUID id;

	private List<DomainClass> classes;
	private List<DomainRelationship> relationships;
	
	private List<DomainDiagram> subDiagrams;
	private String name;
	private List<String> subDiagramNames;

	public DomainDiagram() {
		this(UUID.randomUUID());	
	}
	
	public DomainDiagram(UUID id)	{
		this.id = id;
		classes = new ArrayList<DomainClass>();
		relationships = new ArrayList<DomainRelationship>();
		subDiagrams = new ArrayList<DomainDiagram>();
		
	}

	@Override
	public Iterator<DomainDiagram> iterator() {
		return subDiagrams.iterator();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public List<DomainDiagram> getSubDiagrams() {
		return subDiagrams;
	}

	public List<String> getSubDiagramNames() {
		return subDiagramNames;
	}

	public List<DomainClass> getDomainClasses() {
		return classes;
	}
	
	public List<DomainRelationship> getDomainRelationships(){
		return relationships;
	}
	
	public void addDomainClass(DomainClass clase){
		classes.add(clase);		
	}
	
	public void addDomainRelationship(DomainRelationship relationship){
		relationships.add(relationship);
	}

	public void setRelationships(
			List<DomainRelationship> domainRelationships) {
		this.relationships = domainRelationships;
		
		
	}

	public void setClasses(List<DomainClass> domainClasses) {
	
		this.classes = domainClasses;
		
	}
	
}
