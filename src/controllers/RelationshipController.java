package controllers;

import infrastructure.IProjectContext;
import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import models.Relationship;
import models.RelationshipEntity;
import views.IRelationshipView;
import controllers.factories.IAttributeControllerFactory;
import controllers.factories.IRelationshipEntityControllerFactory;
import controllers.listeners.IRelationshipEventListener;


public class RelationshipController implements IRelationshipController {

	
	private Relationship pendingRelationship; 
	private IRelationshipView view;
	private List<IRelationshipEventListener> listeners;
	
	private IRelationshipEntityController relEntController;
	private IAttributeController attController;
		
	
	//factories
	private IAttributeControllerFactory attributeControllerFactory;
	private IRelationshipEntityControllerFactory relationshipEntityControllerFactory;

	
	public RelationshipController(
			IProjectContext pContext, IRelationshipView view,
			IAttributeControllerFactory attributeControllerFactory,
			IRelationshipEntityControllerFactory relationshipEntityControllerFactory) {
		
		pendingRelationship = new Relationship();
		this.view = view;
		this.attributeControllerFactory = attributeControllerFactory;
		this.relationshipEntityControllerFactory = relationshipEntityControllerFactory;	
		listeners = new ArrayList<IRelationshipEventListener> ();
		
	}

	@Override
	public void create() {
			
		view.setController(this);
		relEntController = relationshipEntityControllerFactory.create();
		relEntController.setRelatinshipEntities(IterableExtensions.getListOf(pendingRelationship.getRelationshipEntities()));
		attController = this.attributeControllerFactory.create();
		attController.setAttributes(this.pendingRelationship.getAttributes().getAttributes());
		attController.setAttributeView(this.view.getAttributeView());
		this.relEntController.setRelationshipEntityView(this.view.getRelationshipEntityView());
				
		this.view.show();
	}

	@Override
	public void addCreateListener(IRelationshipEventListener listener) {
		listeners.add(listener);
		
	}

	@Override
	public void setName(String name) {
		pendingRelationship.setName(name);
	}

	@Override
	public String getName() {
		return pendingRelationship.getName();
	}

	@Override
	public void isComposition(boolean composition) throws Exception {
		if (relEntController.entitiesAreSameType()) 
			pendingRelationship.isComposition(composition);
		else 
			throw new Exception ("All entities should have a type or should be the same type to make a composition");
	}
	
	@Override
	public boolean isComposition ()  {
		return pendingRelationship.isComposition();
	}

	@Override
	public void setRelationshipView(IRelationshipView view) {
		this.view = view;		
	}

	
	@Override
	public void add() throws Exception {
		
		
		if (this.pendingRelationship.getName()==null || this.pendingRelationship.getName().equals(""))
			throw new Exception ("The field \"name\"  is not completed");
		
		
		if (this.relEntController.getRelationshipEntities().size()<2)
			throw new Exception ("There should be at least two entities in a relationship");
		
		this.pendingRelationship.setRelationshipEntities(this.relEntController
				.getRelationshipEntities());
		
		if (relEntController.isUnary()) {
			validateAllEntitiesHaveRoles();
		}
			       	        
	    for (IRelationshipEventListener listener : listeners) 
	       	listener.handleCreatedEvent(pendingRelationship);
	  
	    view.hide();		
	  }



	private void validateAllEntitiesHaveRoles() throws Exception {
		List<RelationshipEntity> relEntlist = relEntController.getRelationshipEntities();
		for (RelationshipEntity ent1 :relEntlist) {
			for(RelationshipEntity ent2 : relEntlist) {
				if (ent1 != ent2 && ent1.getEntityId() == ent2.getEntityId()) {
					if (ent1.getRole() == null || ent1.getRole().equals("")) {
						throw new Exception ("In unary relationships entities should have a role");
					}
					if (ent2.getRole() == null || ent2.getRole().equals("")) {
						throw new Exception ("In unary relationships entities should have a role");
					}
					if (ent2.getRole().equals(ent1.getRole())) {
						throw new Exception ("Roles should not be repeated");
					}
				}
			}
		}
		
	}

	@Override
	public void create(Relationship relationship) {
		this.pendingRelationship = relationship;
		view.setController(this);
		relEntController = relationshipEntityControllerFactory.create();
		relEntController.setRelatinshipEntities(IterableExtensions.getListOf(pendingRelationship.getRelationshipEntities()));
		attController = this.attributeControllerFactory.create();
		attController.setAttributes(this.pendingRelationship.getAttributes().getAttributes());
		attController.setAttributeView(this.view.getAttributeView());
		this.relEntController.setRelationshipEntityView(this.view.getRelationshipEntityView());
		JFrame myView = (JFrame)this.view;
		myView.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.view.show();
	}
	
}
