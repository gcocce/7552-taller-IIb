package validation.metrics;

import validation.IValidationEntry;
import validation.ValidationType;
import models.Diagram;

public class HierarchiesPerDiagramValidationEntry implements IValidationEntry {

	private Diagram diagram;
	private int hierarchyCount;

	public HierarchiesPerDiagramValidationEntry(Diagram diagram, int hierarchyCount){
		this.diagram = diagram;
		this.hierarchyCount = hierarchyCount;
	}
	
	@Override
	public String getMessage() {
		return String.format("Diagram has %d hierarchies.", this.hierarchyCount);
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
