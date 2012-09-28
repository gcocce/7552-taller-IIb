package controllers.factories.tests.mocks;

import controllers.IDiagramController;
import controllers.factories.IDiagramControllerFactory;

public class MockDiagramControllerFactory implements IDiagramControllerFactory{
	
	private IDiagramController diagramController;

	@Override
	public IDiagramController create() {
		return this.diagramController;
	}

	public void setController(IDiagramController diagramController) {
		this.diagramController = diagramController;
	}
}
