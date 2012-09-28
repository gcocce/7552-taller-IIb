package jgraph.extensions;

import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

public class CustomGraph extends mxGraph 
{
	public CustomGraph()
	{
		this.cellsEditable = false;
		this.cellsResizable = false;
		this.cellsBendable = false;
		this.cellsCloneable = false;
		this.cellsDeletable = true;
		this.cellsDisconnectable = true;
		this.autoSizeCells = true;
		this.setConnectableEdges(true);
	}

	@Override
	protected mxGraphView createGraphView()
	{
		return new CustomView(this);
	}

	@Override
	public boolean isCellLocked(Object cell) 
	{
		return this.getModel().isEdge(cell);
	}
}

