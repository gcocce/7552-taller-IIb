package validation.metrics;

import validation.IValidationEntry;
import validation.ValidationType;
import models.Diagram;
import models.Relationship;

public class EntitiesPerRelationshipValidationEntry implements IValidationEntry {

	private Diagram diagram;
	private Relationship relationship;
	private int entitiesInRelationship;

	public EntitiesPerRelationshipValidationEntry(Diagram diagram,
			Relationship relationship, int entitiesInRelationship) 
	{
		this.diagram = diagram;
		this.relationship = relationship;
		this.entitiesInRelationship = entitiesInRelationship;
	}

	@Override
	public String getMessage() {
		return String.format("Relationship %s has %d entities.", this.relationship.getName(), this.entitiesInRelationship);
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
