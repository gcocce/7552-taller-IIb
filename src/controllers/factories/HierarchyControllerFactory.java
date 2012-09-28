package controllers.factories;

import org.picocontainer.MutablePicoContainer;

import controllers.IHierarchyController;

public class HierarchyControllerFactory implements IHierarchyControllerFactory {

	private MutablePicoContainer container;
	
	public HierarchyControllerFactory(MutablePicoContainer container) {
		this.container = container;
	}
	
	@Override
	public IHierarchyController create() {
		return this.container.getComponent(IHierarchyController.class);
	}
}
