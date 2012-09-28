package controllers.factories;

import controllers.IAttributeController;
import org.picocontainer.MutablePicoContainer;

public class AttributeControllerFactory implements IAttributeControllerFactory {
    
	private MutablePicoContainer container;
	
	public AttributeControllerFactory(MutablePicoContainer container) {
		this.container = container;
	}

	@Override
	public IAttributeController create() {
		return this.container.getComponent(IAttributeController.class);
	}
}
