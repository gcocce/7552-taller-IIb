package controllers.tests;

import controllers.AttributeController;
import controllers.tests.mocks.*;
import infrastructure.IterableExtensions;
import junit.framework.Assert;
import models.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AttributeControllerTest {

    private MockAttributeView mockAttributeView;
    private MockProjectContext mockProjectContext;
    private AttributeController attributeController;
    
    @Before
    public void setUp() throws Exception {
        mockAttributeView = new MockAttributeView();
        mockProjectContext = new MockProjectContext();
        attributeController = new AttributeController(mockProjectContext, mockAttributeView);
    }

    @Test
    public void TestCreate() {
        Assert.assertEquals(mockAttributeView.getController(), attributeController);
        Assert.assertEquals(0, IterableExtensions.count(attributeController.getAttributes()));
    }

    @Test
    public void testAddNewAttribute() {
        MockAttributeView mockAttributeView = new MockAttributeView();
        mockAttributeView.setName("att");
        List<String> card = new ArrayList<String>();
        card.add("1");
        card.add("1");
        mockAttributeView.setCardinality(card);
        mockAttributeView.setAttType(AttributeType.calculated);
        mockAttributeView.setExpression("aaksda");

        attributeController.setAttributeView(mockAttributeView);

        Assert.assertEquals(0, IterableExtensions.count(this.attributeController.getAttributes()));

        this.attributeController.addNewAttribute();

        Assert.assertEquals(1, IterableExtensions.count(this.attributeController.getAttributes()));

        this.attributeController.addNewAttribute();

        Assert.assertEquals(1, IterableExtensions.count(this.attributeController.getAttributes()));

        Attribute attribute = this.attributeController.getAttributes().iterator().next();
        Assert.assertEquals("att", attribute.getName());
        Assert.assertEquals(AttributeType.calculated, attribute.getType());

        mockAttributeView.setName("att2");

        this.attributeController.addNewAttribute();

        Assert.assertEquals(2, IterableExtensions.count(this.attributeController.getAttributes()));

        mockAttributeView.setName("");

        this.attributeController.addNewAttribute();

        Assert.assertEquals(2, IterableExtensions.count(this.attributeController.getAttributes()));

    }

    @Test
    public void testAddNewAttributeToAttribute() {
        Attribute attribute = new Attribute("name");
        MockAttributeView mockAttributeView = new MockAttributeView();
        mockAttributeView.setName("att");
        List<String> card = new ArrayList<String>();
        card.add("1");
        card.add("1");
        try {
            mockAttributeView.setCardinality(card);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mockAttributeView.setAttType(AttributeType.calculated);
        mockAttributeView.setExpression("aaksda");
        attributeController.setAttributeView(mockAttributeView);
        attributeController.addNewAttributeToAttribute(attribute);

        Assert.assertTrue(attribute.getAttributes().existsAttribute("att"));
        Assert.assertEquals(1, IterableExtensions.count(attribute.getAttributes()));

        attributeController.addNewAttributeToAttribute(attribute);
        Assert.assertEquals(1, IterableExtensions.count(attribute.getAttributes()));

        mockAttributeView.setName("");
        attributeController.addNewAttributeToAttribute(attribute);
        Assert.assertEquals(1, IterableExtensions.count(attribute.getAttributes()));

    }

    @Test
    public void testEditAttribute() {
        List<Attribute> attributes = new ArrayList<Attribute>();
        Attribute a1 = new Attribute("a1");
        Attribute a2 = new Attribute("a2");
        attributes.add(a1);
        attributes.add(a2);
        this.attributeController.setAttributes(attributes);

        Attribute attribute = new Attribute("name");

        MockAttributeView mockAttributeView = new MockAttributeView();
        mockAttributeView.setName("att");
        List<String> card = new ArrayList<String>();
        card.add("1");
        card.add("1");
            mockAttributeView.setCardinality(card);
        mockAttributeView.setAttType(AttributeType.calculated);
        mockAttributeView.setExpression("aaksda");
        attributeController.setAttributeView(mockAttributeView);


        attributeController.editAttribute(attribute);

        Assert.assertEquals(attribute.getName(), "att");
        Assert.assertEquals(attribute.getType(), AttributeType.calculated);
        Assert.assertEquals(attribute.getExpression(), "aaksda");
        Assert.assertEquals(attribute.getCardinality().getMinimum(), 1.0);
        Assert.assertEquals(attribute.getCardinality().getMaximum(), 1.0);

        mockAttributeView.setName("");
        attributeController.editAttribute(attribute);
        Assert.assertEquals(attribute.getName(), "att");

        mockAttributeView.setName("a1");
        Assert.assertFalse(attributeController.editAttribute(attribute));
    }

    @Test
    public void testRemoveAttribute() {
        List<Attribute> attributes = new ArrayList<Attribute>();
        Attribute a1 = new Attribute("a1");
        Attribute a2 = new Attribute("a2");
        attributes.add(a1);
        attributes.add(a2);
        this.attributeController.setAttributes(attributes);

        attributeController.removeAttribute(a1);
        Assert.assertFalse(attributes.contains(a1));

        attributeController.removeAttribute(a2);
        Assert.assertFalse(attributes.contains(a2));

    }
}
