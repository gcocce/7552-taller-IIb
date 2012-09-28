package validation.metrics;

import validation.IValidationEntry;
import validation.ValidationType;
import models.Diagram;
import models.Entity;

public class AttributesPerEntityValidationEntry implements IValidationEntry {
	private Diagram diagram;
	private Entity entity;
	private int attributeCount;
	
	public AttributesPerEntityValidationEntry(Diagram diagram, Entity entity, int attributeCount){
		this.diagram = diagram;
		this.entity = entity;
		this.attributeCount = attributeCount;
	}
	
	@Override
	public String getMessage() {
		return String.format("Entity %s has %d attributes.", entity.getName(), attributeCount);
	}

	@Override
	public ValidationType getType() {
		return ValidationType.WARNING;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public Diagram getDiagram() {
		return this.diagram;
	}
}
