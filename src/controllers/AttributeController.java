package controllers;

import infrastructure.Func;
import infrastructure.IProjectContext;
import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Attribute;
import models.AttributeCollection;
import models.AttributeType;
import models.Cardinality;
import models.IdGroupCollection;
import views.IAttributeView;

public class AttributeController extends BaseController implements IAttributeController {

    private IAttributeView attributeView;
    private List<Attribute> attributes;

    public AttributeController(IProjectContext projectContext, IAttributeView attributeView) {
        super(projectContext);
        this.attributes = new ArrayList<Attribute>();
        this.setAttributeView(attributeView);
    }

    @Override
    public Attribute addNewAttribute() {
        String attName = attributeView.getName();
        if (IterableExtensions.firstOrDefault(attributes, new FuncAttrCmp(), attName) == null && !attName.equals("")) {
            String expressionClone = (attributeView.getAttributeType() == AttributeType.calculated || attributeView.getAttributeType() == AttributeType.copy) ? attributeView.getExpression() : null;
            Attribute att = new Attribute(attributeView.getName(), this.getCardinalityFromView(), new IdGroupCollection(), attributeView.getAttributeType(), expressionClone);
            this.attributes.add(att);
            return att;
        }
        return null;
    }

    @Override
    public Iterable<Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
        this.attributeView.setAttributes(attributes);
    }

    @Override
    public boolean correctCardinality(String min, String max) {
        if (!min.equals("") && !max.equals("")) {
            if (isACorrectCardinalityNumber(min) && isACorrectCardinalityNumber(max)) {
                return Double.parseDouble(min) <= Double.parseDouble(max);
            } else {
                return ((isACorrectCardinalityNumber(min) && max.equals("*")) || (min.equals("*") && max.equals("*")));
            }
        }
        return false;
    }

    private boolean isACorrectCardinalityNumber(String text) {
        Pattern p = Pattern.compile("([0-9]*)\\.[0]");
        Pattern p2 = Pattern.compile("([0-9]*)");
        Matcher m = p.matcher(text);
        Matcher m2 = p2.matcher(text);
        return m.matches() || m2.matches();
    }

    @Override
    public IAttributeView getAttributeView() {
        return this.attributeView;
    }

    @Override
    public void setAttributeView(IAttributeView attributeView) {
        this.attributeView = attributeView;
        this.attributeView.setController(this);
        this.attributeView.setAttributes(this.attributes);
    }

    @Override
    public Attribute addNewAttributeToAttribute(Attribute attributeSelected) {
        Attribute att = null;
        if (attributeSelected != null) {
            String attName = attributeView.getName();
            if (!attName.equals("") && IterableExtensions.firstOrDefault(attributeSelected.getAttributes(), new FuncAttrCmp(), attName) == null) {
                String expressionClone = (attributeView.getAttributeType() == AttributeType.calculated || attributeView.getAttributeType() == AttributeType.copy) ? attributeView.getExpression() : null;
                Cardinality cardinality = getCardinalityFromView();
                att = new Attribute(attName, cardinality, new IdGroupCollection(), attributeView.getAttributeType(), expressionClone);
                try {
                    AttributeCollection attributeCollection = attributeSelected.getAttributes();
                    if (attributeCollection == null) {
                        attributeCollection = new AttributeCollection();
                        attributeSelected.setAttributes(attributeCollection);
                    }
                    attributeCollection.addAttribute(att);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return att;
    }

    private Cardinality getCardinalityFromView() {
        Cardinality cardinality = null;
        List<String> card = attributeView.getCardinality();
        try {
            if (this.correctCardinality(card.get(0), card.get(1))) {
                cardinality = this.getCardinality(card);
            } else {
                cardinality = new Cardinality(1, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cardinality;
    }

    private Cardinality getCardinality(List<String> cardinality) throws Exception {
        Double min = Cardinality.getCardinalityFromString(cardinality.get(0));
        Double max = Cardinality.getCardinalityFromString(cardinality.get(1));
        return new Cardinality(min, max);

    }

    @Override
    public boolean editAttribute(Attribute attributeSelected) {
        String attName = attributeView.getName();
        if (!attName.equals("") && ((IterableExtensions.firstOrDefault(this.attributes, new FuncAttrCmp(), attName) == null) || attName.equals(attributeSelected.getName()))) {
            attributeSelected.setName(attributeView.getName());
            attributeSelected.setCardinality(this.getCardinalityFromView());
            attributeSelected.setType(attributeView.getAttributeType());
            AttributeType attType = attributeView.getAttributeType();
            if (attType == AttributeType.calculated || attType == AttributeType.copy)
                attributeSelected.setExpression(attributeView.getExpression());
            return true;
        }
        return false;
    }

    @Override
    public void removeAttribute(Attribute attribute) {
        if (this.attributes.contains(attribute))
            this.attributes.remove(attribute);
    }

    private class FuncAttrCmp extends Func<Attribute, String, Boolean> {
        @Override
        public Boolean execute(Attribute attribute, String s) {
            return attribute.getName().equals(s);
        }
    }
}