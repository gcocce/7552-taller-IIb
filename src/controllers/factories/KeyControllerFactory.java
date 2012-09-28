package controllers.factories;

import controllers.IKeysController;
import org.picocontainer.MutablePicoContainer;

public class KeyControllerFactory implements IKeysControllerFactory{

	private MutablePicoContainer container;

	public KeyControllerFactory (MutablePicoContainer container) {
		this.container = container;
	}
	
    @Override
    public IKeysController create() {
    	return this.container.getComponent(IKeysController.class);
    }
}
