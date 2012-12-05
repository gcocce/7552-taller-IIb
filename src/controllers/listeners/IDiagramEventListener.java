package controllers.listeners;

import models.Hierarchy;
import models.der.Diagram;
import models.der.Entity;
import models.der.Relationship;

public interface IDiagramEventListener {
	void handleEntityAdded(Diagram diagram, Entity entity);
	void handleEntityUpdated(Diagram diagram, Entity entity);
	void handleRelationshipAdded(Diagram diagram, Relationship relantionship);
	void handleRelationshipUpdated(Diagram diagram, Relationship relantionship);
	void handleSubDiagramCreated(Diagram diagram, String diagramName);
	void handleHierarchyAdded(Diagram diagram, Hierarchy hierarchy);
	void handleHierarchyUpdated(Diagram diagram, Hierarchy hierarchy);
}
