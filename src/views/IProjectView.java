package views;

import javax.swing.tree.TreeModel;

import controllers.IProjectController;

public interface IProjectView {
	void setController(IProjectController projectController);

	void refreshTree(TreeModel projectTree);
}
