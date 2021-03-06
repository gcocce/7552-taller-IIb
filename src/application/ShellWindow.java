package application;


import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import org.picocontainer.MutablePicoContainer;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controllers.IProjectController;

public class ShellWindow implements IShell {

	private JFrame frame;
	private JSplitPane splitPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Bootstrapper bootstrapper = new Bootstrapper();
				ShellWindow window = new ShellWindow();
				try {
					bootstrapper.run();
					MutablePicoContainer container = bootstrapper.getContainer();
					container.addComponent(IShell.class, window);
					IProjectController projectController = container.getComponent(IProjectController.class);
					window.setLeftContent(projectController.getView());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ShellWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(true);
		frame.setBounds(0, 0, 300, 675);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GLUE_COLSPEC},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.GLUE_ROWSPEC}));
		
		this.splitPane = new JSplitPane();
		this.splitPane.setRightComponent(null);
		this.splitPane.setLeftComponent(null);
		frame.getContentPane().add(splitPane, "2, 2, fill, fill");
		frame.setVisible(true);

	}

	@Override
	public void setLeftContent(Object c) {
		this.splitPane.setLeftComponent((Component) c);
	}

	@Override
	public void setRightContent(Object c) {
		this.splitPane.setRightComponent((Component) c);
	}

	@Override
	public void activateFullSize() {
		frame.setSize(1140, 675);
	}
}
