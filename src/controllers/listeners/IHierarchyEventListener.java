package controllers.listeners;

import models.Hierarchy;

public interface IHierarchyEventListener {
	public void handleCreatedEvent(Hierarchy hierarchy);
}
