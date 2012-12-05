package controllers.listeners;

import models.der.Entity;

public interface IEntityEventListener {
	public void handleCreatedEvent(Entity entity);
}
