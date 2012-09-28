package validation.rules;

import java.util.ArrayList;
import java.util.List;

import models.Diagram;
import models.Entity;
import models.EntityType;
import validation.IValidationEntry;

public class EntityTypeValidator implements IRulesValidator {

	@Override
	public Iterable<IValidationEntry> validate(Diagram diagram) {
		List<IValidationEntry> entries = new ArrayList<IValidationEntry>();
		
		for (Entity entity : diagram.getEntities()) {
			if (entity.getType() == EntityType.None){
				entries.add(new EntityTypeValidationEntry(diagram, entity));
			}
		}
		
		return entries;
	}

}
