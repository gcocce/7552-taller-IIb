package models;

import infrastructure.Func;
import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class AttributeCollection implements Iterable<Attribute> {

    private List<Attribute> items;

    public AttributeCollection() {
        super();
        items = new ArrayList<Attribute>();
    }

    public Attribute createItemInstance(String name) {
        return new Attribute(name);
    }

    public static Attribute createItemInstance(String name, Cardinality cardinality, IdGroupCollection idGroup, AttributeType type, String expression) {
        return new Attribute(name, cardinality, idGroup, type, expression);
    }

    public static Attribute createItemInstance(String name, Cardinality cardinality, IdGroupCollection idGroup, AttributeType type, String expression,
                                               AttributeCollection attCol, UUID myID) {

        return new Attribute(name, cardinality, idGroup,
                type, expression, attCol, myID);
    }

    public int count() {
        return IterableExtensions.count(this.items);
    }

    public Attribute getAttribute(String attributeName) {
        return IterableExtensions.firstOrDefault(this.items,
                new AttributeCmpFunc(), attributeName);
    }

    public void addAttribute(Attribute att) throws Exception {

        if (att == null)
            throw new NullPointerException("Null Attribute was sent");
        if (!existsAttribute(att.getName()))
            items.add(att);
        else
            throw new Exception("Name " + att.getName()
                    + " is used by another attribute");
    }

    public void addAttribute(String attName) throws Exception {
        this.addAttribute(this.createItemInstance(attName));
    }

    public void addAttribute(String name, Cardinality cardinality, IdGroupCollection idGroup, AttributeType type, String expression)
            throws Exception {

        this.addAttribute(AttributeCollection.createItemInstance(name,
                cardinality, idGroup, type,
                expression));
    }

    public void removeAttribute(String attributeName) throws Exception {
        if (existsAttribute(attributeName)) {
            this.items.remove(this.getAttribute(attributeName));
        } else {
            throw new Exception("Do not exists an attribute with name: "
                    + attributeName);
        }
    }

    public boolean existsAttribute(String attributeName) {
        return IterableExtensions.firstOrDefault(this.items,
                new AttributeCmpFunc(), attributeName) != null;
    }

    /**
     * Private class used by IterableExtensions
     */

    private class AttributeCmpFunc extends Func<Attribute, String, Boolean> {
        @Override
        public Boolean execute(Attribute attribute, String name) {
            return attribute.getName().equals(name);
        }

    }
    
    public List<Attribute> getAttributes() {
    	return this.items;
    }

    @Override
    public Iterator<Attribute> iterator() {
        return this.items.iterator();
    }
}
