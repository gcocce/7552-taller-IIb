package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import models.RelationshipEntity;

import controllers.IRelationshipEntityController;
import controllers.tests.mocks.MockProjectContext;
import controllers.tests.mocks.MockRelationshipEntityController;

import views.components.EntityRelationshipTableModel;

public class RelationshipEntityViewImpl extends RelationshipEntityViewAbstract implements IRelationshipEntityView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityRelationshipTableModel tableModel;
	private MyCellFocusListener listener;
	private IRelationshipEntityController controller;
	
	
	public static void main(String args[]) {
		JFrame vent = new JFrame();
		vent.setSize(800, 500);
		RelationshipEntityViewImpl view = new RelationshipEntityViewImpl();
		vent.add(view);
		
		MockProjectContext pContext = new MockProjectContext () ;
		
		MockRelationshipEntityController controller = new MockRelationshipEntityController(pContext);
		controller.setRelationshipEntityView(view);
		controller.createTestingList();
		view.setController(controller);
		
		vent.setVisible(true);
		vent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public RelationshipEntityViewImpl() {
		super();
		tableModel = null;
	}
	
	private void setUpComponents() {
		RelationshipEntity.setModel(tableModel);

		buttonAdd.addActionListener(new ButtonAddActionListener());

		buttonRemove.addActionListener(new ButtonRemoveActionListener());

		if (!(RelationshipEntity.getRowCount() > 0))
			buttonRemove.setEnabled(false);
		
		tableModel.addTableModelListener(new TableModelListenerImpl());
		
	}

	public void addRow (Object [] row) {
		System.out.println("Entro al listener");
		tableModel.addRow(row);
		System.out.println("Firetable");
		tableModel.fireTableRowsInserted(0,
				RelationshipEntity.getRowCount());
		
		//Set cell Editor listener
		for (int i = 0; i< RelationshipEntity.getColumnCount() ; i++) {
			RelationshipEntity.getCellEditor(RelationshipEntity.getRowCount()-1, i).addCellEditorListener(listener);
		}
		
		tableModel.fireTableRowsUpdated(0, RelationshipEntity.getRowCount());
		System.out.println("Add");
		tableModel.printModel();
	}
	
	@Override
	public IRelationshipEntityController getController() {
		return this.controller;
	}

	@Override
	public void setController(
			IRelationshipEntityController relationshipEntityController) {
		controller = relationshipEntityController;
		tableModel = new EntityRelationshipTableModel(this.RelationshipEntity,controller.getEntities(),this);
		listener = new MyCellFocusListener();
		setUpComponents();
		tableModel.setModel(controller.getListForModel());
	}

		
	
	@Override
	public List<Object[]> getModelList () {
		return tableModel.getModelList();
	}
	

	private void updateController() {
		try {
			controller.updateModel(tableModel.getModelList());
		
		}catch (Exception e) {
			//e.printStackTrace();
			showErrorDialog(e.getMessage());
			tableModel.setModel(controller.getListForModel());
		}
	
		System.out.println ("----------------------Modelo-----------------------");
		
		List<RelationshipEntity> list = controller.getRelationshipEntities();
		for (RelationshipEntity r : list) {
				
			System.out.println (r.getEntityId()+","+r.getCardinality().getMinimum()+","+r.getCardinality().getMaximum()+","+r.getRole()+","+r.isStrongEntity());
		}
		
	}
	
	private void showErrorDialog (String msg) {
		JFrame frame = new JFrame ();
		frame.setAlwaysOnTop(true);
		JOptionPane.showMessageDialog(frame, msg,"Error",JOptionPane.ERROR_MESSAGE);
	}
	
	/** Listeners Implementation
	 * 
	 * @author Santiago
	 *
	 */
		
	private class ButtonAddActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			addRow(EntityRelationshipTableModel.getNewRow());
			//updateController();
		}
		
	}

	private class ButtonRemoveActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (RelationshipEntity.getSelectedRow() >= 0
					&& tableModel.getRowCount() > 0
					&& RelationshipEntity.getSelectedRow() <= tableModel
							.getRowCount()) {
				tableModel.removeRow(RelationshipEntity.getSelectedRow());
				tableModel.fireTableRowsDeleted(0,
						RelationshipEntity.getRowCount());
			}
			tableModel.fireTableRowsUpdated(0, RelationshipEntity.getRowCount());
			updateController();
			System.out.println("Remove");
			tableModel.printModel();
		}
	}

	private class TableModelListenerImpl implements TableModelListener {
		@Override
		public void tableChanged(TableModelEvent e) {
			if (RelationshipEntity.getRowCount() == 0)
				buttonRemove.setEnabled(false);
			if (RelationshipEntity.getRowCount() > 0) {
				buttonRemove.setEnabled(true);
			}
			
		}
		
	}
	
	private class MyCellFocusListener implements CellEditorListener {
		
		
		
		public MyCellFocusListener () {}
			
			
		@Override
		public void editingStopped(ChangeEvent e) {
			if (RelationshipEntity.getRowCount()> 0) {
				tableModel.fireTableRowsUpdated(0, tableModel.getRowCount());
				updateController();
			}
			System.out.println(e.toString());
			tableModel.printModel();
		}

		

		@Override
		public void editingCanceled(ChangeEvent e) {
					
		}
		
	}


	
}
