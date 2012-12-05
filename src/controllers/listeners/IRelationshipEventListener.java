package controllers.listeners;

import models.der.Relationship;

public interface IRelationshipEventListener {
	public void handleCreatedEvent(Relationship relationship) throws Exception;
}
