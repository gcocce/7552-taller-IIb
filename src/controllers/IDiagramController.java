package controllers;

import java.awt.Point;

import javax.xml.parsers.ParserConfigurationException;

import models.Diagram;
import models.Entity;
import models.Hierarchy;
import models.Relationship;

import views.IDiagramView;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import controllers.listeners.IDiagramEventListener;
import controllers.listeners.IEntityEventListener;
import controllers.listeners.IHierarchyEventListener;
import controllers.listeners.IRelationshipEventListener;

public interface IDiagramController extends IEntityEventListener, IRelationshipEventListener, IHierarchyEventListener{
	void createSubDiagram(String diagramName);
	void addListener(IDiagramEventListener iDiagramEventListener);
	mxGraph getGraph();
	Diagram getDiagram();
	void createEntity();
	void createRelationship();
	void createHierarchy();
	void addEntity(double x, double y) throws Exception;
	mxCell getEntityCell(String id);
	mxCell getAttributeCell(String id);
	mxCell getAttributeConnectorCell(String id);
	mxCell getIdGroupConnectorCell(String string);
	mxCell getWeakEntityConnectorCell(String id);
	boolean hasPendingEntity();
	void save() throws ParserConfigurationException;
	IDiagramView getView();
	void handleDrop(Point end);
	void handleDragStart(Point start);
	void load(Diagram diagram);
	void updateEntity(Entity entity);
	void updateRelationship(Relationship relationship);
	void updateHierarchy(Hierarchy hierarchy);
	void deleteEntityPeripherals(Entity entity);
	Iterable<Entity> getAvailableEntities();
	boolean deleteEntity(Entity entity);
	boolean deleteRelationship(Relationship relationship);
	boolean deleteHierarchy(Hierarchy hierarchy);
    void validate();
}
