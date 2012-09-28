package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import controllers.IAttributeController;
import controllers.IRelationshipController;
import controllers.IRelationshipEntityController;
import controllers.tests.mocks.MockAttributeController;
import controllers.tests.mocks.MockProjectContext;
import controllers.tests.mocks.MockRelationshipController;
import controllers.tests.mocks.MockRelationshipEntityController;

public class RelationshipViewImpl extends RelationshipViewAbstract implements
		IRelationshipView {

	IRelationshipController relController;
	IAttributeController attController;
	IRelationshipEntityController relEntController;
	RelationshipEntityViewImpl relEntView;
	IAttributeView attView;

	public RelationshipViewImpl(IRelationshipEntityView relEntView,
			IAttributeView attView) {
		super(relEntView, attView);
		setUpCompontenents((RelationshipEntityViewImpl) relEntView, (AttributeView) attView);
	}

	public static void main(String args[]) {

		AttributeView attView = new AttributeView();
		RelationshipEntityViewImpl relEntView = new RelationshipEntityViewImpl();

		MockAttributeController attController = new MockAttributeController();
		MockProjectContext pContext = new MockProjectContext();
		MockRelationshipEntityController relEntController = new MockRelationshipEntityController(
				pContext);
		relEntController.createTestingList();

		attView.setController(attController);
		attController.setAttributeView(attView);
		relEntView.setController(relEntController);
		relEntController.setRelationshipEntityView(relEntView);
		RelationshipViewImpl vent = null;
		try {

			vent = new RelationshipViewImpl(relEntView, attView);
			MockRelationshipController mockRelController = new MockRelationshipController();

			vent.setController(mockRelController);

			vent.setVisible(true);
			vent.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	@Override
	protected void setUpCompontenents(RelationshipEntityViewImpl relEntView,
			AttributeView attView) {
		// add listeners
//		checkBox1.addItemListener(new CheckListener());
//		this.textFieldName.addActionListener(new MyLabelNameListener());
//		this.textFieldName.addFocusListener(new MyTextBoxLister());
//		this.button1.addActionListener(new ActionAdd());
		this.attView = attView;
		this.relEntView = relEntView;
			
	}

	@Override
	public IRelationshipController getController() {
		return relController;
	}

	@Override
	public void setController(IRelationshipController relationshipController) {
		relController = relationshipController;
		this.textFieldName.setText(relController.getName());
		this.checkBox1.setSelected(relController.isComposition());
		checkBox1.addItemListener(new CheckListener());
		MyTextBoxListener listener = new MyTextBoxListener();
		this.textFieldName.addActionListener(listener);
		this.textFieldName.addFocusListener(listener);
		this.textFieldName.addKeyListener(listener);
		this.button1.addActionListener(new ActionAdd());
	}

	@Override
	public void addRelationshipEntityView(RelationshipEntityViewImpl relEntView) {
		this.relEntView = relEntView;
		this.panelEntities = relEntView;
	}

	@Override
	public void addAttributeView(IAttributeView attview) {
		this.attView = attview;
		this.panelAttributes = attview.getFrame();
	}

	private void showErrorDialog(String msg) {
		JFrame frame = new JFrame();
		frame.setAlwaysOnTop(true);
		JOptionPane.showMessageDialog(frame, msg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	private class CheckListener implements ItemListener {
		/** Pregunta al controlador si debo poder seleccionar el item */
		@Override
		public void itemStateChanged(ItemEvent e) {
			try {
				relController.isComposition(checkBox1.isSelected());
			} catch (Exception exception) {
				//exception.printStackTrace();
				showErrorDialog(exception.getMessage());
				checkBox1.setSelected(false);
                
			}
		}
	}

	private class ActionAdd implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				relController.add();
			} catch (Exception e1) {
				//e1.printStackTrace();
				showErrorDialog(e1.getMessage());
          	}
		}
	}
	
	private class MyTextBoxListener implements FocusListener, ActionListener, KeyListener {

		@Override
		public void focusGained(FocusEvent e) {}

		@Override
		public void focusLost(FocusEvent e) {
			relController.setName(textFieldName.getText());
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			relController.setName(textFieldName.getText());
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			relController.setName(textFieldName.getText());
		}
	}

	@Override
	public IAttributeView getAttributeView() {
		return this.attView;
	}

	@Override
	public IRelationshipEntityView getRelationshipEntityView() {
		return this.relEntView;
	}

}
