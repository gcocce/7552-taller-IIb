package validation.rules;

import java.util.ArrayList;
import java.util.List;

import models.Diagram;
import models.Relationship;
import models.RelationshipEntity;
import validation.IValidationEntry;

public class RelationshipCardinalityValidator implements IRulesValidator {

	@Override
	public Iterable<IValidationEntry> validate(Diagram diagram) {
		List<IValidationEntry> entries = new ArrayList<IValidationEntry>();
		
		for (Relationship relationship : diagram.getRelationships()) {
			for (RelationshipEntity relationshipEntity : relationship.getRelationshipEntities()) {
				if (relationshipEntity.getCardinality().equals(0, 0)){
					entries.add(new RelationshipCardinalityValidationEntry(diagram, relationship));
					break;
				}
			}
		}
		
		return entries;
	}
}
