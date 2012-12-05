package validation.rules;

import validation.IValidationEntry;
import models.der.Diagram;

public interface IRulesValidator {
	public Iterable<IValidationEntry> validate(Diagram diagram);
}
