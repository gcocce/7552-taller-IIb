/*
 * Created by JFormDesigner on Wed Jun 20 16:35:42 ART 2012
 */

package views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.jgoodies.forms.factories.*;

import com.jgoodies.forms.layout.*;
import controllers.IKeysController;
import models.IKey;
import models.IdGroup;
import models.IdGroupCollection;

public class KeyView extends JFrame implements IKeysView {

    private IKeysController controller;
    private Iterable<IKey> possibleKeys;
    private IdGroup idGroupSelected;
    private IKey keySelectedToAdd;
    private IKey keySelectedToRemove;
    private DefaultListModel idGroupListModel;
    private DefaultListModel currentKeysListModel;
    private DefaultListModel possibleKeysListModel;

    public KeyView() {
        initComponents();
        cleanView();

        this.idGroupListModel = new DefaultListModel();
        this.idGroupList.setModel(this.idGroupListModel);
        this.idGroupList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                cleanView();
                idGroupSelected = (IdGroup) idGroupList.getSelectedValue();
                if (idGroupSelected != null)
                    refreshKeyListWithIdGroup(idGroupSelected);
            }
        });
        this.idGroupList.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    controller.removeIdGroupFromAllIdGroups((IdGroup) idGroupList.getSelectedValue());
                    idGroupListModel.removeElement(idGroupList.getSelectedValue());
                    cleanView();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        this.currentKeysList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                keySelectedToRemove = (IKey) currentKeysList.getSelectedValue();
            }
        });
        this.possibleKeysList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                keySelectedToAdd = (IKey) possibleKeysList.getSelectedValue();
            }
        });

        this.finsihButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        this.newIdGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idGroupName = idGroupNameTextField.getText();
                if (controller.validIdGroupName(idGroupName) && !inModel(idGroupName)) {
                    idGroupListModel.addElement(new IdGroup(idGroupName));
                    cleanView();
                }
            }
        });

        this.addKeyToIdGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (keySelectedToAdd != null) {
                    controller.addIdGroupToKey();
                    currentKeysListModel.addElement(keySelectedToAdd);
                    possibleKeysListModel.removeElement(keySelectedToAdd);
                    updateKeysList();
                }
            }
        });

        this.removeKeyFromIdGriupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (keySelectedToRemove != null) {
                    controller.removeIdGroupFromKey();
                    possibleKeysListModel.addElement(keySelectedToRemove);
                    currentKeysListModel.removeElement(keySelectedToRemove);
                    updateKeysList();
                }
            }
        });
    }

    private boolean inModel(String idGroupName) {
        Integer modelSize = idGroupListModel.getSize();
        for (int i = 0; i < modelSize; i++) {
            IdGroup idGroup = (IdGroup) idGroupListModel.get(i);
            if (idGroup.getName().equals(idGroupName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void showView() {
        this.setVisible(true);
    }

    @Override
    public void setController(IKeysController controller) {
        this.controller = controller;
    }

    @Override
    public void setPossibleKeys(Iterable<IKey> keys) {
        this.possibleKeys = keys;
    }

    @Override
    public IdGroup getIdGroupSelected() {
        return this.idGroupSelected;
    }

    @Override
    public IKey getKeySelectedToAdd() {
        return this.keySelectedToAdd;
    }

    @Override
    public IKey getKeySelectedToRemove() {
        return this.keySelectedToRemove;
    }

    @Override
    public void setExistIdGroup(Iterable<IdGroup> idGroupFromKeys) {
        for (IdGroup idGroup : idGroupFromKeys) {
            this.idGroupListModel.addElement(idGroup);
        }
    }

    private void updateKeysList() {
        keySelectedToAdd = null;
        keySelectedToRemove = null;
        possibleKeysList.updateUI();
        currentKeysList.updateUI();
    }

    private void cleanView() {
        this.currentKeysListModel = new DefaultListModel();
        this.currentKeysList.setModel(this.currentKeysListModel);
        this.possibleKeysListModel = new DefaultListModel();
        this.possibleKeysList.setModel(this.possibleKeysListModel);
        this.idGroupNameTextField.setText("");
        this.idGroupSelected = null;
        idGroupList.revalidate();
        idGroupList.repaint();
        updateKeysList();
    }

    private void refreshKeyListWithIdGroup(IdGroup idGroupSelected) {
        String id = idGroupSelected.getName();
        for (IKey key : this.possibleKeys) {
            IdGroupCollection idGroupCollection = key.getIdGroup();
            if (idGroupCollection.exists(id)) {
                this.currentKeysListModel.addElement(key);
            } else {
                this.possibleKeysListModel.addElement(key);
            }

        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - santiago storti
        idGroupNameTextField = new JTextField();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        newIdGroupButton = new JButton();
        scrollPane1 = new JScrollPane();
        idGroupList = new JList();
        scrollPane2 = new JScrollPane();
        possibleKeysList = new JList();
        scrollPane3 = new JScrollPane();
        currentKeysList = new JList();
        addKeyToIdGroupButton = new JButton();
        removeKeyFromIdGriupButton = new JButton();
        finsihButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
                "64*(default, $lcgap), default",
                "57*(default, $lgap), default"));
        contentPane.add(idGroupNameTextField, CC.xywh(9, 3, 19, 5));

        //---- label1 ----
        label1.setText("Id Groups");
        contentPane.add(label1, CC.xy(5, 5));

        //---- label2 ----
        label2.setText("Possible keys");
        contentPane.add(label2, CC.xy(57, 7));

        //---- label3 ----
        label3.setText("Current keys");
        contentPane.add(label3, CC.xy(111, 7));

        //---- newIdGroupButton ----
        newIdGroupButton.setText("New id group");
        contentPane.add(newIdGroupButton, CC.xy(23, 9));

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(idGroupList);
        }
        contentPane.add(scrollPane1, CC.xywh(3, 11, 25, 102));

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(possibleKeysList);
        }
        contentPane.add(scrollPane2, CC.xywh(35, 11, 41, 102));

        //======== scrollPane3 ========
        {
            scrollPane3.setViewportView(currentKeysList);
        }
        contentPane.add(scrollPane3, CC.xywh(87, 11, 41, 100));

        //---- addKeyToIdGroupButton ----
        addKeyToIdGroupButton.setText("Add key to id group");
        contentPane.add(addKeyToIdGroupButton, CC.xy(81, 13));

        //---- removeKeyFromIdGriupButton ----
        removeKeyFromIdGriupButton.setText("Remove key from id group");
        contentPane.add(removeKeyFromIdGriupButton, CC.xy(81, 97));

        //---- finsihButton ----
        finsihButton.setText("Finish");
        contentPane.add(finsihButton, CC.xy(123, 113));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - santiago storti
    private JTextField idGroupNameTextField;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JButton newIdGroupButton;
    private JScrollPane scrollPane1;
    private JList idGroupList;
    private JScrollPane scrollPane2;
    private JList possibleKeysList;
    private JScrollPane scrollPane3;
    private JList currentKeysList;
    private JButton addKeyToIdGroupButton;
    private JButton removeKeyFromIdGriupButton;
    private JButton finsihButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
