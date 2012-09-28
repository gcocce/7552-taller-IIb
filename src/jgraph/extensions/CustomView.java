package jgraph.extensions;

import styling.StyleConstants;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

public class CustomView extends mxGraphView {

	public CustomView(mxGraph arg0) {
		super(arg0);
	}
	
	@Override
	public void updateFloatingTerminalPoint(mxCellState edge,
			mxCellState start, mxCellState end, boolean source)
	{
		start = getTerminalPort(edge, start, source);
		mxPoint next = getNextPoint(edge, end, source);
		double border = mxUtils.getDouble(edge.getStyle(),
				mxConstants.STYLE_PERIMETER_SPACING);
		border += mxUtils.getDouble(edge.getStyle(),
				(source) ? mxConstants.STYLE_SOURCE_PERIMETER_SPACING
						: mxConstants.STYLE_TARGET_PERIMETER_SPACING);
		
		if (this.shouldUseEndPoint(edge, start)){
			edge.setAbsoluteTerminalPoint(start.getAbsolutePoint(start.getAbsolutePointCount() -1 ), source);
			return;
		}
		
		if (this.shouldUseStartPoint(edge, start)){
			edge.setAbsoluteTerminalPoint(start.getAbsolutePoint(0), source);
			return;
		}
		
		mxPoint pt = getPerimeterPoint(start, next, graph.isOrthogonal(edge),
				border);
		edge.setAbsoluteTerminalPoint(pt, source);
	}

	private boolean shouldUseEndPoint(mxCellState edge, mxCellState start) {
		return (((mxCell)edge.getCell()).getStyle().equalsIgnoreCase(StyleConstants.NON_FIRST_ID_GROUP_CONNECTOR_STYLE) &&
				((mxCell)start.getCell()).getStyle().equalsIgnoreCase(StyleConstants.FIRST_ID_GROUP_CONNECTOR_STYLE));
	}
	
	private boolean shouldUseStartPoint(mxCellState edge, mxCellState end) {
		return (((mxCell)edge.getCell()).getStyle().equalsIgnoreCase(StyleConstants.WEAK_ENTITY_CONNECTOR_STYLE) &&
				((mxCell)end.getCell()).getStyle().equalsIgnoreCase(StyleConstants.FIRST_ID_GROUP_CONNECTOR_STYLE));
	}
}

