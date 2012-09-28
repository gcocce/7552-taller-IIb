package controllers.factories;

import org.picocontainer.MutablePicoContainer;

import controllers.IRelationshipController;

public class RelationshipControllerFactory implements
		IRelationshipControllerFactory {

	private MutablePicoContainer container;

	public RelationshipControllerFactory(MutablePicoContainer container) {
		this.container = container;
	}
	
	
	@Override
	public IRelationshipController create() {
		return this.container.getComponent(IRelationshipController.class);
	}
}
