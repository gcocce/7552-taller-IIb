package validation.rules;

import models.Diagram;
import models.Entity;
import validation.IValidationEntry;
import validation.ValidationType;

public class EntityTypeValidationEntry implements IValidationEntry {

	private Entity entity;
	private Diagram diagram;

	public EntityTypeValidationEntry(Diagram diagram, Entity entity) {
		this.diagram = diagram;
		this.entity = entity;
	}

	@Override
	public Diagram getDiagram() {
		return this.diagram;
	}

	@Override
	public String getMessage() {
		return String.format("Entity %s does not have a type.", this.entity.getName());
	}

	@Override
	public ValidationType getType() {
		return ValidationType.ERROR;
	}

}
