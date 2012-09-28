package controllers.factories;

import controllers.IEntityController;
import org.picocontainer.MutablePicoContainer;

public class EntityControllerFactory implements IEntityControllerFactory {
    private MutablePicoContainer container;
	
	public EntityControllerFactory(MutablePicoContainer container) {
		this.container = container;
	}
	
	@Override
    public IEntityController create() {
		return this.container.getComponent(IEntityController.class);
    }
}
