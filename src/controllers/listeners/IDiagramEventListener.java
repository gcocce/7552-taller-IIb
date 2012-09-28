package controllers.listeners;

import models.Diagram;
import models.Entity;
import models.Hierarchy;
import models.Relationship;

public interface IDiagramEventListener {
	void handleEntityAdded(Diagram diagram, Entity entity);
	void handleEntityUpdated(Diagram diagram, Entity entity);
	void handleRelationshipAdded(Diagram diagram, Relationship relantionship);
	void handleRelationshipUpdated(Diagram diagram, Relationship relantionship);
	void handleSubDiagramCreated(Diagram diagram, String diagramName);
	void handleHierarchyAdded(Diagram diagram, Hierarchy hierarchy);
	void handleHierarchyUpdated(Diagram diagram, Hierarchy hierarchy);
}
