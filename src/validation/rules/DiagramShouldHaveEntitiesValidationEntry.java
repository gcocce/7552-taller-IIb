package validation.rules;

import models.Diagram;
import validation.IValidationEntry;
import validation.ValidationType;

public class DiagramShouldHaveEntitiesValidationEntry implements
		IValidationEntry {

	private Diagram diagram;

	public DiagramShouldHaveEntitiesValidationEntry(Diagram diagram) {
		this.diagram = diagram;
	}

	@Override
	public Diagram getDiagram() {
		return this.diagram;
	}

	@Override
	public String getMessage() {
		return "Diagram does not have relationships";
	}

	@Override
	public ValidationType getType() {
		return ValidationType.WARNING;
	}

}
