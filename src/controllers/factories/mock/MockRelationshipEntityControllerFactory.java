package controllers.factories.mock;

import java.util.List;

import models.RelationshipEntity;
import controllers.IRelationshipEntityController;
import controllers.factories.IRelationshipEntityControllerFactory;
import controllers.tests.mocks.MockRelationshipEntityController;

public class MockRelationshipEntityControllerFactory implements
		IRelationshipEntityControllerFactory {

	private MockRelationshipEntityController mockRelationshipEntityController;
	private List<RelationshipEntity> relationshipEntities;
	
	public void setRelationshipEntityController(
			IRelationshipEntityController mockRelationshipEntityController2) {
		this.mockRelationshipEntityController = (MockRelationshipEntityController) mockRelationshipEntityController2;
		
	}

	@Override
	public IRelationshipEntityController create() {
		mockRelationshipEntityController.setRelationshipEntities(this.relationshipEntities);
		return mockRelationshipEntityController;
	}
	
	public void setRelationshipEntities(List<RelationshipEntity> relationshipEntities) {
		this.relationshipEntities = relationshipEntities;
	}

}
