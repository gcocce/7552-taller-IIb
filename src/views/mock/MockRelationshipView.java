package views.mock;

import views.IAttributeView;
import views.IRelationshipEntityView;
import views.IRelationshipView;
import views.RelationshipEntityViewImpl;
import controllers.IRelationshipController;

public class MockRelationshipView implements IRelationshipView {

	public boolean visible;
	public IRelationshipController relController;

	public IRelationshipController getController() {
		return relController;
	}

	@Override
	public void setController(IRelationshipController relationshipController) {
		relController = relationshipController;
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
	public void addRelationshipEntityView(RelationshipEntityViewImpl relEntView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAttributeView(IAttributeView attview) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IAttributeView getAttributeView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRelationshipEntityView getRelationshipEntityView() {
		// TODO Auto-generated method stub
		return null;
	}

}
