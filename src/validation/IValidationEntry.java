package validation;

import models.Diagram;

public interface IValidationEntry {
	public Diagram getDiagram();
	public String getMessage();
    public ValidationType getType();
}
