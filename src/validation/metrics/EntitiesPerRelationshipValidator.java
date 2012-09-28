package validation.metrics;

import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.List;

import validation.IValidationEntry;

import models.Diagram;
import models.Relationship;

public class EntitiesPerRelationshipValidator implements IMetricsValidator {
	@Override
	public Iterable<IValidationEntry> validate(Diagram diagram,
			Metrics metrics, int tolerance) {
		List<IValidationEntry> entries = new ArrayList<IValidationEntry>();
    	
        for (Relationship relationship : diagram.getRelationships()) {
        	int entitiesInRelationship = IterableExtensions.count(relationship.getRelationshipEntities());
        	if (!metrics.getEntitiesPerRelationship().isInRange(entitiesInRelationship, tolerance)){
				entries.add(new EntitiesPerRelationshipValidationEntry(diagram, relationship, entitiesInRelationship));
			}
		}
        
    	return entries;
	}
}
