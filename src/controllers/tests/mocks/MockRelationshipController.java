package controllers.tests.mocks;

import java.util.ArrayList;
import java.util.List;
import views.IRelationshipView;

import models.Relationship;
import controllers.IRelationshipController;
import controllers.listeners.IRelationshipEventListener;

public class MockRelationshipController implements IRelationshipController{

	private boolean createCalled;
	private List<IRelationshipEventListener> listeners;
	private int updateCalls;
	
	public MockRelationshipController(){
		this.createCalled = false;
		this.listeners = new ArrayList<IRelationshipEventListener>();
	}
	
	public boolean createWasCalled() {
		return this.createCalled;
	}

	@Override
	public void create() {
		this.createCalled = true;
	}

	public List<IRelationshipEventListener> getListeners() {
		return this.listeners;
	}

	@Override
	public void addCreateListener(IRelationshipEventListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isComposition() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void isComposition(boolean composition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRelationshipView(IRelationshipView view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create(Relationship relationship) {
		this.updateCalls++;
	}

	public int getUpdateCallsCount() {
		return this.updateCalls;
	}
}
