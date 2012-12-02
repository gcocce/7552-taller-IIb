package infrastructure;

import java.util.UUID;

import models.Diagram;
import models.DomainDiagram;
import models.Entity;
import models.Hierarchy;

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
	// TODO: See if we need it
//	Iterable<DomainClass> getAllDomainClasses();
	void addContextDiagram(Diagram diagram);
	void addProjectDiagram(Diagram diagram);
	Hierarchy getHierarchy(UUID id);
	Entity getEntity(UUID entityId);
	Diagram getContextDiagram(String defaultDiagramName);
	DomainDiagram getContextDomainDiagram(String defaultDiagramName);
	void clearProjectDiagrams();
}
