package validation.rules;

import models.Diagram;
import validation.IValidationEntry;
import validation.ValidationType;

public class DiagramMustHaveEntitiesValidationEntry implements IValidationEntry {

	private Diagram diagram;

	public DiagramMustHaveEntitiesValidationEntry(Diagram diagram){
		this.diagram = diagram;
	}
	
	@Override
	public Diagram getDiagram() {
		return this.diagram;
	}

	@Override
	public String getMessage() {
		return "Diagram does not have entities.";
	}

	@Override
	public ValidationType getType() {
		return ValidationType.ERROR;
	}

}
