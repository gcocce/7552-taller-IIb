package validation.rules;

import models.Diagram;
import models.Relationship;
import validation.IValidationEntry;
import validation.ValidationType;

public class RelationshipCardinalityValidationEntry implements IValidationEntry {

	private Diagram diagram;
	private Relationship relationship;

	public RelationshipCardinalityValidationEntry(Diagram diagram, Relationship relationship) {
		this.diagram = diagram;
		this.relationship = relationship;
	}

	@Override
	public Diagram getDiagram() {
		return this.diagram;
	}

	@Override
	public String getMessage() {
		return String.format("Relationship %s in has cardinality (0,0) for one of its entities.", this.relationship.getName());
	}

	@Override
	public ValidationType getType() {
		return ValidationType.ERROR;
	}

}
