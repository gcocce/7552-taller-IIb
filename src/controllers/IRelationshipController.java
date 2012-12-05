package controllers;

import controllers.listeners.IRelationshipEventListener;

import views.IRelationshipView;

import models.der.Relationship;


public interface IRelationshipController {
	
	void create();
	void addCreateListener(IRelationshipEventListener listener);
	
	void setName(String name);
	String getName();
	
	
	void isComposition(boolean composition) throws Exception;
	boolean isComposition();
	
	void add() throws Exception;

	void setRelationshipView(IRelationshipView view);
	void create(Relationship relationship);
	
	
	
	

	
	
	
}
