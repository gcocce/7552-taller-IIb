package views;

import controllers.IRelationshipController;

public interface IRelationshipView {

	IRelationshipController getController();

	void setController(IRelationshipController relationshipController);

	void show();
	void hide();

	void addRelationshipEntityView(RelationshipEntityViewImpl relEntView);

	void addAttributeView(IAttributeView attview);

	IAttributeView getAttributeView();

	IRelationshipEntityView getRelationshipEntityView();
	
}
