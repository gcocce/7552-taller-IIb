package controllers.tests.mocks;

import models.Hierarchy;
import controllers.listeners.IHierarchyEventListener;

public class MockHierarchyEventListener implements IHierarchyEventListener{

	public boolean called = false;
	private Hierarchy hierarchy;

	@Override
	public void handleCreatedEvent(Hierarchy hierarchy) {
		this.called = true;
		this.hierarchy = hierarchy;
		
	}

	public Hierarchy getHierarchy() {
		return this.hierarchy;
	}

}
