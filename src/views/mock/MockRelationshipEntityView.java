package views.mock;

import java.util.List;

import controllers.IRelationshipEntityController;
import controllers.RelationshipEntityController;
import views.IRelationshipEntityView;

public class MockRelationshipEntityView implements IRelationshipEntityView{

	public boolean visible;
	public IRelationshipEntityController controller;
	
	public MockRelationshipEntityView () {
		visible = false;
		controller = null;
	}
	
	@Override
	public IRelationshipEntityController getController() {
		return controller;
	}

	@Override
	public void setController(
			IRelationshipEntityController relationshipEntityController) {
		controller = relationshipEntityController;
		
	}

	@Override
	public void show() {
		visible = true;		
	}

	@Override
	public void hide() {
		visible = false;
	}

	

	@Override
	public List<Object[]> getModelList() {
		// TODO Auto-generated method stub
		return null;
	}

}
