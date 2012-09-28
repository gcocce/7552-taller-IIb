package validation.metrics;

import validation.IValidationEntry;
import validation.ValidationType;
import models.Diagram;

public class EntitiesPerDiagramValidationEntry implements IValidationEntry {

	private Diagram diagram;
	private int entityCount;

	public EntitiesPerDiagramValidationEntry(Diagram diagram, int entityCount){
		this.diagram = diagram;
		this.entityCount = entityCount;
	}
	
	@Override
	public String getMessage() {
		return String.format("Diagram has %d entities.", this.entityCount);
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
