package controllers;

import java.util.List;

import models.Attribute;
import views.IAttributeView;

public interface IAttributeController {

    public Attribute addNewAttribute();

    public Iterable<Attribute> getAttributes();

    public IAttributeView getAttributeView();

    public void setAttributeView(IAttributeView attributeView);

    public Attribute addNewAttributeToAttribute(Attribute attributeSelected);

    public boolean editAttribute(Attribute attributeSelected);

    public void removeAttribute(Attribute attribute);

    public void setAttributes(List<Attribute> attributes);

    public boolean correctCardinality(String min, String max);
}
