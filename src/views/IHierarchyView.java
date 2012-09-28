package views;

import controllers.IHierarchyController;

public interface IHierarchyView {
	
	public void setController(IHierarchyController controller);

	boolean isVisible();

	void showView();

	public void update();

	public void create();
}
