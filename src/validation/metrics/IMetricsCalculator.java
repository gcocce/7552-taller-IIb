package validation.metrics;

import models.der.Diagram;

public interface IMetricsCalculator {
	Metrics calculateMetrics(Iterable<Diagram> diagrams);
}
