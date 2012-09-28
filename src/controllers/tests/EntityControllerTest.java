package controllers.tests;

import controllers.EntityController;
import controllers.tests.mocks.*;
import junit.framework.Assert;
import models.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EntityControllerTest {

	private MockAttributeController mockAttributeController;
	private MockEntityView mockEntityView;
	private MockProjectContext mockProjectContext;
	private MockEntityEventListener mockEntityCreatedListener;
	private MockAttributeControllerFactory mockAttributeControllerFactory;
	private EntityController entityController;
	private static final String ENTITY_NAME = "NAME";
	private static final String ATT_NAME_1 = "ADSADS";
	private static final String ATT_NAME_2 = "SSADSADS";
	private static final String ATT_NAME_3 = "ADSSSSADS";
	private static final String ATT_NAME_4 = "ADSASADDS";
	private MockKeyControllerFactory mockKeyControllerFactory;
	private MockKeyController mockKeyController;

	@Test
	public void TestCreation() {
		Assert.assertEquals(entityController,
				this.mockEntityView.getController());
	}

	@Test
	public void TestCreate() {
		entityController.create();
		Assert.assertTrue(this.mockEntityView.addViewWasCall);
		Assert.assertTrue(this.mockEntityView.isVisible());
	}

	@Test
	public void TestValidateName() {
		EntityCollection entityCollection = new EntityCollection();
		entityCollection.add(ENTITY_NAME, EntityType.Domain);

		this.mockProjectContext.setEntityCollection(entityCollection);
		entityController = new EntityController(mockProjectContext, mockEntityView, mockAttributeControllerFactory,
				mockKeyControllerFactory);
		entityController.create();

		mockEntityView.setEntityName("");
		Assert.assertFalse(this.entityController.addEntity());

		mockEntityView.setEntityName(ENTITY_NAME);
		Assert.assertFalse(this.entityController.addEntity());

		mockEntityView.setEntityName("asdadasdasdas");
		Assert.assertTrue(this.entityController.addEntity());

	}

	@Test
	public void TestAddEntity() {
		EntityCollection entityCollection = new EntityCollection();
		entityCollection.add(ENTITY_NAME, EntityType.Domain);
		List<Attribute> list = new ArrayList<Attribute>();
		list.add(new Attribute(ATT_NAME_1));
		list.add(new Attribute(ATT_NAME_2));
		list.add(new Attribute(ATT_NAME_3));
		list.add(new Attribute(ATT_NAME_4));
		mockAttributeController.setAttributes(list);

		mockEntityCreatedListener = new MockEntityEventListener();
		this.entityController.addSubscriber(this.mockEntityCreatedListener);
		this.entityController.create();
		this.mockEntityView.setEntityName("");
		Assert.assertFalse(this.entityController.addEntity());
		Assert.assertFalse(this.mockEntityCreatedListener.called);

		this.mockProjectContext.setEntityCollection(entityCollection);

		mockAttributeControllerFactory = new MockAttributeControllerFactory();
		mockAttributeControllerFactory
				.setAttributeController(mockAttributeController);
		
		entityController = new EntityController(mockProjectContext, 
				mockEntityView, mockAttributeControllerFactory,
				mockKeyControllerFactory);
		this.entityController.addSubscriber(this.mockEntityCreatedListener);
		entityController.create();
		this.mockEntityView.setEntityName("sdadasd");

		Assert.assertTrue(this.entityController.addEntity());
		Assert.assertTrue(this.mockEntityCreatedListener.called);

		this.mockEntityView.setEntityName(ENTITY_NAME);
		Assert.assertFalse(this.entityController.addEntity());
		this.mockEntityCreatedListener.called = false;
		Assert.assertFalse(this.mockEntityCreatedListener.called);
	}

	@Test
	public void TestSelectKeys() {
		entityController = new EntityController(mockProjectContext, 
				new MockEntityView(), mockAttributeControllerFactory,
				mockKeyControllerFactory);
		entityController.create();

		entityController.selectKeys();

		Assert.assertFalse(mockKeyControllerFactory.createCalled());

		List<Attribute> list = new ArrayList<Attribute>();
		list.add(new Attribute("nombre"));
		this.mockAttributeController.setAttributes(list);

		entityController.selectKeys();

		Assert.assertEquals(mockKeyController.getKeys(), list);
		Assert.assertTrue(mockKeyControllerFactory.createCalled());
	}
	
	
	@Test
	public void TestShouldCallEverithingWellWhenUpdateAnEntity() throws Exception {
		Entity entity = new Entity("Entity1");
		AttributeCollection collection = new AttributeCollection();
		collection.addAttribute("att1");
		collection.addAttribute("att2");
		collection.addAttribute("att3");
		entity.setAttributes(collection);
		
		
		entityController.create(entity);
		
		Assert.assertTrue(this.mockAttributeControllerFactory.wasCalled());
		
		List<Attribute> attributes = (List<Attribute>) this.mockAttributeController.getAttributes();
		
		Assert.assertEquals(collection.getAttributes(), attributes);
		
		Assert.assertTrue(this.mockEntityView.addViewWasCall);
		Assert.assertTrue(this.mockEntityView.isVisible());
	}

	@Before
	public void setUp() throws Exception {
		mockAttributeController = new MockAttributeController();
		mockEntityView = new MockEntityView();
		mockProjectContext = new MockProjectContext();
		mockEntityCreatedListener = new MockEntityEventListener();
		mockAttributeControllerFactory = new MockAttributeControllerFactory();
		mockAttributeControllerFactory
				.setAttributeController(mockAttributeController);
		mockKeyControllerFactory = new MockKeyControllerFactory();
		mockKeyController = new MockKeyController();
		entityController = new EntityController(mockProjectContext, 
				mockEntityView, mockAttributeControllerFactory,
				mockKeyControllerFactory);

		mockAttributeControllerFactory
				.setAttributeController(mockAttributeController);
		mockKeyControllerFactory.setKeyController(mockKeyController);

	}
}