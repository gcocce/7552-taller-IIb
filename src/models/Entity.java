package models;

import java.util.UUID;

public class Entity implements INameable, IStrongEntity {

    private String name;
    private UUID id;
    private EntityType type;
    private AttributeCollection attributes;
    

    public Entity(String name) {
        this(name, UUID.randomUUID(), EntityType.None, new AttributeCollection());
    }

    public Entity(String name, EntityType type) {
        this(name, UUID.randomUUID(), type, new AttributeCollection());
    }

    public Entity(String name, UUID id, EntityType type) {
        this(name, id, type, new AttributeCollection());
    }

    public Entity(String name, EntityType type, AttributeCollection attributes) {
        this(name, UUID.randomUUID(), type, attributes);
    }

    public Entity(String name, UUID id, EntityType type, AttributeCollection attributes) {
        super();
        this.setName(name);
        this.setType(type);
        this.id = id;
        this.attributes = attributes;
       
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public EntityType getType() {
        return this.type;
    }

    public AttributeCollection getAttributes() {
        return this.attributes;
    }

    
    public void setAttributes(AttributeCollection attributeCollection){
        this.attributes = attributeCollection;
    }
 
    @Override
    public String toString()
    {
    	return this.name;
    }
}
