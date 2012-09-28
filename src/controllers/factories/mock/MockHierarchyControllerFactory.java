package controllers.factories.mock;

import views.IHierarchyView;
import models.Entity;
import models.Hierarchy;
import controllers.IHierarchyController;
import controllers.factories.IHierarchyControllerFactory;
import controllers.listeners.IHierarchyEventListener;

public class MockHierarchyControllerFactory implements IHierarchyControllerFactory{

	@Override
	public IHierarchyController create() {
		return new IHierarchyController() {
			
			private IHierarchyEventListener listener;

			@Override
			public void setTotal(boolean total) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setHierachyView(IHierarchyView hierarchyView) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean setGeneralEntity(Entity entity) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void setExclusive(boolean exclusive) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean removeSpecificEntity(Entity entity) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void create() {
				if (MockEntityControllerFactory.Entities.size() <= 3)
				{
					return;
				}
				
				Hierarchy hierarchy = new Hierarchy();
				
				for (Entity entity : MockEntityControllerFactory.Entities) {
					if (entity.getName().equalsIgnoreCase("Entity1")){
						hierarchy.setGeneralEntityId(entity.getId());
					}
					
					if (entity.getName().equalsIgnoreCase("Entity2") || 
							entity.getName().equalsIgnoreCase("Entity3") || 
							entity.getName().equalsIgnoreCase("Entity4")){
						try {
							hierarchy.addChildEntity(entity.getId());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				this.listener.handleCreatedEvent(hierarchy);
			}
			
			@Override
			public void addSuscriber(IHierarchyEventListener listener) {
				this.listener = listener;
			}
			
			@Override
			public boolean addSpecificEntity(Entity entity) throws Exception {
				return false;
			}
			
			@Override
			public boolean addHierarchy() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Iterable<Entity> getAvailableEntities() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean hasSpecificEntity(Entity entity) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isGeneralEntity(Entity entity) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void create(Hierarchy hierarchy) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean relationshipIsTotal() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean relationshipIsExclusive() {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}
}
