package models;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class RelationshipCollection extends ModelCollection<Relationship>{

	protected List<Relationship> relationships;

	public RelationshipCollection() {
		this.relationships = new ArrayList<Relationship>();
	}

	public int count() {
		return this.relationships.size();
	}

	
	public void add(Relationship relationship) throws Exception {
		if (relationship.getName() == null || relationship.getName().equals("")) 
			throw new Exception("Relationships should have a name");
		
		for (Relationship aux : relationships) {
			if (aux.getName().equals(relationship.getName()))
				throw new Exception ("Relationships should not have repeated names");
		}
		relationships.add(relationship);
	}
	
	public Relationship get(String name) {
		for (Relationship rel : relationships) 
			if (rel.getName().equals(name)) return rel;
		return null;
	}
	
	public Relationship get (UUID id) {
		for (Relationship rel : relationships) 
			if (rel.getId().equals(id)) return rel;
		return null;
	}
	
			
	public void remove (Relationship relationship) {
		relationships.remove(relationship);
	}

//	public void remove(String relationshipName) {
//		for (Relationship rel : relationships ) {
//			if (rel.getName().equals(relationshipName) )relationships.remove(rel);
//		}
//	}
	
	public void remove (UUID relationshipID) {
		for (Relationship rel : relationships ) {
			if (rel.getId() == relationshipID )relationships.remove(rel);
		}
	}

	public Iterator<Relationship> iterator(){
		return this.relationships.iterator();
	}
	
	public List<Relationship> getRelationships() {
		return relationships;
	}
	
	public void setRelationships (List<Relationship> relationships) {
		this.relationships = relationships;
	}

	@Override
	protected Relationship createItemInstance(String itemName) {
		Relationship relationship = new Relationship();
		relationship.setName(itemName);
		return relationship;
	}
	
}
