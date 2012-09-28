package controllers.tests.mocks;

import persistence.IGraphPersistenceService;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class MockGraphPersistenceService implements IGraphPersistenceService {

	private int saveCalls;
	
	private mxGraph graphToSave;

	private String savePath;
	
	private String diagramName;

	private String[] cellsToLoad;
	
	public void setCellsToLoad(String[] cellNames){
		this.cellsToLoad = cellNames;
	}

	@Override
	public void load(String name, mxGraph graph) {
		this.diagramName = name;
		
		for (String cellName : this.cellsToLoad) {
			mxCell cell = new mxCell();
			cell.setId(cellName);
			graph.addCell(cell);
		}
	}

	public String getDiagramName() {
		return this.diagramName;
	}
	
	@Override
	public void save(String name, mxGraph graph) {
		this.saveCalls++;
		this.graphToSave = graph;
		this.savePath = name;
	}
	
	public int getSaveCalls(){
		return this.saveCalls;
	}

	public mxGraph getGraphToSave(){
		return this.graphToSave;
	}

	public String getSavePath() {
		return this.savePath;
	}
}
