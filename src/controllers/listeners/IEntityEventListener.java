package controllers.listeners;

import models.Entity;

public interface IEntityEventListener {
	public void handleCreatedEvent(Entity entity);
}
