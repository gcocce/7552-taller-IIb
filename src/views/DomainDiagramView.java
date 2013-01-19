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
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.TooManyListenersException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.util.png.mxPngEncodeParam;
import com.mxgraph.util.png.mxPngImageEncoder;
import com.mxgraph.view.mxGraph;

import controllers.IDomainDiagramController;

public class DomainDiagramView extends JPanel implements IDomainDiagramView,
		DropTargetListener {

	private static final long serialVersionUID = 1385530682356119350L;

	private IDomainDiagramController diagramController;
	private final JButton btnSave;
	private mxGraphComponent graphComponent;
	private final JButton btnPrint;
	private final JButton btnExport;
	private JButton btnZoomIn;
	private JButton btnZoomOut;

	/**
	 * Create the panel.
	 */
	public DomainDiagramView() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"), }));

		Insets inset = new Insets(2, 35, 2, 35);

		this.btnSave = new JButton("Save");
		this.btnSave.setMargin(inset);
		add(this.btnSave, "2, 2");

		this.btnPrint = new JButton("Print");
		this.btnPrint.setMargin(inset);
		add(this.btnPrint, "3, 2");

		this.btnExport = new JButton("Export");
		this.btnExport.setMargin(inset);
		add(this.btnExport, "4, 2");

		btnZoomIn = new JButton("+");
		add(btnZoomIn, "5, 2");

		btnZoomOut = new JButton("-");
		add(btnZoomOut, "6, 2");

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

		this.add(this.graphComponent, "2, 4, 6, 1, fill, fill");

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
		this.btnExport.addActionListener(new ExportAction());

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

	private class ExportAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			mxGraph graph = graphComponent.getGraph();

			// Creates the image for the PNG file
			BufferedImage image = mxCellRenderer.createBufferedImage(graph,
					null, 1, graphComponent.getBackground(),
					graphComponent.isAntiAlias(), null,
					graphComponent.getCanvas());

			// Creates the URL-encoded XML data
			mxCodec codec = new mxCodec();
			String xml;
			FileOutputStream outputStream = null;
			try {
				xml = URLEncoder.encode(
						mxXmlUtils.getXml(codec.encode(graph.getModel())),
						"UTF-8");

				mxPngEncodeParam param = mxPngEncodeParam
						.getDefaultEncodeParam(image);
				param.setCompressedText(new String[] { "mxGraphModel", xml });

				// Saves as a PNG file
				String name = diagramController.getDiagram().getName();
				outputStream = new FileOutputStream(new File(name + ".jpg"));

				mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream,
						param);

				if (image != null) {
					encoder.encode(image);

				} else {
					JOptionPane.showMessageDialog(graphComponent,
							mxResources.get("noImageData"));
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				try {
					outputStream.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
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
