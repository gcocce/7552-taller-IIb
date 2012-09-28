package validation.metrics;

import validation.IValidationEntry;
import models.Diagram;

public interface IMetricsValidator {
    public Iterable<IValidationEntry> validate(Diagram diagram, Metrics metrics, int tolerance);
}
