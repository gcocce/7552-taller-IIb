package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class DomainDiagram implements Iterable<DomainDiagram> {
	private UUID id;
// TODO: This should be a Class Collection
//	private EntityCollection entities;
	
	private List<DomainDiagram> subDiagrams;
	private String name;
	private List<String> subDiagramNames;

	public DomainDiagram() {
		this(UUID.randomUUID());
	}

	public DomainDiagram(UUID id)	{
		this.id = id;
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
		return new ArrayList<DomainClass>();
	}
}
