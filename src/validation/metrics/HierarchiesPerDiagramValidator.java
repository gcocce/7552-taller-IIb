package validation.metrics;

import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.List;

import validation.IValidationEntry;

import models.Diagram;

public class HierarchiesPerDiagramValidator implements IMetricsValidator {
	@Override
	public Iterable<IValidationEntry> validate(Diagram diagram,
			Metrics metrics, int tolerance) {
		List<IValidationEntry> entries = new ArrayList<IValidationEntry>();
		
		 int hierarchyCount = IterableExtensions.count(diagram.getHierarchies());
		 
		 if (!metrics.getHierarchiesPerDiagram().isInRange(hierarchyCount, tolerance)){
				entries.add(new HierarchiesPerDiagramValidationEntry(diagram, hierarchyCount));
		 }
		 
		 return entries;
	}
}
