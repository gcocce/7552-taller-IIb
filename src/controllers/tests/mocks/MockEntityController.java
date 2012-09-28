package controllers.tests.mocks;

import java.util.ArrayList;
import java.util.List;

import models.Entity;

import views.IEntityView;
import controllers.IEntityController;
import controllers.listeners.IEntityEventListener;

public class MockEntityController implements IEntityController{

	private int createCalls;
	private List<IEntityEventListener> listeners;
	private int updateCalls;
	
	public MockEntityController(){
		this.createCalls = 0;
		this.listeners = new ArrayList<IEntityEventListener>();
	}
	
	@Override
	public void create() {
		this.createCalls++;
	}

    public int getCreateCallsCount() {
		return createCalls;
	}

	@Override
	public boolean addEntity() {
		return false;
	}


	@Override
	public void setEntityView(IEntityView entityView) {
		
	}

    @Override
    public void selectKeys() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public boolean validateEntityName(String name) {
		return false;
	}

	@Override
	public void addSubscriber(IEntityEventListener listener) {
		this.listeners.add(listener);
	}

	public List<IEntityEventListener> getListeners() {
		return this.listeners;
	}

	@Override
	public void create(Entity entity) {
		this.updateCalls++;
	}

	public int getUpdateCallsCount() {
		return this.updateCalls;
	}
}
