package views;

import infrastructure.IterableExtensions;

import java.awt.Point;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.xml.parsers.ParserConfigurationException;

import models.Entity;

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

import controllers.IDiagramController;

public class DiagramView extends JPanel implements IDiagramView, DropTargetListener {

    private IDiagramController diagramController;
    private final JButton btnEntity;
    private final JButton btnRelationship;
    private final JButton btnHierarchy;
    private final JButton btnSave;
    private final JButton btnSubdiagram;
    private mxGraphComponent graphComponent;
    private JPopupMenu entityMenu;
    private JPopupMenu existingEntitiesMenu;
    private JMenuItem existingEntitiesMenuItem;
    private final JButton btnValidate;
    private final JButton btnPrint;
    private final JButton btnExport;
    private JButton btnZoomIn;
    private JButton btnZoomOut;

    /**
     * Create the panel.
     */
    public DiagramView() {
        setLayout(new FormLayout(new ColumnSpec[] {
        		FormFactory.RELATED_GAP_COLSPEC,
        		FormFactory.DEFAULT_COLSPEC,
        		FormFactory.DEFAULT_COLSPEC,
        		FormFactory.DEFAULT_COLSPEC,
        		FormFactory.DEFAULT_COLSPEC,
        		FormFactory.DEFAULT_COLSPEC,
        		FormFactory.DEFAULT_COLSPEC,
        		FormFactory.DEFAULT_COLSPEC,
        		FormFactory.DEFAULT_COLSPEC,
        		FormFactory.DEFAULT_COLSPEC,
        		FormFactory.DEFAULT_COLSPEC,
        		FormFactory.DEFAULT_COLSPEC,
        		FormFactory.DEFAULT_COLSPEC,},
        	new RowSpec[] {
        		FormFactory.RELATED_GAP_ROWSPEC,
        		FormFactory.DEFAULT_ROWSPEC,
        		FormFactory.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("default:grow"),}));

        this.btnEntity = new JButton("Entity");
        add(this.btnEntity, "2, 2");
        
                this.btnRelationship = new JButton("Relationship");
                btnRelationship.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                	}
                });
                add(this.btnRelationship, "3, 2");
        
                this.btnHierarchy = new JButton("Hierarchy");
                add(this.btnHierarchy, "4, 2");
        
                this.btnSave = new JButton("Save");
                add(this.btnSave, "5, 2");
        
                this.btnSubdiagram = new JButton("Sub-Diagram");
                add(this.btnSubdiagram, "6, 2");
        
                this.btnPrint = new JButton("Print");
                add(this.btnPrint, "7, 2");
        
                this.btnExport = new JButton("Export");
                add(this.btnExport, "8, 2");

        this.entityMenu = new JPopupMenu();
        this.existingEntitiesMenu = new JPopupMenu();
        this.existingEntitiesMenuItem = new JMenuItem(new AbstractAction("Existing Entity") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Iterable<Entity> entities = diagramController.getAvailableEntities();
                existingEntitiesMenu.removeAll();

                if (IterableExtensions.count(entities) == 0) {
                    return;
                }

                for (final Entity entity : entities) {
                    existingEntitiesMenu.add(new JMenuItem(new AbstractAction(entity.getName()) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            diagramController.handleCreatedEvent(entity);
                        }
                    }));
                }
                existingEntitiesMenu.show(btnEntity,
                        btnEntity.getX(), btnEntity.getY() + btnEntity.getHeight());
            }
        });
        
                this.btnValidate = new JButton("Validate");
                add(this.btnValidate, "9, 2");
        
        btnZoomIn = new JButton("+");
        add(btnZoomIn, "10, 2");
        
        btnZoomOut = new JButton("-");
        add(btnZoomOut, "11, 2");


        this.entityMenu.add(new JMenuItem(new AbstractAction("New Entity") {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagramController.createEntity();
            }
        }));

        this.entityMenu.add(this.existingEntitiesMenuItem);
    }

    @Override
    public void setController(IDiagramController controller) {   	
    	this.diagramController = controller;

        this.graphComponent = new mxGraphComponent(this.diagramController.getGraph());
        this.graphComponent.setAutoScroll(true);
        this.graphComponent.setAutoscrolls(true);
        // cannot create new arrows clicking from entity
        this.graphComponent.getConnectionHandler().setCreateTarget(false);
        this.graphComponent.setConnectable(false);
        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (diagramController.hasPendingEntity()) {
                    try {
                        diagramController.addEntity(e.getPoint().x, e.getPoint().y);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
        this.graphComponent.getGraphControl().addMouseListener(listener);

        try {
            this.graphComponent.getDropTarget().addDropTargetListener(this);
        } catch (TooManyListenersException e1) {
            // should not occur
            e1.printStackTrace();
        }

        this.add(this.graphComponent, "2, 4, 12, 1, fill, fill");

        this.btnEntity.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                entityMenu.show(e.getComponent(), btnEntity.getX(), btnEntity.getY() + btnEntity.getHeight());
                //diagramController.createEntity();
            }
        });

        this.btnRelationship.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                diagramController.createRelationship();
            }
        });

        this.btnHierarchy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                diagramController.createHierarchy();
            }
        });

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

        this.btnSubdiagram.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String diagramName = JOptionPane.showInputDialog(null, "Provide the diagram's name", "New Diagram", JOptionPane.QUESTION_MESSAGE);
                if (diagramName != null) {
                    diagramController.createSubDiagram(diagramName);
                }
            }
        });

        this.btnPrint.addActionListener(new PrintAction());
        this.btnExport.addActionListener(new ExportAction());
        this.btnValidate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagramController.validate();
            }
        });
        
        this.btnZoomIn.addMouseListener(new MouseAdapter(){
        	 @Override
             public void mouseClicked(MouseEvent e) {
                 graphComponent.zoomIn();
             }
        });
        
        this.btnZoomOut.addMouseListener(new MouseAdapter(){
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
    public boolean showDeleteDialog(String typeAndName, String otherMessege, boolean couldDelete) {
        int result = -1;
        if (couldDelete == false)
            JOptionPane.showMessageDialog(null, "The selected " + typeAndName +
                    otherMessege, "Deleting invalid " + typeAndName,
                    JOptionPane.QUESTION_MESSAGE);
        else
            result = JOptionPane.showConfirmDialog(null, "The " + typeAndName +
                    " are being deleted, are you shure you want this?",
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
                    null, 1, graphComponent.getBackground(), graphComponent.isAntiAlias(), null,
                    graphComponent.getCanvas());

            // Creates the URL-encoded XML data
            mxCodec codec = new mxCodec();
            String xml;
            FileOutputStream outputStream = null;
            try {
                xml = URLEncoder.encode(
                        mxXmlUtils.getXml(codec.encode(graph.getModel())), "UTF-8");

                mxPngEncodeParam param = mxPngEncodeParam
                        .getDefaultEncodeParam(image);
                param.setCompressedText(new String[]{"mxGraphModel", xml});

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
