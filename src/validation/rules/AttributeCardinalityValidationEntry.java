package validation.rules;

import models.Attribute;
import models.Diagram;
import models.Entity;
import validation.IValidationEntry;
import validation.ValidationType;

public class AttributeCardinalityValidationEntry implements IValidationEntry {

	private Attribute attribute;
	private Entity entity;
	private Diagram diagram;

	public AttributeCardinalityValidationEntry(Diagram diagram, Entity entity, Attribute attribute){
		this.diagram = diagram;
		this.entity = entity;
		this.attribute = attribute;
	}
	
	@Override
	public Diagram getDiagram() {
		return this.diagram;
	}

	@Override
	public String getMessage() {
		return String.format("Attribute %s in Entity %s has cardinality (0,0).", this.attribute.getName(), this.entity.getName());
	}

	@Override
	public ValidationType getType() {
		return ValidationType.ERROR;
	}
}
