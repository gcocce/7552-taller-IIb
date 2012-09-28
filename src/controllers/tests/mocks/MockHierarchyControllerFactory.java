package controllers.tests.mocks;

import controllers.IHierarchyController;
import controllers.factories.IHierarchyControllerFactory;

public class MockHierarchyControllerFactory implements IHierarchyControllerFactory {

	private IHierarchyController controller;
	private int createCalls;

	public void setController(IHierarchyController hierarchyController) {
		this.controller = hierarchyController;
	}

	@Override
	public IHierarchyController create() {
		this.createCalls++;
		return this.controller;
	}

	public int getCreateCallsCount() {
		return this.createCalls;
	}
}
