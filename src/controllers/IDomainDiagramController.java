package controllers;

import java.awt.Point;

import models.DomainDiagram;
import views.IDomainDiagramView;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public interface IDomainDiagramController {
	mxGraph getGraph();
	DomainDiagram getDiagram();
	IDomainDiagramView getView();
	mxCell getClassCell(String id);
	void handleDrop(Point end);
	void handleDragStart(Point start);
	void load(DomainDiagram diagram);
	// TODO: See if we need this
//	Iterable<DomainClass> getAvailableClasses();
}
