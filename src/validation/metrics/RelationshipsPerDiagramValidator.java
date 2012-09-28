package validation.metrics;

import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.List;

import validation.IValidationEntry;

import models.Diagram;

public class RelationshipsPerDiagramValidator implements IMetricsValidator {

	@Override
	public Iterable<IValidationEntry> validate(Diagram diagram,
			Metrics metrics, int tolerance) {
		List<IValidationEntry> entries = new ArrayList<IValidationEntry>();
		
		int relationshipCount = IterableExtensions.count(diagram.getRelationships());
		 
		if (!metrics.getRelationshipsPerDiagram().isInRange(relationshipCount, tolerance)){
			entries.add(new RelationshipsPerDiagramValidationEntry(diagram, relationshipCount));
		}
		 
		return entries;
	}
}
