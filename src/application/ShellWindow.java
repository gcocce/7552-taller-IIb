package application;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;

import controllers.IProjectController;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JSplitPane;

public class ShellWindow implements IShell {

	private JFrame frame;
	private JSplitPane splitPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Bootstrapper bootstrapper = new Bootstrapper();
					bootstrapper.run();
					ShellWindow window = new ShellWindow();
					bootstrapper.getContainer().addComponent(IShell.class, window);
					IProjectController projectController = 
						(IProjectController) bootstrapper.getContainer().getComponent(IProjectController.class);
					window.frame.setVisible(true);
					window.setLeftContent((Component) projectController.getView());
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
		frame.setResizable(false);
		frame.setBounds(0, 0, 300, 675);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		this.splitPane = new JSplitPane();
		this.splitPane.setRightComponent(null);
		this.splitPane.setLeftComponent(null);
		frame.getContentPane().add(splitPane, "2, 2, fill, fill");
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
