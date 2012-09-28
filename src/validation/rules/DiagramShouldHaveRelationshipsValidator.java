package validation.rules;

import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.List;

import models.Diagram;
import validation.IValidationEntry;

public class DiagramShouldHaveRelationshipsValidator implements IRulesValidator {

	@Override
	public Iterable<IValidationEntry> validate(Diagram diagram) {
		List<IValidationEntry> entries = new ArrayList<IValidationEntry>();
		
		if (IterableExtensions.count(diagram.getRelationships()) == 0){
			entries.add(new DiagramShouldHaveEntitiesValidationEntry(diagram));
		}
				
		return entries;
	}

}
