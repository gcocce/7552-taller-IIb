package controllers.tests.mocks;

import controllers.IAttributeController;
import models.Attribute;
import models.AttributeType;
import views.IAttributeView;

import java.util.List;

import javax.swing.JPanel;

public class MockAttributeView implements IAttributeView {
    private IAttributeController controller;
    private String expression;
    private String name;
    private List<String> cardinality;
    private AttributeType attType;


    @Override
    public void setController(IAttributeController attributeController) {
        this.controller = attributeController;
    }

    @Override
    public void setAttributes(List<Attribute> attributes) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getInternalFrame() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return this.name;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> getCardinality() {
        return this.cardinality;
    }

    @Override
    public AttributeType getAttributeType() {
        return this.attType;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getExpression() {
        return this.expression;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IAttributeController getController() {
        return this.controller;
    }

    public void setAttType(AttributeType attType) {
        this.attType = attType;
    }

    public void setCardinality(List<String> cardinality) {
        this.cardinality = cardinality;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

	@Override
	public JPanel getFrame() {
		// TODO Auto-generated method stub
		return null;
	}
}
