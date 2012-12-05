package validation;

import models.der.Diagram;

public interface IValidationEntry {
	public Diagram getDiagram();
	public String getMessage();
    public ValidationType getType();
}
