package models;

import infrastructure.Func;
import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Relationship implements INameable {

	private List<RelationshipEntity> relationshipEntites;
	private UUID id;
	private Boolean isComposition;
	private String name;
	private AttributeCollection attributes;
	
	public Relationship() {
		this(UUID.randomUUID(), "", false);
	}
	
	public Relationship(RelationshipEntity entity1, RelationshipEntity entity2) throws Exception 
	{
		this(UUID.randomUUID(), "", false);
		if (!this.addRelationshipEntity(entity1) || !this.addRelationshipEntity(entity2))
		{
			throw new Exception();
		}
	}
	
	public Relationship(UUID id, String name, Boolean isComposition) {
		this.attributes = new AttributeCollection();
		this.relationshipEntites = new ArrayList<RelationshipEntity>();
		this.id = id;
		this.isComposition = isComposition;
		this.name = name;
	}

	public Boolean addRelationshipEntity(RelationshipEntity relationshipEntity)
	{
		for (RelationshipEntity relEntity : this.relationshipEntites)
		{
			if (relEntity.getEntityId() == relationshipEntity.getEntityId())
			{
				String role1 = relationshipEntity.getRole();
				String role2 = relEntity.getRole();
				
				if (role1 == null || role1 == "" || role2 == null || role2 == "" || role1 == role2)
				{
					return false;
				}
			}
		}
		
		this.relationshipEntites.add(relationshipEntity);
		return true;
	}

	public Iterable<RelationshipEntity> getRelationshipEntities() 
	{
		return this.relationshipEntites;
	}
	
	public int count() {
		return relationshipEntites.size();
	}
	
	public void setRelationshipEntities ( List<RelationshipEntity> relationshipEntites) {
		this.relationshipEntites = relationshipEntites;
	}
	

	public UUID getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void isComposition(Boolean isComposition) {
		this.isComposition = isComposition;
	}

	public Boolean isComposition() {
		return isComposition;
	}

	public AttributeCollection getAttributes() {
		return this.attributes; 
	}

	public void setAttributes(AttributeCollection attributes) {
		this.attributes = attributes;
	}
	
	public boolean hasWeakEntity(){
		if (this.relationshipEntites.size() != 2){
			return false;
		}
		
		Func<RelationshipEntity, Boolean, Boolean> cmp = new Func<RelationshipEntity, Boolean, Boolean>(){

			@Override
			public Boolean execute(RelationshipEntity relationshipEntity, Boolean isStrong) {
				return relationshipEntity.isStrongEntity() == isStrong;
			}
			
		};
		
		return IterableExtensions.firstOrDefault(this.relationshipEntites, cmp, true) != null;
	}
	
	public RelationshipEntity getWeakEntity(){
		Func<RelationshipEntity, Boolean, Boolean> cmp = new Func<RelationshipEntity, Boolean, Boolean>(){

			@Override
			public Boolean execute(RelationshipEntity relationshipEntity, Boolean isStrong) {
				return relationshipEntity.isStrongEntity() == isStrong;
			}
			
		};
		
		return IterableExtensions.firstOrDefault(this.relationshipEntites, cmp, false);
	}
	
	public RelationshipEntity getStrongEntity(){
		Func<RelationshipEntity, Boolean, Boolean> cmp = new Func<RelationshipEntity, Boolean, Boolean>(){

			@Override
			public Boolean execute(RelationshipEntity relationshipEntity, Boolean isStrong) {
				return relationshipEntity.isStrongEntity() == isStrong;
			}
			
		};
		
		return IterableExtensions.firstOrDefault(this.relationshipEntites, cmp, true);
	}
	
	@Override
	public String toString() {
		return this.name != null ? this.name : this.id.toString();
	}
}
