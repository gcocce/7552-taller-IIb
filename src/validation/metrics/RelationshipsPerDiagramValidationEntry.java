package validation.metrics;

import validation.IValidationEntry;
import validation.ValidationType;
import models.Diagram;

public class RelationshipsPerDiagramValidationEntry implements IValidationEntry {

	private int relationshipCount;
	private Diagram diagram;

	public RelationshipsPerDiagramValidationEntry(Diagram diagram,
			int relationshipCount) {
		this.diagram = diagram;
		this.relationshipCount = relationshipCount;
	}

	@Override
	public String getMessage() {
		return String.format("Diagram has %d relationships.", this.relationshipCount);
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
