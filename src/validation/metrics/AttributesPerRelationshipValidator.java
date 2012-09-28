package validation.metrics;

import java.util.ArrayList;
import java.util.List;

import validation.IValidationEntry;

import models.Diagram;
import models.Relationship;

public class AttributesPerRelationshipValidator implements IMetricsValidator {
	@Override
    public Iterable<IValidationEntry> validate(Diagram diagram, Metrics metrics, int tolerance) {
        List<IValidationEntry> entries = new ArrayList<IValidationEntry>();
    	
        for (Relationship relationship : diagram.getRelationships()) {
        	int attributesInEntity = MetricsCalculator.getAttributeCount(relationship.getAttributes());
        	if (!metrics.getAttributesPerRelationship().isInRange(attributesInEntity, tolerance)){
				entries.add(new AttributesPerRelationshipValidationEntry(diagram, relationship, attributesInEntity));
			}
		}
        
    	return entries;
    }
}
