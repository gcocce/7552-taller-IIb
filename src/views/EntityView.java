/*
 * Created by JFormDesigner on Sat Jun 02 13:13:10 ART 2012
 */

package views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import controllers.IEntityController;
import models.EntityType;

public class EntityView implements IEntityView {
    private IEntityController entityController;
    private IAttributeView attributeView;
    private static String TITLE = "Entity Creation";

    public EntityView() {
        initComponents();
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createEntity();
            }
        });
        selectKeysButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                entityController.selectKeys();
            }
        });
        frame1.setTitle(TITLE);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - santiago storti
        frame1 = new JFrame();
        entityNameText = new JLabel();
        entityName = new JTextField();
        entityTypeText = new JLabel();
        entityTypeComboBox = new JComboBox(EntityType.values());
        panel1 = new JPanel();
        selectKeysButton = new JButton();
        createButton = new JButton();

        //======== frame1 ========
        {
            frame1.setTitle("Entity Creation");
            Container frame1ContentPane = frame1.getContentPane();
            frame1ContentPane.setLayout(new FormLayout(
                    "74*(default, $lcgap), default",
                    "73*(default, $lgap), default"));

            //---- entityNameText ----
            entityNameText.setText("Name: ");
            entityNameText.setLabelFor(entityName);
            frame1ContentPane.add(entityNameText, CC.xy(5, 3));
            frame1ContentPane.add(entityName, CC.xywh(9, 3, 41, 1));

            //---- entityTypeText ----
            entityTypeText.setText("Type");
            entityTypeText.setLabelFor(entityTypeComboBox);
            frame1ContentPane.add(entityTypeText, CC.xy(5, 7));
            frame1ContentPane.add(entityTypeComboBox, CC.xy(9, 7));

            //======== panel1 ========
            {

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                        panel1Layout.createParallelGroup()
                                .addGap(0, 1745, Short.MAX_VALUE)
                );
                panel1Layout.setVerticalGroup(
                        panel1Layout.createParallelGroup()
                                .addGap(0, 1269, Short.MAX_VALUE)
                );
            }
            frame1ContentPane.add(panel1, CC.xywh(5, 11, 145, 133));

            //---- selectKeysButton ----
            selectKeysButton.setText("Select Keys");
            frame1ContentPane.add(selectKeysButton, CC.xy(145, 147));

            //---- createButton ----
            createButton.setText("Finish");
            frame1ContentPane.add(createButton, CC.xy(149, 147));
            frame1.pack();
            frame1.setLocationRelativeTo(frame1.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void createEntity() {
        if (entityController.addEntity()) {
            this.frame1.setVisible(false);
        } else {
            showWrongEntityNameDialog();
        }
    }

    private void showWrongEntityNameDialog() {
        if (entityName.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "An entity must have a name, please complete it.", "Invalid Entity Name", JOptionPane.QUESTION_MESSAGE);
        } else
            JOptionPane.showMessageDialog(null, this.entityName.getText() + " already exists as an entity name on this project, please change it.", "Invalid Entity Name", JOptionPane.QUESTION_MESSAGE);
    }

    @Override
    public void setController(IEntityController entityController) {
        this.entityController = entityController;
    }

    @Override
    public void addAttributeView(IAttributeView attributeView) {
        this.attributeView = attributeView;
        frame1.remove(panel1);
        Container frame1ContentPane = frame1.getContentPane();
        frame1ContentPane.add((Component) attributeView.getInternalFrame(), CC.xywh(5, 11, 145, 133));

    }

    @Override
    public IAttributeView getAttributeView() {
        return this.attributeView;
    }

    @Override
    public String getEntityName() {
        return this.entityName.getText();
    }

    @Override
    public EntityType getEntityType() {
        return (EntityType) this.entityTypeComboBox.getItemAt(this.entityTypeComboBox.getSelectedIndex());
    }

    @Override
    public boolean isVisible() {
        return this.frame1.isVisible();
    }

    @Override
    public void setEntityName(String name) {
        this.entityName.setText(name);
    }

    @Override
    public void setEntityType(EntityType type) {
        entityTypeComboBox.setSelectedItem(type);
        entityTypeComboBox.repaint();
        entityTypeComboBox.updateUI();
    }

    @Override
    public void showView() {
        this.frame1.setVisible(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - santiago storti
    private JFrame frame1;
    private JLabel entityNameText;
    private JTextField entityName;
    private JLabel entityTypeText;
    private JComboBox entityTypeComboBox;
    private JPanel panel1;
    private JButton selectKeysButton;
    private JButton createButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

	@Override
	public void setModeUpdating() {
		this.frame1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}
