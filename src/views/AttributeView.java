package views;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.jgoodies.forms.factories.*;

import com.jgoodies.forms.layout.*;

import controllers.IAttributeController;
import infrastructure.visual.AttributeTreeNode;
import models.Attribute;
import models.AttributeType;
import models.Cardinality;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AttributeView implements IAttributeView {

    private DefaultTreeModel attributeModel;

    public AttributeView() {
        initComponents();
        attributeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Attributes"));
        attributes.setModel(attributeModel);
        this.attributeSelected = null;
        attributes.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (attributes.getLastSelectedPathComponent() instanceof AttributeTreeNode) {
                    Attribute attribute = ((AttributeTreeNode) attributes.getLastSelectedPathComponent()).getAttribute();
                    attributeSelected(attribute);
                } else {
                    attributeSelected = null;
                }
            }
        });

        attributes.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (attributes.getLastSelectedPathComponent() instanceof AttributeTreeNode) {
                        AttributeTreeNode attributeTreeNode = (AttributeTreeNode) attributes.getLastSelectedPathComponent();
                        Object parent = ((MutableTreeNode) attributes.getLastSelectedPathComponent()).getParent();
                        if (parent instanceof AttributeTreeNode) {
                            ((AttributeTreeNode) parent).removeOwnAttribute(attributeTreeNode.getAttribute());
                        } else {
                            controller.removeAttribute(attributeTreeNode.getAttribute());
                        }
                        attributeModel.removeNodeFromParent((MutableTreeNode) attributes.getLastSelectedPathComponent());
                        cleanView();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        createAttributeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createAttribute();
            }
        });
        addToAttibuteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addAttributeToSelectedOne();
            }
        });
        editAttributeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editAttribute();
            }
        });
        this.type.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateTypeSelection();
            }
        });
        updateTypeSelection();
        cleanView();
    }

    private void updateTypeSelection() {
        boolean editable = (getAttributeType() != AttributeType.characterization);
        if (editable)
            expression.setBackground(Color.white);
        else
            expression.setBackground(Color.gray);
        expression.setEditable(editable);
    }

    @Override
    public void setController(IAttributeController attributeController) {
        this.controller = attributeController;
    }

    @Override
    public void setAttributes(List<Attribute> attributes) {
        this.attributeModelList = attributes;
        generateAttributeTree(this.attributeModelList, (DefaultMutableTreeNode) attributeModel.getRoot());
    }

    private void generateAttributeTree(Iterable<Attribute> attributes, DefaultMutableTreeNode node) {
        for (Attribute attribute : attributes) {
            DefaultMutableTreeNode newNode = new AttributeTreeNode(attribute);
            node.add(newNode);
            generateAttributeTree(attribute.getAttributes(), newNode);
        }
    }

    @Override
    public Object getInternalFrame() {
        return panel1;
    }

    public JPanel getFrame() {
        return panel1;
    }

    @Override
    public String getName() {
        return name.getText();
    }

    @Override
    public List<String> getCardinality() {
        List<String> card = new ArrayList<String>();
        card.add(this.minCardinality.getText());
        card.add(this.maxCardinality.getText());
        return card;
    }

    @Override
    public AttributeType getAttributeType() {
        return (AttributeType) type.getItemAt(type.getSelectedIndex());
    }

    @Override
    public String getExpression() {
        return this.expression.getText();
    }

    private void addAttributeToSelectedOne() {
        if (this.controller.correctCardinality(this.minCardinality.getText(), this.maxCardinality.getText())) {
            if (attributeSelected != null) {
                Attribute attribute = controller.addNewAttributeToAttribute(attributeSelected);
                if (attribute != null) {
                    ((AttributeTreeNode) attributes.getLastSelectedPathComponent()).add(new AttributeTreeNode(attribute));
                    cleanView();
                } else
                    showWrongAttributeDialog();
            }
        } else {
            this.showWrongCardinalityDialog();
        }

    }

    private void createAttribute() {
        if (this.controller.correctCardinality(this.minCardinality.getText(), this.maxCardinality.getText())) {
            Attribute attributeCreated = this.controller.addNewAttribute();
            if (attributeCreated != null) {
                ((DefaultMutableTreeNode) attributeModel.getRoot()).add(new AttributeTreeNode(attributeCreated));
                cleanView();
            } else
                showWrongAttributeDialog();
        } else {
            this.showWrongCardinalityDialog();
        }

    }

    private void cleanView() {
        this.name.setText("");
        this.expression.setText("");
        this.type.setSelectedIndex(0);
        this.maxCardinality.setText("1");
        this.minCardinality.setText("1");
        attributes.revalidate();
        attributes.repaint();
        attributes.updateUI();
        attributes.clearSelection();
    }

    private void attributeSelected(Attribute selectedValue) {
        if (selectedValue != null) {
            this.attributeSelected = selectedValue;
            this.name.setText(attributeSelected.getName());
            String expressionClone = (attributeSelected.getType() == AttributeType.calculated || attributeSelected.getType() == AttributeType.copy) ? attributeSelected.getExpression() : "";
            this.expression.setText(expressionClone);
            this.type.setSelectedItem(attributeSelected.getType());
            Cardinality cardinality = this.attributeSelected.getCardinality();
            if (cardinality != null) {
                minCardinality.setText(Cardinality.getStringForCardinality(cardinality.getMinimum()));
                maxCardinality.setText(Cardinality.getStringForCardinality(cardinality.getMaximum()));
            } else {
                minCardinality.setText("1");
                maxCardinality.setText("1");
            }
        }

    }

    private void editAttribute() {
        if (this.controller.correctCardinality(this.minCardinality.getText(), this.maxCardinality.getText())) {
            if (attributeSelected != null) {
                if (this.controller.editAttribute(attributeSelected)) {
                    attributeSelected = null;
                    cleanView();
                } else
                    showWrongAttributeDialog();
            }
        } else {
            this.showWrongCardinalityDialog();
        }
    }

    private void showWrongCardinalityDialog() {
        JOptionPane.showMessageDialog(null, "Please put a correct value for the attribute's cardinality. If you want to put Infinit value please write *");
    }

    private void showWrongAttributeDialog() {
        JOptionPane.showMessageDialog(null, "There is an attribute on the same level with name, please change it.");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - santiago storti
        panel1 = new JPanel();
        attributesText = new JLabel();
        nameText = new JLabel();
        name = new JTextField();
        scrollPane1 = new JScrollPane();
        attributes = new JTree();
        typeText = new JLabel();
        type = new JComboBox(AttributeType.values());
        expressionText = new JLabel();
        scrollPane2 = new JScrollPane();
        expression = new JTextArea();
        addToAttibuteButton = new JButton();
        createAttributeButton = new JButton();
        editAttributeButton = new JButton();
        label1 = new JLabel();
        label2 = new JLabel();
        minCardinality = new JTextField();
        minCardinality.setText("1");
        maxCardinality = new JTextField();
        maxCardinality.setText("1");
        label3 = new JLabel();

        //======== panel1 ========
        {

            panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent e) {
                    if ("border".equals(e.getPropertyName())) throw new RuntimeException();
                }
            });

            panel1.setLayout(new FormLayout(
                    "22*(default, $lcgap), 11*(default), 2*($lcgap, default), 3*(default), 3*($lcgap, default), 16*(default)",
                    "21*(default, $lgap), default"));

            //---- attributesText ----
            attributesText.setText("Attributes");
            panel1.add(attributesText, CC.xy(5, 1));

            //---- nameText ----
            nameText.setText("Name");
            panel1.add(nameText, CC.xy(66, 3));
            panel1.add(name, CC.xywh(70, 3, 5, 1));

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(attributes);
            }
            panel1.add(scrollPane1, CC.xywh(5, 5, 58, 21));

            //---- typeText ----
            typeText.setText("Type");
            panel1.add(typeText, CC.xy(66, 5));
            panel1.add(type, CC.xy(70, 5));

            //---- expressionText ----
            expressionText.setText("Expression");
            panel1.add(expressionText, CC.xy(66, 7));

            //======== scrollPane2 ========
            {
                scrollPane2.setViewportView(expression);
            }
            panel1.add(scrollPane2, CC.xywh(70, 7, 6, 6));

            //---- addToAttibuteButton ----
            addToAttibuteButton.setText("Add to selected attribute");
            panel1.add(addToAttibuteButton, CC.xy(70, 21));

            //---- createAttributeButton ----
            createAttributeButton.setText("Create Attribute");
            panel1.add(createAttributeButton, CC.xy(66, 21));

            //---- editAttributeButton ----
            editAttributeButton.setText("Edit Selected");
            panel1.add(editAttributeButton, CC.xy(72, 21));

            //---- label1 ----
            label1.setText("Cardinality");
            panel1.add(label1, CC.xy(5, 29));

            //---- label2 ----
            label2.setText("Min");
            panel1.add(label2, CC.xy(7, 33));
            panel1.add(minCardinality, CC.xywh(11, 30, 15, 6));
            panel1.add(maxCardinality, CC.xywh(11, 38, 15, 6));

            //---- label3 ----
            label3.setText("Max");
            panel1.add(label3, CC.xy(7, 41));
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - santiago storti
    private JPanel panel1;
    private JLabel attributesText;
    private JLabel nameText;
    private JTextField name;
    private JScrollPane scrollPane1;
    private JTree attributes;
    private JLabel typeText;
    private JComboBox type;
    private JLabel expressionText;
    private JScrollPane scrollPane2;
    private JTextArea expression;
    private JButton addToAttibuteButton;
    private JButton createAttributeButton;
    private JButton editAttributeButton;
    private JLabel label1;
    private JLabel label2;
    private JTextField minCardinality;
    private JTextField maxCardinality;
    private JLabel label3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private IAttributeController controller;
    private List<Attribute> attributeModelList;
    private Attribute attributeSelected;
}
