package controllers;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import views.IProjectView;

public interface IProjectController {
	void createProject(String string);
	boolean openProject(String string) throws Exception;
	IDiagramController getCurrentDiagramController();
	IProjectView getView();
	TreeModel getProjectTree();
	void changeElement(TreePath treePath);
	void deleteElement(TreePath path);
	void validateProject(int toleranceLevel);
}
