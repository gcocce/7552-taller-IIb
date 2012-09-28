package controllers.tests.mocks;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

import javax.xml.parsers.ParserConfigurationException;

import models.Diagram;
import models.Entity;
import models.Hierarchy;
import models.Relationship;
import views.IDiagramView;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import controllers.IDiagramController;
import controllers.listeners.IDiagramEventListener;

public class MockDiagramController implements IDiagramController {

	private Diagram diagram;
	
	private MockDiagramView view;

	private List<IDiagramEventListener> listeners;

	private int saveCalls;

	private Diagram loadedDiagram;
	
	private boolean called;

	private boolean relationshipCalled;

	private boolean hierarchyCalled;

	private boolean entityCalled;

	private Relationship updatedRelationship;

	private Entity updatedEntity;

	private Hierarchy updatedHierarchy;
	
	public MockDiagramController(){
		this.diagram = new Diagram();
		this.view = new MockDiagramView();
		this.listeners = new ArrayList<IDiagramEventListener>();
		this.called = false;
		this.relationshipCalled = false;
		this.hierarchyCalled = false;
		this.entityCalled = false;
	}
	
	@Override
	public void addEntity(double x, double y) throws Exception {
	}

	@Override
	public void createEntity() {
	}

	@Override
	public void createHierarchy() {
	}

	@Override
	public void createRelationship() {
	}

	@Override
	public mxCell getAttributeCell(String id) {
		return null;
	}

	@Override
	public mxCell getAttributeConnectorCell(String id) {
		return null;
	}

	@Override
	public mxCell getEntityCell(String id) {
		return null;
	}

	@Override
	public mxGraph getGraph() {
		return null;
	}

	@Override
	public IDiagramView getView() {
		return this.view;
	}

	@Override
	public void handleDragStart(Point start) {
		
	}

	@Override
	public void handleDrop(Point end) {
		
	}

	@Override
	public boolean hasPendingEntity() {
		return false;
	}

	@Override
	public void save() throws ParserConfigurationException {
		this.saveCalls++;
	}

	@Override
	public void handleCreatedEvent(Entity entity) {
		
	}

	@Override
	public void handleCreatedEvent(Relationship relationship) {
		
	}

	@Override
	public void handleCreatedEvent(Hierarchy hierarchy) {
		
	}

	@Override
	public Diagram getDiagram() {
		return this.diagram;
	}

	@Override
	public void addListener(IDiagramEventListener listener) {
		this.listeners.add(listener);
		
	}

	public List<IDiagramEventListener> getListeners() {
		return this.listeners;
	}

	@Override
	public void createSubDiagram(String diagramName) {
	}
	
	public void setDiagram(Diagram diagram){
		this.diagram = diagram;
	}

	@Override
	public void load(Diagram diagram) {
		this.loadedDiagram = diagram;
		this.called = true;
	}
	
	public int getSaveCalls(){
		return this.saveCalls;
	}

	public Diagram getLoadedDiagram() {
		return loadedDiagram;
	}

	public boolean wasCalled() {
		return this.called;
	}

	@Override
	public void updateEntity(Entity entity) {
		this.entityCalled = true;
		this.updatedEntity = entity;
	}

	@Override
	public void updateHierarchy(Hierarchy hierarchy) {
		this.hierarchyCalled = true;
		this.updatedHierarchy = hierarchy;
	}

	@Override
	public void updateRelationship(Relationship relationship) {
		this.relationshipCalled = true;
		this.updatedRelationship = relationship;
	}

	public boolean updateEntityWasCalled() {
		return this.entityCalled;
	}

	public boolean updateRelationshipWasCalled() {
		return this.relationshipCalled;
	}

	public boolean updateHierarchyWasCalled() {
		return this.hierarchyCalled;
	}

	public Relationship getUpdatedRelationship() {
		return this.updatedRelationship;
	}

	public Entity getUpdatedEntity() {
		return this.updatedEntity;
	}

	public Hierarchy getUpdatedHierarchy() {
		return this.updatedHierarchy;
	}

	@Override
	public mxCell getIdGroupConnectorCell(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public mxCell getWeakEntityConnectorCell(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEntityPeripherals(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterable<Entity> getAvailableEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteEntity(Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteRelationship(Relationship relationship) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteHierarchy(Hierarchy hierarchy) {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public void validate() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
