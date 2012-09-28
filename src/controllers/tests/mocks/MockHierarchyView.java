package controllers.tests.mocks;

import controllers.IHierarchyController;
import views.IHierarchyView;

public class MockHierarchyView implements IHierarchyView {
	private IHierarchyController controller;
	public boolean showViewWasCall = false;
	
	public IHierarchyController getController() {
		return this.controller;
	}
	
	@Override
	public void setController(IHierarchyController controller)
	{
		this.controller = controller;
	}

	@Override
	public boolean isVisible() {
		return showViewWasCall;
	}
	
	@Override
    public void showView() {
        this.showViewWasCall = true;
    }

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
