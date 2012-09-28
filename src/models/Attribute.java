package models;

import java.util.UUID;

public class Attribute implements INameable, IKey {
    private String name;
    private Cardinality cardinality;
    private String expression;
    private AttributeType type;

    private IdGroupCollection idGroup;
    private AttributeCollection attributes;//TODO: cambiar esto para que pueda ser accedido;

    private UUID myID;

    public Attribute(String name) {
        this.setName(name);
        myID = UUID.randomUUID();
        this.idGroup = new IdGroupCollection();
        this.attributes = new AttributeCollection();
    }

    public Attribute(String name, Cardinality cardinality, IdGroupCollection idGroup,
                     AttributeType type, String expression) {

        this(name);
        this.setCardinality(cardinality);
        this.idGroup = idGroup == null ? new IdGroupCollection() : idGroup;
        this.type = type;

        if (expression != null) setExpression(expression); //Throws IllegalArgumentException

    }

    public Attribute(String name, Cardinality cardinality, IdGroupCollection idGroup,
                     AttributeType type, String expression, AttributeCollection attCol, UUID myID) {
        this(name, cardinality, idGroup, type, expression);
        if (attCol != null)
            this.attributes = attCol;
        if (myID != null) {
            this.myID = myID;
        }

    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public AttributeType getType() {
        return type;
    }

    public Cardinality getCardinality() {
        return cardinality;
    }

    public void setCardinality(Cardinality cardinality) {
        this.cardinality = cardinality;
    }

    public String getExpression() throws IllegalArgumentException {
        if (this.type == AttributeType.calculated
                || this.type == AttributeType.copy)
            return expression;
        else
            throw new IllegalArgumentException();
    }

    public void setExpression(String expression) throws IllegalArgumentException {
        if (this.type == AttributeType.calculated
                || this.type == AttributeType.copy)
            this.expression = expression;
        else
            throw new IllegalArgumentException();
    }

    @Override
    public IdGroupCollection getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(IdGroupCollection idGroup) {
        this.idGroup = idGroup;
    }

    public boolean isComposite() {
        return (attributes != null);
    }

    public AttributeCollection getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributeCollection attributes) {
        this.attributes = attributes;
    }

    public void setId(UUID id) {
        myID = id;
    }

    @Override
    public UUID getId() {
        return myID;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
