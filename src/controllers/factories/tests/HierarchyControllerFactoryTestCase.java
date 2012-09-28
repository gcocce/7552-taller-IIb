package controllers.factories.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.MutablePicoContainer;

import controllers.IHierarchyController;
import controllers.factories.HierarchyControllerFactory;
import controllers.tests.mocks.MockHierarchyController;

import application.tests.mocks.MockPicoContainer;


public class HierarchyControllerFactoryTestCase {

	MutablePicoContainer container;
	
	@Before
	public void setUp() {
		this.container = new MockPicoContainer();
	}
	
	@Test
	public void testShouldUseContainerToResolveController() {
		MockHierarchyController controller = new MockHierarchyController();
		this.container.addComponent(IHierarchyController.class, controller);
		
		HierarchyControllerFactory factory = this.createHierarchyControllerFactory();
		Assert.assertSame(controller, factory.create());
	}

	public HierarchyControllerFactory createHierarchyControllerFactory() {
		return new HierarchyControllerFactory(this.container);
	}
}
