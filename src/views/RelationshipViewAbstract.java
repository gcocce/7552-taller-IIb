/*
 * Created by JFormDesigner on Sun Jul 15 01:06:54 ART 2012
 */

package views;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;

/**
 * @author Santiago Storti
 */
public abstract class RelationshipViewAbstract extends JFrame {
	public RelationshipViewAbstract(IRelationshipEntityView relEntView, IAttributeView attView) {
		initComponents((RelationshipEntityViewImpl) relEntView, (AttributeView) attView);
	}
	protected abstract  void setUpCompontenents (RelationshipEntityViewImpl relEntView, AttributeView attView) ;

	private void initComponents(RelationshipEntityViewImpl relEntView, AttributeView attView) {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Santiago Storti
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		labelName = new JLabel();
		textFieldName = new JTextField();
		separator1 = compFactory.createSeparator("");
		tabbedPane3 = new JTabbedPane();
		panelEntities = new JPanel();
		panelAttributes = new JPanel();
		Title = new JLabel();
		checkBox1 = new JCheckBox();
		button1 = new JButton();

		//======== this ========
		setTitle("Relationship Editor");
		Container contentPane = getContentPane();

		//---- labelName ----
		labelName.setText("Name:");

		//======== tabbedPane3 ========
		{

			//======== panelEntities ========
			{

				// JFormDesigner evaluation mark
//				panelEntities.setBorder(new javax.swing.border.CompoundBorder(
//					new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
//						"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
//						javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
//						java.awt.Color.red), panelEntities.getBorder())); panelEntities.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});
//
//
//				GroupLayout panelEntitiesLayout = new GroupLayout(panelEntities);
//				panelEntities.setLayout(panelEntitiesLayout);
//				panelEntitiesLayout.setHorizontalGroup(
//					panelEntitiesLayout.createParallelGroup()
//						.addGap(0, 680, Short.MAX_VALUE)
//				);
//				panelEntitiesLayout.setVerticalGroup(
//					panelEntitiesLayout.createParallelGroup()
//						.addGap(0, 337, Short.MAX_VALUE)
//				);
			}
			panelEntities = relEntView;
			tabbedPane3.addTab("Entities", panelEntities);


			//======== panelAttributes ========
			{

				GroupLayout panelAttributesLayout = new GroupLayout(panelAttributes);
				panelAttributes.setLayout(panelAttributesLayout);
				panelAttributesLayout.setHorizontalGroup(
					panelAttributesLayout.createParallelGroup()
						.addGap(0, 680, Short.MAX_VALUE)
				);
				panelAttributesLayout.setVerticalGroup(
					panelAttributesLayout.createParallelGroup()
						.addGap(0, 337, Short.MAX_VALUE)
				);
			}
			panelAttributes = attView.getFrame();
			tabbedPane3.addTab("Attributes", panelAttributes);

		}

		//---- Title ----
		Title.setText("Edit your Relationship");

		//---- checkBox1 ----
		checkBox1.setText("Composition");

		//---- button1 ----
		button1.setText("Finish");

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(separator1, GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(button1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
								.addGroup(contentPaneLayout.createParallelGroup()
									.addGroup(contentPaneLayout.createSequentialGroup()
										.addGap(339, 339, 339)
										.addComponent(checkBox1))
									.addGroup(contentPaneLayout.createSequentialGroup()
										.addGap(30, 30, 30)
										.addComponent(tabbedPane3, GroupLayout.PREFERRED_SIZE, 685, GroupLayout.PREFERRED_SIZE))))
							.addGap(0, 29, Short.MAX_VALUE))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(248, 248, 248)
							.addComponent(labelName, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addGap(18, 18, 18)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addComponent(Title)
									.addGap(0, 329, Short.MAX_VALUE))
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 295, Short.MAX_VALUE)))))
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addComponent(Title, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addGap(18, 18, 18)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelName))
					.addGap(26, 26, 26)
					.addComponent(checkBox1)
					.addGap(18, 18, 18)
					.addComponent(separator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18, 18, 18)
					.addComponent(tabbedPane3)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(button1)
					.addGap(18, 18, 18))
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Santiago Storti
	protected JLabel labelName;
	protected JTextField textFieldName;
	protected JComponent separator1;
	protected JTabbedPane tabbedPane3;
	protected JPanel panelEntities;
	protected JPanel panelAttributes;
	protected JLabel Title;
	protected JCheckBox checkBox1;
	protected JButton button1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
