package models.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import models.INameable;
import models.der.AttributeCollection;
import models.der.EntityType;
import models.der.IStrongEntity;


public class DomainClass implements INameable, IStrongEntity {

    private String name;
    private UUID id;
    private Collection<DomainAttribute> attributes;
    
    public DomainClass(String name, UUID id, EntityType type, AttributeCollection attributes) {
        this.setName(name);
        this.id = id;
        this.attributes = new ArrayList<DomainAttribute>();
       
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

    public Collection<DomainAttribute> getAttributes() {
        return this.attributes;
    }

    
    public void addAttribute(DomainAttribute attribute){
        this.attributes.add(attribute);
    }
 
    @Override
    public String toString()
    {
    	return this.name;
    }
}
