package controllers.tests.mocks;

import javax.swing.tree.TreeModel;

import controllers.IProjectController;
import views.IProjectView;

public class MockProjectView implements IProjectView {

	private IProjectController controller;

	@Override
	public void setController(IProjectController projectController) {
		this.controller = projectController;
	}
	
	public IProjectController getController(){
		return this.controller;
	}

	@Override
	public void refreshTree(TreeModel projectTree) {
		// TODO Auto-generated method stub
		
	}

}
