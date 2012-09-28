package controllers.listeners;


import models.RelationshipEntity;

public interface IRelationshipEntityEventListener {
	public void handleCreatedEvent(RelationshipEntity relationshipEntity);
}
