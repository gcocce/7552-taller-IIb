package controllers;

import java.util.List;
import java.util.UUID;

import controllers.listeners.IRelationshipEntityEventListener;

import models.Cardinality;
import models.Entity;
import models.RelationshipEntity;

import views.IRelationshipEntityView;


public interface IRelationshipEntityController {

	public void create();
	public void remove(UUID uuid) throws Exception;
	public List<RelationshipEntity> getRelationshipEntities();
	public void setRelationshipEntityView (IRelationshipEntityView view);
	void addSuscriber(IRelationshipEntityEventListener listener);
	void modify(UUID uuid, Cardinality card, String role, boolean isStrong)
			throws Exception;
	void add(UUID uuid, Cardinality card, String role, boolean isStrong);
	List<Object[]> getListForModel();
	public Iterable<Entity> getEntities();
	public boolean entitiesAreSameType();
	void updateModel(List<Object[]> list) throws Exception;
	public void setRelatinshipEntities(List<RelationshipEntity> relationshipEntities);
	boolean isUnary();
	
}
