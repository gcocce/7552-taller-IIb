package views;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.plaf.basic.BasicArrowButton;

import models.Entity;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import controllers.IHierarchyController;

public class HierarchyView implements IHierarchyView{

	private IHierarchyController hierarchyController;
	private List<Entity> availableEntities;
	private static String TITLE = "Hierarchy Creation";
	private JFrame frame1; 
	private JLabel generalEntityLabel;
	private JLabel specificEntitiesLabel;
	private JLabel availableEntitiesLabel;
	private JList<Entity> lstAvailableEntities;
	private JList<Entity> lstSpecificEntities;
	private JButton arrAddSpecificEntities;
	private JButton arrRemoveSpecificEntities;
	private JComboBox comBoxGeneralEntity;
	private JRadioButton btnTotal;
	private JRadioButton btnParcial;
	private JRadioButton btnExclusive;
	private JRadioButton btnOverlap;
	private JButton createHierarchy;
	private JButton cancel;
	
	public HierarchyView()
	{
		this.availableEntities = new ArrayList<Entity>();
		// frame
		this.frame1 = new JFrame(HierarchyView.TITLE);
		this.frame1.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		Container container = frame1.getContentPane();
		container.setLayout(new FormLayout(
                "70*(default, $lcgap), default",
                "68*(default, $lgap), default"));
		
		// general entity
		this.comBoxGeneralEntity = new JComboBox();
		this.comBoxGeneralEntity.addItem("None");
		this.generalEntityLabel = new JLabel("Entidad General");
		this.generalEntityLabel.setLabelFor(this.comBoxGeneralEntity);
		container.add(this.generalEntityLabel, CC.xywh(8, 4, 40, 10));
		container.add(this.comBoxGeneralEntity, CC.xywh(8, 14, 40, 10));
		
		// specific entities
		this.specificEntitiesLabel = new JLabel("Entidades Específicas");
		this.availableEntitiesLabel = new JLabel("Entidades Disponibles");
		this.lstAvailableEntities = new JList<Entity>(new DefaultListModel<Entity>());
		this.lstSpecificEntities = new JList<Entity>(new DefaultListModel<Entity>());
		this.availableEntitiesLabel.setLabelFor(this.lstAvailableEntities);
		this.specificEntitiesLabel.setLabelFor(this.lstSpecificEntities);
		container.add(this.availableEntitiesLabel, CC.xywh(8, 30, 50, 10));
		container.add(this.lstAvailableEntities, CC.xywh(8, 40, 50, 70));
		container.add(this.specificEntitiesLabel, CC.xywh(86, 30, 50, 10));
		container.add(this.lstSpecificEntities, CC.xywh(86, 40, 50, 70));
		
		// arrows
		this.arrAddSpecificEntities = new BasicArrowButton(BasicArrowButton.EAST);
		container.add(this.arrAddSpecificEntities, CC.xywh(68, 55, 8, 15));
		
		this.arrRemoveSpecificEntities = new BasicArrowButton(BasicArrowButton.WEST);
		container.add(this.arrRemoveSpecificEntities, CC.xywh(68, 75, 8, 15));
		
		// Button group
		this.btnTotal = new JRadioButton("Total");
		container.add(this.btnTotal, CC.xywh(64, 8, 30, 10));
		
		this.btnParcial = new JRadioButton("Parcial");
		container.add(this.btnParcial, CC.xywh(64, 18, 30, 10));
		
		this.btnExclusive = new JRadioButton("Exclusive");
		container.add(this.btnExclusive, CC.xywh(94, 8, 30, 10));
		
		this.btnOverlap = new JRadioButton("Overlap");
		container.add(this.btnOverlap, CC.xywh(94, 18, 30, 10));
		
		ButtonGroup group1 = new ButtonGroup();
		ButtonGroup group2 = new ButtonGroup();
		
		group1.add(this.btnTotal);
		group1.add(this.btnParcial);
		group2.add(this.btnExclusive);
		group2.add(this.btnOverlap);
		
		// accept and cancel
		this.createHierarchy = new JButton("Aceptar");
		container.add(this.createHierarchy, CC.xywh(106, 120, 30, 10));
		
		this.cancel = new JButton("Cancelar");
		container.add(this.cancel, CC.xywh(74, 120, 30, 10));
	}
	
	@Override
	public boolean isVisible() {
		return this.frame1.isVisible();
	}

	@Override
	public void setController(IHierarchyController controller) {
		this.hierarchyController = controller;
		
		// accept and cancel
		this.createHierarchy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (hierarchyController.addHierarchy())
					frame1.setVisible(false);
			}
		});
		this.cancel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				frame1.dispose();
			}
		});
		
		// arrows
		this.arrAddSpecificEntities.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultListModel<Entity> listModelSpecificEntities = (DefaultListModel<Entity>) lstSpecificEntities.getModel();
				DefaultListModel<Entity> listModelAvailableEntities = (DefaultListModel<Entity>) lstAvailableEntities.getModel();
				
				for (Object o : lstAvailableEntities.getSelectedValuesList())
				{
					Entity entity = (Entity) o;
					try {
						if (hierarchyController.addSpecificEntity(entity)) {
							comBoxGeneralEntity.removeItem(entity);
							listModelAvailableEntities.removeElement(entity);
							listModelSpecificEntities.addElement(entity);
						}
					} catch (Exception e1) {
					}
				}
				checkCountSpecificEntities(listModelSpecificEntities.size());
			}
		});
		this.arrRemoveSpecificEntities.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultListModel<Entity> listModelSpecificEntities = (DefaultListModel<Entity>) lstSpecificEntities.getModel();
				DefaultListModel<Entity> listModelAvailableEntities = (DefaultListModel<Entity>) lstAvailableEntities.getModel();
				for (Object o : lstSpecificEntities.getSelectedValuesList())
				{
					Entity entity = (Entity) o;
					listModelSpecificEntities.removeElement(entity);
					listModelAvailableEntities.addElement(entity);
					comBoxGeneralEntity.addItem(entity);
					hierarchyController.removeSpecificEntity(entity);
				}
				checkCountSpecificEntities(listModelSpecificEntities.getSize());
			}
		});
		
		// button group
		this.btnTotal.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					hierarchyController.setTotal(true);
				else
					hierarchyController.setTotal(false);
			}
		});
		this.btnExclusive.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					hierarchyController.setExclusive(true);
				else
					hierarchyController.setExclusive(false);
			}
		});
		
		// general entity
		this.comBoxGeneralEntity.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox comBox = (JComboBox) e.getSource();
				
				DefaultComboBoxModel combGeneralMdl = (DefaultComboBoxModel) comBoxGeneralEntity.getModel();
				DefaultListModel<Entity> lstAvailableMdl = (DefaultListModel<Entity>) lstAvailableEntities.getModel();
				
				for (Entity entity : availableEntities)
					if (combGeneralMdl.getIndexOf(entity) != -1 && lstAvailableMdl.indexOf(entity) == -1)
						lstAvailableMdl.addElement(entity);
				
				if (comBox.getSelectedIndex() != 0)
				{
					Entity entity = (Entity) comBox.getSelectedItem();
					hierarchyController.setGeneralEntity(entity);
					lstAvailableMdl.removeElement(entity);
				}else{
					hierarchyController.setGeneralEntity(null);
				}
			}
		});
	}

	private void checkCountSpecificEntities(int size) {
		if (size == 1) {
			this.btnParcial.doClick();
			this.btnExclusive.doClick();
			this.btnParcial.setEnabled(false);
			this.btnTotal.setEnabled(false);
			this.btnExclusive.setEnabled(false);
			this.btnOverlap.setEnabled(false);
		}else {
			this.btnParcial.setEnabled(true);
			this.btnTotal.setEnabled(true);
			this.btnExclusive.setEnabled(true);
			this.btnOverlap.setEnabled(true);
		}
	}
	
	@Override
	public void showView() {
		this.frame1.pack();
		this.frame1.setVisible(true);
	}

	@Override
	public void update() {
		this.cancel.setVisible(false);
		this.frame1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.getAvailableEntities();
		//specific entities
		DefaultListModel<Entity> lstAvailableMdl = (DefaultListModel<Entity>) this.lstAvailableEntities.getModel();
		DefaultListModel<Entity> lstSpecificMdl = (DefaultListModel<Entity>) this.lstSpecificEntities.getModel();

		for (Entity entity : this.availableEntities) {
			if (this.hierarchyController.hasSpecificEntity(entity)) {
				lstSpecificMdl.addElement(entity);
				continue;
			}
			this.comBoxGeneralEntity.addItem(entity);
			if (this.hierarchyController.isGeneralEntity(entity)) {
				this.comBoxGeneralEntity.setSelectedItem(entity);
				continue;
			}
			lstAvailableMdl.addElement(entity);
		}
		
		//Radiobuttoms
		if (this.hierarchyController.relationshipIsTotal())
			this.btnTotal.doClick();
		else
			this.btnParcial.doClick();
		
		if (this.hierarchyController.relationshipIsExclusive())
			this.btnExclusive.doClick();
		else
			this.btnOverlap.doClick();
	}

	@Override
	public void create() {
		this.getAvailableEntities();
		//general entity and available entities
		DefaultListModel<Entity> listModel = (DefaultListModel<Entity>) this.lstAvailableEntities.getModel();
		for (Entity entity : this.availableEntities)
		{
			this.comBoxGeneralEntity.addItem(entity);
			listModel.addElement(entity);
		}
	}
	
	private List<Entity> getAvailableEntities() {
		for (Entity entity : this.hierarchyController.getAvailableEntities()) {
			this.availableEntities.add(entity);
		}
		return null;
	}

}
