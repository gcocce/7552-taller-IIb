package validation.metrics;

import java.util.ArrayList;
import java.util.List;

import validation.IValidationEntry;

import models.Diagram;
import models.Entity;

public class AttributesPerEntityValidator implements IMetricsValidator {
    @Override
    public Iterable<IValidationEntry> validate(Diagram diagram, Metrics metrics, int tolerance) {
        List<IValidationEntry> entries = new ArrayList<IValidationEntry>();
    	
        for (Entity entity : diagram.getEntities()) {
        	int attributesInEntity = MetricsCalculator.getAttributeCount(entity.getAttributes());
        	if (!metrics.getAttributesPerEntity().isInRange(attributesInEntity, tolerance)){
				entries.add(new AttributesPerEntityValidationEntry(diagram, entity, attributesInEntity));
			}
		}
        
    	return entries;
    }
}
