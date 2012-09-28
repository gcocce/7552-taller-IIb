package validation.metrics;

import models.Diagram;
import models.Hierarchy;
import validation.IValidationEntry;
import validation.ValidationType;

public class EntitiesPerHierarchyValidationEntry implements IValidationEntry {

	private Diagram diagram;
	private Hierarchy hierarchy;
	private int entitiesInHierarchy;

	public EntitiesPerHierarchyValidationEntry(Diagram diagram,
			Hierarchy hierarchy, int entitiesInHierarchy) 
	{
		this.diagram = diagram;
		this.hierarchy = hierarchy;
		this.entitiesInHierarchy = entitiesInHierarchy;
	}

	@Override
	public String getMessage() {
		return String.format("Hierarchy %s has %d entities.", this.hierarchy.toString(), this.entitiesInHierarchy);
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
