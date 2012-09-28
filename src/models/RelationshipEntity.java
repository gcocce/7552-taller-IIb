package models;

import java.util.UUID;

public class RelationshipEntity {
	private Cardinality cardinality;
	private String role;
	private UUID entityId;
	private boolean isStrongEntity;
	
	public RelationshipEntity(Entity entity) {
		super();
		this.entityId = entity.getId();
		try {
			cardinality = new Cardinality(0, 0);
		}catch (Exception e) {}
		role = new String ("");
	}

	public RelationshipEntity(Entity entity, Cardinality cardinality,
			String role) {		
		this(entity.getId(), cardinality, role);
	}
	
	public RelationshipEntity(Entity entity, Cardinality cardinality,
			String role, boolean isStrong) {
		this(entity.getId(), cardinality, role,isStrong);
	}
	

	public RelationshipEntity(UUID id, Cardinality cardinality, String role) {
		super();
		this.entityId = id;
		this.cardinality = cardinality;
		this.role = role;
		isStrongEntity = false;
	}
	
	public RelationshipEntity(UUID id, Cardinality cardinality, String role, boolean isStrong) {
		this(id,cardinality,role);
		isStrongEntity = isStrong;
	}

	public boolean isStrongEntity() {
		return isStrongEntity;
	}

	public void setStrongEntity(boolean isStrongEntity) {
		this.isStrongEntity = isStrongEntity;
	}

	public void setCardinality(Cardinality cardinality) {
		this.cardinality = cardinality;
	}

	public Cardinality getCardinality() {
		return this.cardinality;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return this.role;
	}

	public UUID getEntityId() {
		return this.entityId;
	}
	
}
