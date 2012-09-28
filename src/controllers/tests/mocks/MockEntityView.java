package controllers.tests.mocks;

import controllers.IEntityController;
import models.EntityType;
import views.IAttributeView;
import views.IEntityView;


public class MockEntityView implements IEntityView {
    private IEntityController controller;
    public boolean addViewWasCall = false;
    private boolean showViewWasCall = false;
    private String name;
	private IAttributeView attributeView;

    @Override
    public void setController(IEntityController entityController) {
        this.controller = entityController;
    }

    @Override
    public void showView() {
        this.showViewWasCall = true;
    }

    @Override
    public void addAttributeView(IAttributeView attributeView) {
        this.attributeView = attributeView;
    	this.addViewWasCall = true;
    }

    @Override
    public IAttributeView getAttributeView() {
        return this.attributeView;
    }

    @Override
    public String getEntityName() {
        return this.name;
    }

    @Override
    public EntityType getEntityType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isVisible() {
        return this.showViewWasCall;
    }

    public IEntityController getController() {
        return controller;
    }

    public void setEntityName(String s) {
        this.name = s;
    }

    @Override
    public void setEntityType(EntityType type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getName(){
        return this.name;
    }

	@Override
	public void setModeUpdating() {
		// TODO Auto-generated method stub
		
	}
}
