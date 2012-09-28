package controllers.tests.mocks;

import application.IShell;

public class MockShell implements IShell {

	private Object rightContent;
	private Object leftContent;
	private int fullSizeCalls;

	@Override
	public void setLeftContent(Object c) {
		this.leftContent = c;
	}

	@Override
	public void setRightContent(Object c) {
		this.rightContent = c;
	}
	
	public Object getRightContent(){
		return this.rightContent;
	}
	
	public Object getLeftContent(){
		return this.leftContent;
	}

	@Override
	public void activateFullSize() {
		this.fullSizeCalls++;
	}
	
	public int getFullSizeCalls(){
		return this.fullSizeCalls;
	}
}
