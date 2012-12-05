package validation.rules;

import java.util.ArrayList;
import java.util.List;

import models.der.Attribute;
import models.der.Diagram;
import models.der.Entity;
import validation.IValidationEntry;

public class AttributeCardinalityValidator implements IRulesValidator {

	@Override
	public Iterable<IValidationEntry> validate(Diagram diagram) {
		List<IValidationEntry> entries = new ArrayList<IValidationEntry>();
		
		for (Entity entity : diagram.getEntities()) {
			for (Attribute attribute : entity.getAttributes()) {
				if (attribute.getCardinality() == null || attribute.getCardinality().equals(0, 0)){
					entries.add(new AttributeCardinalityValidationEntry(diagram, entity, attribute));
				}
			}
		}
		
		return entries;
	}

}
