package controllers.factories;

import org.picocontainer.MutablePicoContainer;

import controllers.IDomainDiagramController;

public class DomainDiagramControllerFactory implements IDomainDiagramControllerFactory {
	private MutablePicoContainer container;

	public DomainDiagramControllerFactory(MutablePicoContainer container) {
		this.container = container;
	}

	@Override
	public IDomainDiagramController create() {
		return this.container.getComponent(IDomainDiagramController.class);
	}

}
