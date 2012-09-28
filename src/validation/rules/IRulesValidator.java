package validation.rules;

import validation.IValidationEntry;
import models.Diagram;

public interface IRulesValidator {
	public Iterable<IValidationEntry> validate(Diagram diagram);
}
