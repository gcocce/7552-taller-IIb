package infrastructure.visual;

import models.Attribute;

import javax.swing.tree.DefaultMutableTreeNode;

public class AttributeTreeNode extends DefaultMutableTreeNode {
    private Attribute attribute;

    public AttributeTreeNode(Attribute attribute) {
        this.attribute = attribute;
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    @Override
    public String toString() {
        return attribute.getName();
    }

    public void removeOwnAttribute(Attribute attribute) {
        try {
            this.attribute.getAttributes().removeAttribute(attribute.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
