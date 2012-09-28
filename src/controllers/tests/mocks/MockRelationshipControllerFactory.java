package controllers.tests.mocks;

import controllers.IRelationshipController;
import controllers.factories.IRelationshipControllerFactory;

public class MockRelationshipControllerFactory 
	implements IRelationshipControllerFactory{

	private IRelationshipController controller;
	private int createCalls;
	
	@Override
	public IRelationshipController create() {
		this.createCalls++;
		return this.controller;
	}

	public void setController(IRelationshipController controller) {
		this.controller = controller;
	}

	public IRelationshipController getController() {
		return controller;
	}

	public int getCreateCallsCount() {
		return this.createCalls;
	}
}
