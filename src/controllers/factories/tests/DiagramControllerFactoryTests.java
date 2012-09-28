package controllers.factories.tests;


import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import controllers.IDiagramController;
import controllers.factories.DiagramControllerFactory;
import controllers.tests.mocks.MockDiagramController;

import application.tests.mocks.MockPicoContainer;

public class DiagramControllerFactoryTests {

	private MockPicoContainer container;
	
	@Before
	public void setUp() throws Exception {
		this.container = new MockPicoContainer();
	}
	
	@Test
	public void testShouldUseContainerToResolveController(){
		MockDiagramController controller = new MockDiagramController();
		this.container.addComponent(IDiagramController.class, controller);
		
		DiagramControllerFactory factory = this.createDiagramControllerFactory();
		Assert.assertSame(controller,factory.create());
	}
	
	public DiagramControllerFactory createDiagramControllerFactory(){
		return new DiagramControllerFactory(this.container);
	}

}
