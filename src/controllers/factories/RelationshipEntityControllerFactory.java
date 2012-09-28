package controllers.factories;

import org.picocontainer.MutablePicoContainer;

import controllers.IRelationshipEntityController;

public class RelationshipEntityControllerFactory implements
		IRelationshipEntityControllerFactory {

	private MutablePicoContainer container;


	public RelationshipEntityControllerFactory(MutablePicoContainer container) {
		this.container = container;
	}
	
	@Override
	public IRelationshipEntityController create() {
		return this.container.getComponent(IRelationshipEntityController.class);
	}

}
