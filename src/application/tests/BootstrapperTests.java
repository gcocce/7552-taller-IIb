package application.tests;

import infrastructure.IProjectContext;
import infrastructure.ProjectContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.picocontainer.MutablePicoContainer;

import persistence.DiagramXmlManager;
import persistence.GraphPersistenceService;
import persistence.IGraphPersistenceService;
import persistence.IXmlFileManager;
import persistence.IXmlManager;
import persistence.XmlFileManager;
import views.DiagramView;
import views.HierarchyView;
import views.IDiagramView;
import views.IHierarchyView;
import views.IProjectView;
import views.ProjectView;
import application.Bootstrapper;
import application.tests.mocks.MockPicoContainer;
import controllers.DiagramController;
import controllers.HierarchyController;
import controllers.IDiagramController;
import controllers.IHierarchyController;
import controllers.IProjectController;
import controllers.ProjectController;
import controllers.factories.AttributeControllerFactory;
import controllers.factories.DiagramControllerFactory;
import controllers.factories.EntityControllerFactory;
import controllers.factories.HierarchyControllerFactory;
import controllers.factories.IAttributeControllerFactory;
import controllers.factories.IDiagramControllerFactory;
import controllers.factories.IEntityControllerFactory;
import controllers.factories.IHierarchyControllerFactory;
import controllers.factories.IKeysControllerFactory;
import controllers.factories.IRelationshipControllerFactory;
import controllers.factories.IRelationshipEntityControllerFactory;
import controllers.factories.KeyControllerFactory;
import controllers.factories.RelationshipControllerFactory;
import controllers.factories.RelationshipEntityControllerFactory;

public class BootstrapperTests {
	
	private MockPicoContainer container;

	@Before
	public void setUp() throws Exception {
		this.container = new MockPicoContainer();
	}
	
	@Test
	public void testShouldCreateContainerWhenRunning(){
		TestableBootstrapper bootstrapper = this.createBootstrapper();
		
		bootstrapper.setContainerToReturn(this.container);
		
		Assert.assertNull(bootstrapper.getContainer());
		
		bootstrapper.run();
		
		Assert.assertNotNull(bootstrapper.getContainer());
		junit.framework.Assert.assertSame(this.container, bootstrapper.getContainer());
	}
	
	@Test
	public void testShouldConfigureMappings(){
		TestableBootstrapper bootstrapper = this.createBootstrapper();
		bootstrapper.setContainerToReturn(this.container);
		
		Assert.assertEquals(0, this.container.getMappings().size());
		
		bootstrapper.run();
		
		Assert.assertEquals(43, this.container.getMappings().size());
		Assert.assertSame(this.container, this.container.getMappings().get(MutablePicoContainer.class));
		Assert.assertSame(DiagramController.class, this.container.getMappings().get(IDiagramController.class));
		Assert.assertSame(DiagramView.class, this.container.getMappings().get(IDiagramView.class));
		Assert.assertSame(DiagramXmlManager.class, this.container.getMappings().get(IXmlManager.class));
		Assert.assertSame(XmlFileManager.class, this.container.getMappings().get(IXmlFileManager.class));
		Assert.assertSame(ProjectContext.class, this.container.getMappings().get(IProjectContext.class));
		Assert.assertSame(EntityControllerFactory.class, this.container.getMappings().get(IEntityControllerFactory.class));
		Assert.assertSame(RelationshipControllerFactory.class, this.container.getMappings().get(IRelationshipControllerFactory.class));
		Assert.assertSame(RelationshipEntityControllerFactory.class, this.container.getMappings().get(IRelationshipEntityControllerFactory.class));
		Assert.assertSame(HierarchyControllerFactory.class, this.container.getMappings().get(IHierarchyControllerFactory.class));
		Assert.assertSame(HierarchyController.class, this.container.getMappings().get(IHierarchyController.class));
		Assert.assertSame(HierarchyView.class, this.container.getMappings().get(IHierarchyView.class));
		Assert.assertSame(AttributeControllerFactory.class, this.container.getMappings().get(IAttributeControllerFactory.class));
		Assert.assertSame(KeyControllerFactory.class, this.container.getMappings().get(IKeysControllerFactory.class));
		Assert.assertSame(GraphPersistenceService.class, this.container.getMappings().get(IGraphPersistenceService.class));
		Assert.assertSame(DiagramControllerFactory.class, this.container.getMappings().get(IDiagramControllerFactory.class));
		Assert.assertSame(ProjectController.class, this.container.getMappings().get(IProjectController.class));
		Assert.assertSame(ProjectView.class, this.container.getMappings().get(IProjectView.class));
	}
	
	private TestableBootstrapper createBootstrapper(){
		return new TestableBootstrapper();
	}
	
	private class TestableBootstrapper extends Bootstrapper	{
		private MutablePicoContainer containerToReturn;
		
		public void setContainerToReturn(MutablePicoContainer c){
			this.containerToReturn = c;
		}
		
		@Override
		public MutablePicoContainer createContainer(){
			return this.containerToReturn;
		};
	}
}
