package controllers.factories.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.MutablePicoContainer;

import controllers.IEntityController;
import controllers.factories.EntityControllerFactory;
import controllers.tests.mocks.MockEntityController;

import application.tests.mocks.MockPicoContainer;


public class EntityControllerFactoryTestCase {

	private MutablePicoContainer container;
	
	@Before
	public void setUp() {
		this.container = new MockPicoContainer();
	}
	
	@Test
	public void testShouldUseContainerToResolveFactory() {
		MockEntityController controller = new MockEntityController();
		this.container.addComponent(IEntityController.class, controller);
		
		EntityControllerFactory factory = this.createEntityControllerFactory();
		Assert.assertSame(controller, factory.create());
	}
	
	private EntityControllerFactory createEntityControllerFactory() {
		return new EntityControllerFactory(this.container);
	}
}
