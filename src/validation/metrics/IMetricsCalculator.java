package validation.metrics;

import models.Diagram;

public interface IMetricsCalculator {
	Metrics calculateMetrics(Iterable<Diagram> diagrams);
}
