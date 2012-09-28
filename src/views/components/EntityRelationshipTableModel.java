package views.components;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import views.RelationshipEntityViewImpl;

import models.Entity;

public class EntityRelationshipTableModel extends AbstractTableModel {

	private JTable table;
	private RelationshipEntityViewImpl view;

	private static final long serialVersionUID = 1L;

	private Class<?>[] columnTypes = new Class<?>[] { Entity.class,
			String.class, String.class, String.class, Boolean.class };

	private String[] columnNames = new String[] { "Entity", "Min. Card.",
			"Max. Card.", "Role", "Is Strong" };

	MyComboBox combo;

	private List<Object[]> listEntityRelationship;

	public EntityRelationshipTableModel(JTable table,
			Iterable<Entity> entities, RelationshipEntityViewImpl view) {
		super();
		this.table = table;
		this.view = view;
		listEntityRelationship = new ArrayList<Object[]>();
		combo = new MyComboBox(entities);
		setEditor();

	}

	private void addItems(List<Object[]> list) {
		for (Object[] obj : list)
			view.addRow(obj);
	}

	private void setEditor() {
		TableColumn col = table.getColumnModel().getColumn(0);
		col.setCellEditor(new DefaultCellEditor(combo));
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		col.setCellRenderer(renderer);
	}

	
	public void printModel() {
		System.out
				.println("-----------------------------------Table Model-----------------------------------");
		System.out.println("Row Count: " + this.getRowCount());
		for (int i = 0; i < this.getRowCount(); i++) {
			Object[] ob = listEntityRelationship.get(i);
			System.out.println(ob[0] + "," + ob[1] + "," + ob[2] + "," + ob[3]
					+ "," + ob[4]);
		}
	}

	public void removeRow(int selectedRow) {
		listEntityRelationship.remove(selectedRow);
	}

	public void addRow(Object[] objects) {
		if (getRowCount() == 0)
			setEditor();
		listEntityRelationship.add(objects);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnTypes[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return columnTypes.length;
	}

	@Override
	public int getRowCount() {
		return listEntityRelationship.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return listEntityRelationship.get(arg0)[arg1];
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return true;
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		(listEntityRelationship.get(arg1))[arg2] = arg0;
	}

	public static Object[] getNewRow() {
		return new Object[] { new String("None"), new String("0"), new String("0"),
				new String(""), new Boolean(false) };
	}

	@Override
	public String getColumnName(int arg0) {
		return columnNames[arg0];
	}

	private class MyComboBox extends JComboBox<Entity> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		public MyComboBox(Iterable<Entity> entities) {
			super();

			this.setRenderer(new EntityComboRenderer());
			for (Entity ent : entities)
				this.addItem(ent);
		}

		@Override
		public String toString() {
			return this.getSelectedItem().toString();

		}

		private class EntityComboRenderer extends BasicComboBoxRenderer {
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				String name = ((Entity) value).getName();

				setText(name);
				if (isSelected) {
					setBackground(list.getSelectionBackground());
					setForeground(list.getSelectionForeground());
				} else {
					setBackground(list.getBackground());
					setForeground(list.getForeground());
				}

				return this;
			}

		}
	}

	public void setModel(List<Object[]> relationshipEntities) {
		int cantCol = getRowCount ();
		while (getRowCount () > 0) {
			this.removeRow(getRowCount () -1);
			this.fireTableRowsDeleted(0,getRowCount());
		}
		
		addItems(relationshipEntities);
		this.fireTableRowsInserted(0, relationshipEntities.size());
		
	}

	public List<Object[]> getModelList() {
		return listEntityRelationship;
	}
}
