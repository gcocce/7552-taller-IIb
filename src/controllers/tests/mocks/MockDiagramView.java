package controllers.tests.mocks;

import com.mxgraph.view.mxGraph;

import controllers.IDiagramController;
import views.IDiagramView;

public class MockDiagramView implements IDiagramView{

	private IDiagramController controller;
	private mxGraph graph;
	
	public IDiagramController getController() {
		return this.controller;
	}

	@Override
	public void setController(IDiagramController controller) {
		this.controller = controller;
		this.graph = controller.getGraph();
	}

	public mxGraph getGraph() {
		return this.graph;
	}

	@Override
	public void refreshGraphComponent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean showDeleteDialog(String typeAndName, String otherMessege, boolean couldDelete) {
		// TODO Auto-generated method stub
		return false;
	}

}
