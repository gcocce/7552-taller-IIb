package controllers;

import controllers.listeners.IHierarchyEventListener;
import models.Entity;
import models.Hierarchy;
import views.IHierarchyView;

public interface IHierarchyController {
	
	void create();

	void setHierachyView(IHierarchyView hierarchyView);

	void addSuscriber(IHierarchyEventListener listener);

	boolean addHierarchy();

	boolean setGeneralEntity(Entity entity);

	boolean addSpecificEntity(Entity entity) throws Exception;

	boolean removeSpecificEntity(Entity entity);

	void setTotal(boolean total);

	void setExclusive(boolean exclusive);

	Iterable<Entity> getAvailableEntities();

	boolean hasSpecificEntity(Entity entity);

	boolean isGeneralEntity(Entity entity);

	void create(Hierarchy hierarchy);

	boolean relationshipIsTotal();

	boolean relationshipIsExclusive();

}
