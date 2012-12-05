package controllers.listeners;


import models.der.RelationshipEntity;

public interface IRelationshipEntityEventListener {
	public void handleCreatedEvent(RelationshipEntity relationshipEntity);
}
