package controllers.tests.mocks;

import controllers.IAttributeController;
import controllers.factories.IAttributeControllerFactory;

public class MockAttributeControllerFactory implements IAttributeControllerFactory{
    private IAttributeController attributeController;
	private boolean wasCalled;

	public MockAttributeControllerFactory() {
		this.wasCalled = false;
	}
	
    @Override
    public IAttributeController create() {
    	this.wasCalled = true;
    	return this.attributeController;
    }

    public void setAttributeController(IAttributeController attributeController) {
        this.attributeController = attributeController;
    }

	public boolean wasCalled() {
		return this.wasCalled;
	}
}
