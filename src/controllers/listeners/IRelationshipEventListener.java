package controllers.listeners;

import models.Relationship;

public interface IRelationshipEventListener {
	public void handleCreatedEvent(Relationship relationship) throws Exception;
}
