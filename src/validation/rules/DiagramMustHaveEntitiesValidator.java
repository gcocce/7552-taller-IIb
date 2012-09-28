package validation.rules;

import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.List;

import models.Diagram;
import validation.IValidationEntry;

public class DiagramMustHaveEntitiesValidator implements IRulesValidator {

	@Override
	public Iterable<IValidationEntry> validate(Diagram diagram) {
		List<IValidationEntry> entries = new ArrayList<IValidationEntry>();
		
		if (IterableExtensions.count(diagram.getEntities()) == 0){
			entries.add(new DiagramMustHaveEntitiesValidationEntry(diagram));
		}
				
		return entries;
	}

}
