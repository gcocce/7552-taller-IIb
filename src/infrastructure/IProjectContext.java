package infrastructure;

import models.Diagram;
import models.Entity;
import models.Hierarchy;
import java.util.UUID;

public interface IProjectContext {
	String getName();
	void setName(String name);
	String getDataDirectory();
	void clearContextDiagrams();
	Iterable<Entity> getAllEntities();
	Iterable<Entity> getAllEntities(Entity entityToExclude);
	Iterable<Entity> getFamilyEntities();
	Iterable<Entity> getFamilyEntities(Entity entityToExclude);
	Iterable<Entity> getContextEntities();
	Iterable<Hierarchy> getAllHierarchies();
	Iterable<Hierarchy> getFamilyHierarchies();
	Iterable<Hierarchy> getContextHierarchies();
	Iterable<Diagram> getProjectDiagrams();
	void addContextDiagram(Diagram diagram);
	void addProjectDiagram(Diagram diagram);
	Hierarchy getHierarchy(UUID id);
	Entity getEntity(UUID entityId);
	Diagram getContextDiagram(String defaultDiagramName);
	void clearProjectDiagrams();
}
