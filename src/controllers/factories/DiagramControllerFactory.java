package controllers.factories;

import org.picocontainer.MutablePicoContainer;

import controllers.IDiagramController;

public class DiagramControllerFactory implements IDiagramControllerFactory {

	private MutablePicoContainer container;

	public DiagramControllerFactory(MutablePicoContainer container) {
		this.container = container;
	}

	@Override
	public IDiagramController create() {
		return this.container.getComponent(IDiagramController.class);
	}

}
