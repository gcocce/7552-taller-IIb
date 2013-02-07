package views;

import java.awt.Insets;
import java.awt.Point;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.TooManyListenersException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.mxgraph.swing.mxGraphComponent;

import controllers.IDomainDiagramController;

public class DomainDiagramView extends JPanel implements IDomainDiagramView,
		DropTargetListener {

	private static final long serialVersionUID = 1385530682356119350L;

	private IDomainDiagramController diagramController;
	private final JButton btnSave;
	private mxGraphComponent graphComponent;
	private final JButton btnPrint;
	private JButton btnZoomIn;
	private JButton btnZoomOut;

	/**
	 * Create the panel.
	 */
	public DomainDiagramView() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow") }, 
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.GLUE_ROWSPEC }));

		Insets inset = new Insets(2, 35, 2, 35);

		this.btnSave = new JButton("Save");
		this.btnSave.setMargin(inset);
		add(this.btnSave, "2, 2");

		this.btnPrint = new JButton("Print");
		this.btnPrint.setMargin(inset);
		add(this.btnPrint, "3, 2");

		btnZoomIn = new JButton("+");
		add(btnZoomIn, "4, 2");

		btnZoomOut = new JButton("-");
		add(btnZoomOut, "5, 2");

	}

	@Override
	public void setController(IDomainDiagramController controller) {
		this.diagramController = controller;

		this.graphComponent = new mxGraphComponent(
				this.diagramController.getGraph());
		this.graphComponent.setAutoScroll(true);
		this.graphComponent.setAutoscrolls(true);
		// cannot create new arrows clicking from entity
		this.graphComponent.getConnectionHandler().setCreateTarget(false);
		this.graphComponent.setConnectable(false);

		try {
			this.graphComponent.getDropTarget().addDropTargetListener(this);
		} catch (TooManyListenersException e1) {
			// should not occur
			e1.printStackTrace();
		}

		this.add(this.graphComponent, "2, 4, 5, 1, fill, fill");

		this.btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					diagramController.save();
				} catch (ParserConfigurationException exception) {
					exception.printStackTrace();
				}
			}
		});

		this.btnPrint.addActionListener(new PrintAction());

		this.btnZoomIn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				graphComponent.zoomIn();
			}
		});

		this.btnZoomOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				graphComponent.zoomOut();
			}
		});
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		Point point = dtde.getLocation();
		this.diagramController.handleDragStart(point);
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		Point point = dtde.getLocation();
		this.diagramController.handleDrop(point);
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	@Override
	public void refreshGraphComponent() {
		this.graphComponent.refresh();
	}

	@Override
	public boolean showDeleteDialog(String typeAndName, String otherMessege,
			boolean couldDelete) {
		int result = -1;
		if (couldDelete == false)
			JOptionPane.showMessageDialog(null, "The selected " + typeAndName
					+ otherMessege, "Deleting invalid " + typeAndName,
					JOptionPane.QUESTION_MESSAGE);
		else
			result = JOptionPane.showConfirmDialog(null, "The " + typeAndName
					+ " are being deleted, are you shure you want this?",
					"Deleting " + typeAndName, JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}

	private class PrintAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			PrinterJob pj = PrinterJob.getPrinterJob();

			if (pj.printDialog()) {
				PageFormat pf = graphComponent.getPageFormat();
				Paper paper = new Paper();
				double margin = 36;
				paper.setImageableArea(margin, margin, paper.getWidth()
						- margin * 2, paper.getHeight() - margin * 2);
				pf.setPaper(paper);
				pj.setPrintable(graphComponent, pf);

				try {
					pj.print();
				} catch (PrinterException e2) {
					System.out.println(e2);
				}
			}
		}
	}

}
