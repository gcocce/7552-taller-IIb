package views;

import controllers.IDomainDiagramController;

public interface IDomainDiagramView {
	void setController(IDomainDiagramController controller);
	void refreshGraphComponent();
	boolean showDeleteDialog(String typeAndName, String otherMessege, boolean couldDelete);
}
