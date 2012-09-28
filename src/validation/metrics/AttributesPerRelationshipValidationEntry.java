package validation.metrics;

import validation.IValidationEntry;
import validation.ValidationType;
import models.Diagram;
import models.Relationship;

public class AttributesPerRelationshipValidationEntry implements IValidationEntry {
	private Diagram diagram;
	private Relationship relationship;
	private int attributeCount;
	
	public AttributesPerRelationshipValidationEntry(Diagram diagram, Relationship relationship, int attributeCount){
		this.diagram = diagram;
		this.relationship = relationship;
		this.attributeCount = attributeCount;
	}
	
	@Override
	public String getMessage() {
		return String.format("Relationship %s has %d attributes.", this.relationship.getName(), attributeCount);
	}

	@Override
	public ValidationType getType() {
		return ValidationType.WARNING;
	}

	@Override
	public Diagram getDiagram() {
		return this.diagram;
	}
}
