package models;

import java.util.UUID;

import infrastructure.Func;
import infrastructure.IterableExtensions;

public class EntityCollection extends ModelCollection<Entity>{
	
	public EntityCollection () {
		super ();
	}
	
	
	public boolean add(String entityName, EntityType entityType) {
		return this.add(new Entity(entityName, entityType));
	}
	
	public boolean add(Entity entity)
	{
		if (this.get(entity.getName()) == null)
		{
			this.items.add(entity);
		}
		return false;
	}
	
	public Entity get(UUID id) {
		return IterableExtensions.firstOrDefault(this.items,
				new Func<Entity, String, Boolean>() {
					@Override
					public Boolean execute(Entity item, String id) {
						return item.getId().toString().equals(id);
					}
				}, id.toString());
	}
	
	@Override
	public Entity createItemInstance (String entityName) {
		return new Entity(entityName);
	}	
}
