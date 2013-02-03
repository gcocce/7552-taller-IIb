package controllers.tests.mocks;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import views.IProjectView;
import controllers.IDiagramController;
import controllers.IProjectController;

public class MockProjectController implements IProjectController {

	@Override
	public void createProject(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean openProject(String string) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IDiagramController getCurrentDiagramController() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProjectView getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeModel getProjectTree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeElement(TreePath treePath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteElement(TreePath path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateProject(int toleranceLevel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void navigateToDomainDiagram() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDiagram() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
