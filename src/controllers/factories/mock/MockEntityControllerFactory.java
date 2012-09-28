package controllers.factories.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import controllers.factories.IEntityControllerFactory;
import controllers.listeners.IEntityEventListener;
import views.IEntityView;
import models.Attribute;
import models.AttributeType;
import models.Cardinality;
import models.Entity;
import models.EntityType;
import models.IdGroup;
import models.IdGroupCollection;
import controllers.IEntityController;

public class MockEntityControllerFactory implements IEntityControllerFactory {

	public static List<Entity> Entities = new ArrayList<Entity>();
	
	@Override
	public IEntityController create() {
		return new IEntityController() {
			
			private IEntityEventListener listener;

			@Override
			public boolean validateEntityName(String name) {
				return false;
			}
			
			@Override
			public void setEntityView(IEntityView entityView) {
			}

            @Override
            public void selectKeys() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
			public void create() {
				Random random = new Random();
				Entity entity = new Entity("Entity" + (MockEntityControllerFactory.Entities.size() + 1));
				EntityType[] types = {EntityType.None, EntityType.Domain,EntityType.Thing,EntityType.Programmed,EntityType.Historic};
				int value = random.nextInt(4);
				entity.setType(types[value]);
				MockEntityControllerFactory.Entities.add(entity);
				try {
					IdGroupCollection groupCollection1 = new IdGroupCollection();
					groupCollection1.addIdGroup(new IdGroup("1"));
					entity.getAttributes().addAttribute("Attribute1", new Cardinality(0, 1), groupCollection1, AttributeType.copy, null);
					
					IdGroupCollection groupCollection2 = new IdGroupCollection();
					groupCollection2.addIdGroup(new IdGroup("2"));
					groupCollection2.addIdGroup(new IdGroup("3"));
					
					IdGroupCollection groupCollection3 = new IdGroupCollection();
					groupCollection3.addIdGroup(new IdGroup("2"));
					
					IdGroupCollection groupCollection4 = new IdGroupCollection();
					groupCollection4.addIdGroup(new IdGroup("3"));
					
					entity.getAttributes().addAttribute("Attribute2", new Cardinality(0, 1), groupCollection2, AttributeType.calculated, "expression");
					entity.getAttributes().addAttribute("Attribute3", new Cardinality(0, 1), groupCollection3, AttributeType.characterization, null);
					
					entity.getAttributes().addAttribute("Attribute4");
					Attribute attribute4 = entity.getAttributes().getAttribute("Attribute4");
					attribute4.getAttributes().addAttribute("Attribute6");
					attribute4.getAttributes().addAttribute("Attribute7");
					Attribute attribute6 = attribute4.getAttributes().getAttribute("Attribute6");
					attribute6.getAttributes().addAttribute("Attribute8");
					attribute6.getAttributes().addAttribute("Attribute9");
					
					entity.getAttributes().addAttribute("Attribute5", new Cardinality(0, 1), groupCollection3, AttributeType.calculated, "Attribute5");
					
					entity.getAttributes().addAttribute("Attribute10", new Cardinality(0, 1), groupCollection4, AttributeType.copy, null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listener.handleCreatedEvent(entity);
			}
			
			@Override
			public void addSubscriber(IEntityEventListener listener) {
				this.listener = listener;
			}
			
			@Override
			public boolean addEntity() {
				return true;
			}

			@Override
			public void create(Entity entity) {
				// TODO Auto-generated method stub
				
			}
		};
	}
}
