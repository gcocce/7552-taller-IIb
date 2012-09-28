/*
 * Created by JFormDesigner on Sat Jun 23 19:15:36 ART 2012
 */

package views;

import javax.swing.*;
import javax.swing.table.*;

/**
 * @author santiago storti
 */
public abstract class RelationshipEntityViewAbstract extends JPanel {
	public RelationshipEntityViewAbstract() {
		RelationshipEntity = new JTable();
		initComponents();
		
	}

	private void createUIComponents() {
		// TODO: add custom component creation code here
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - santiago storti
		createUIComponents();

		buttonAdd = new JButton();
		buttonRemove = new JButton();
		scrollPane2 = new JScrollPane();

		//======== this ========
		setBackground(UIManager.getColor("Button.disabledShadow"));

		// JFormDesigner evaluation mark
//		setBorder(new javax.swing.border.CompoundBorder(
//			new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
//				"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
//				javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
//				java.awt.Color.red), getBorder())); 
		addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});


		//---- buttonAdd ----
		buttonAdd.setText("Add");

		//---- buttonRemove ----
		buttonRemove.setText("Remove");

		//======== scrollPane2 ========
		{
			scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			//---- RelationshipEntity ----
			RelationshipEntity.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Entity", "Min. Card.", "Max. Card.", "Role", "Is Strong"
				}
			) {
				Class<?>[] columnTypes = new Class<?>[] {
					Object.class, String.class, String.class, String.class, Object.class
				};
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			RelationshipEntity.setCellSelectionEnabled(true);
			RelationshipEntity.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			scrollPane2.setViewportView(RelationshipEntity);
		}

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addContainerGap(135, Short.MAX_VALUE)
					.addComponent(buttonAdd, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
					.addGap(47, 47, 47)
					.addComponent(buttonRemove)
					.addGap(135, 135, 135))
				.addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 245, GroupLayout.PREFERRED_SIZE)
					.addGap(18, 18, 18)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(buttonAdd)
						.addComponent(buttonRemove))
					.addGap(0, 14, Short.MAX_VALUE))
		);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - santiago storti
	protected JButton buttonAdd;
	protected JButton buttonRemove;
	protected JScrollPane scrollPane2;
	protected JTable RelationshipEntity;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	

	
}
