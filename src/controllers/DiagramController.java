package controllers;

import infrastructure.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import persistence.IGraphPersistenceService;
import persistence.IXmlFileManager;
import persistence.IXmlManager;
import styling.StyleConstants;
import styling.Styler;

import models.Attribute;
import models.AttributeCollection;
import models.Cardinality;
import models.Diagram;
import models.Entity;
import models.Hierarchy;
import models.IdGroup;
import models.Relationship;
import models.RelationshipEntity;

import jgraph.extensions.CustomGraph;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;

import controllers.factories.IEntityControllerFactory;
import controllers.factories.IHierarchyControllerFactory;
import controllers.factories.IRelationshipControllerFactory;
import controllers.listeners.IDiagramEventListener;
import validation.IProjectValidationService;
import views.IDiagramView;

public class DiagramController extends BaseController
        implements IDiagramController, mxIEventListener {

    public static class CellConstants {
        public static final String WeakEntityConnectorPrefix = "WeakEntityConnector";
        public static final String IdGroupConnectorPrefix = "IdGroupConnector";
        public static final String EntityPrefix = "Entity";
        public static final String RelationshipPrefix = "Relationship";
        public static final String AttributePrefix = "Attribute";
        public static final String AttributeConnectorPrefix = "AttributeConnector";
        public static final String RelationshipConnectorPrefix = "RelationshipConnector";
        public static final String HierarchyNodePrefix = "HierarchyNode";
        public static final String HierarchyConnectorPrefix = "HierarchyConnector";
    }

    private IProjectValidationService validationService;
    private IFileSystemService fileSystemService;


    private enum Operations {
        None,
        CreateEntity,
        UpdateEntity,
        CreateRelationship,
        UpdateRelationship,
        CreateHierarchy,
        UpdateHierarchy
    }

    private CustomGraph graph;
    private Map<String, mxCell> entityCells;
    private Map<String, mxCell> relationshipCells;
    private Map<String, mxCell> attributeCells;
    private Map<String, mxCell> attributeConnectorCells;
    private Map<String, mxCell> relationshipConnectorCells;
    private Map<String, mxCell> hierarchyNodeCells;
    private Map<String, mxCell> hierarchyConnectorCells;
    private Map<String, mxCell> idGroupConnectorCells;
    private Map<String, mxCell> weakEntityConnectorCells;
    private IEntityControllerFactory entityControllerFactory;
    private Entity pendingEntity;
    private IRelationshipControllerFactory relationshipControllerFactory;
    private Diagram diagram;
    private IXmlFileManager xmlFileManager;
    private IXmlManager<Diagram> diagramXmlManager;
    private IDiagramView diagramView;
    private List<mxCell> selectedCells;
    private Point dragStartPoint;
    private IHierarchyControllerFactory hierarchyControllerFactory;
    private IGraphPersistenceService graphPersistenceService;
    private List<IDiagramEventListener> listeners;
    private Pattern regex;
    private Operations currentOperation;

    public DiagramController(IProjectContext projectContext, IDiagramView diagramView,
                             IEntityControllerFactory entityControllerFactory,
                             IRelationshipControllerFactory relationshipControllerFactory,
                             IHierarchyControllerFactory hierarchyControllerFactory,
                             IXmlFileManager xmlFileManager,
                             IXmlManager<Diagram> diagramXmlManager,
                             IGraphPersistenceService graphPersistenceService,
                             IProjectValidationService validationService,
                             IFileSystemService fileSystemService) {
        super(projectContext);
        this.validationService = validationService;
        this.fileSystemService = fileSystemService;
        this.currentOperation = Operations.None;
        this.diagram = new Diagram();
        this.selectedCells = new ArrayList<mxCell>();
        this.entityControllerFactory = entityControllerFactory;
        this.relationshipControllerFactory = relationshipControllerFactory;
        this.hierarchyControllerFactory = hierarchyControllerFactory;
        this.graph = new CustomGraph();

        this.graph.getSelectionModel().addListener(mxEvent.CHANGE, this);

        this.entityCells = new HashMap<String, mxCell>();
        this.attributeCells = new HashMap<String, mxCell>();
        this.attributeConnectorCells = new HashMap<String, mxCell>();
        this.relationshipCells = new HashMap<String, mxCell>();
        this.relationshipConnectorCells = new HashMap<String, mxCell>();
        this.hierarchyConnectorCells = new HashMap<String, mxCell>();
        this.hierarchyNodeCells = new HashMap<String, mxCell>();
        this.idGroupConnectorCells = new HashMap<String, mxCell>();
        this.weakEntityConnectorCells = new HashMap<String, mxCell>();
        this.xmlFileManager = xmlFileManager;
        this.diagramXmlManager = diagramXmlManager;
        this.diagramView = diagramView;
        this.diagramView.setController(this);
        this.graphPersistenceService = graphPersistenceService;
        this.listeners = new ArrayList<IDiagramEventListener>();

        this.regex = Pattern.compile("(?:" + CellConstants.EntityPrefix + "|"
                + CellConstants.RelationshipPrefix + ")"
                + "([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}).*");
    }

    public IDiagramView getView() {
        return this.diagramView;
    }

    public mxGraph getGraph() {
        return this.graph;
    }

    public void createEntity() {
        if (!this.hasPendingEntity()) {
            this.currentOperation = Operations.CreateEntity;
            IEntityController entityController = this.entityControllerFactory.create();
            entityController.addSubscriber(this);
            entityController.create();
        }
    }

    @Override
    public void handleCreatedEvent(Entity entity) {
        switch (this.currentOperation) {
            case UpdateEntity:
                this.handleEntityUpdate(entity);
                this.diagram.isNotValidated();
                for (IDiagramEventListener listener : this.listeners) {
                    listener.handleEntityUpdated(this.diagram, entity);
                }
                break;
            case CreateEntity:
            default:
                this.pendingEntity = entity;
                break;
        }

        this.currentOperation = Operations.None;
    }

    private void handleEntityUpdate(Entity entity) {
        Object parent = this.graph.getDefaultParent();
        try {
            mxCell entityCell = this.getEntityCell(entity.getId().toString());
            this.addAttributesToElement(parent, entityCell, entity.getAttributes(), entity.getId());

            Func<Relationship, String, Boolean> relCmp = new Func<Relationship, String, Boolean>() {
                @Override
                public Boolean execute(Relationship relationship, String entityId) {
                    return relationship.hasWeakEntity() && relationship.getWeakEntity().getEntityId().toString().equalsIgnoreCase(entityId);
                }
            };

            for (Relationship relationship : IterableExtensions.where(this.diagram.getRelationships(), relCmp, entity.getId().toString())) {
                RelationshipEntity weakRelationshipEntity = relationship.getWeakEntity();
                RelationshipEntity strongRelationshipEntity = relationship.getStrongEntity();

                Entity weakEntity = this.diagram.getEntities().get(weakRelationshipEntity.getEntityId());

                Map<String, List<Attribute>> attributesByIdGroup = this.getAttributesByIdGroup(weakEntity.getAttributes());

                this.addWeakEntityConnectors(parent, strongRelationshipEntity.getEntityId(), weakEntity, relationship.getId(), attributesByIdGroup);
            }

            this.graph.getView().getState(entityCell).setLabel(entity.getName());
            entityCell.setValue(entity.getName());
            entityCell.setStyle(Styler.getFillColor(entity.getType()) + ";" + StyleConstants.ENTITY_STYLE);
            this.diagramView.refreshGraphComponent();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.graph.getModel().endUpdate();
            this.graph.repaint();
        }
    }

    public void createRelationship() {
        this.currentOperation = Operations.CreateRelationship;

        IRelationshipController relationshipController =
                this.relationshipControllerFactory.create();

        relationshipController.addCreateListener(this);

        relationshipController.create();
    }

    @Override
    public void handleCreatedEvent(Relationship relationship) throws Exception {
        double[] coordinates = this.getRelationshipNodeCoordinates(relationship.getRelationshipEntities());
        double x = coordinates[0];
        double y = coordinates[1];
        this.graph.getModel().beginUpdate();

        try {
            Object parent = this.graph.getDefaultParent();
            mxCell relationshipCell = this.addRelationshipToGraph(relationship, parent, x, y);

            Map<UUID, Integer> entityCount = new HashMap<UUID, Integer>();

            for (RelationshipEntity relationshipEntity : relationship.getRelationshipEntities()) {
                if (!entityCount.containsKey(relationshipEntity.getEntityId())) {
                    entityCount.put(relationshipEntity.getEntityId(), 1);
                } else {
                    Integer count = entityCount.get(relationshipEntity.getEntityId());
                    count++;
                    entityCount.put(relationshipEntity.getEntityId(), count);
                }
            }

            List<UUID> repeatedEntities = new ArrayList<UUID>();
            int relationshipEntitiesCount = 0;
            for (UUID uuid : entityCount.keySet()) {
                Integer count = entityCount.get(uuid);
                if (count > 1) {
                    repeatedEntities.add(uuid);
                    relationshipEntitiesCount += count;
                }
            }

            double partialRelationshipEntitiesAngle = relationshipEntitiesCount != 0 ? (2 * Math.PI) / relationshipEntitiesCount : 0;
            double currentRelationshipEntitiesAngle = 0;

            Map<UUID, Integer> entityAppeareanceCount = new HashMap<UUID, Integer>();

            for (RelationshipEntity relationshipEntity : relationship.getRelationshipEntities()) {

                int appeareance = 0;

                if (entityAppeareanceCount.containsKey(relationshipEntity.getEntityId())) {
                    appeareance = entityAppeareanceCount.get(relationshipEntity.getEntityId()) + 1;
                } else {
                    appeareance = 1;

                }

                entityAppeareanceCount.put(relationshipEntity.getEntityId(), appeareance);

                double xExit = 0;
                double yExit = 0;
                Boolean useExit = false;
                if (repeatedEntities.contains(relationshipEntity.getEntityId())) {
                    xExit = Math.cos(currentRelationshipEntitiesAngle) * 0.5 + 0.5;
                    yExit = Math.sin(currentRelationshipEntitiesAngle) * 0.5 + 0.5;
                    currentRelationshipEntitiesAngle += partialRelationshipEntitiesAngle;
                    useExit = true;
                }

                this.addRelationshipConnectorToGraph(parent, relationship, relationshipCell, relationshipEntity, xExit, yExit, useExit, appeareance);
            }

            this.addAttributesToElement(parent, relationshipCell, relationship.getAttributes(), relationship.getId());

            if (relationship.hasWeakEntity()) {
                RelationshipEntity weakRelationshipEntity = relationship.getWeakEntity();
                RelationshipEntity strongRelationshipEntity = relationship.getStrongEntity();

                Entity weakEntity = this.diagram.getEntities().get(weakRelationshipEntity.getEntityId());

                Map<String, List<Attribute>> attributesByIdGroup = this.getAttributesByIdGroup(weakEntity.getAttributes());

                this.addWeakEntityConnectors(parent, strongRelationshipEntity.getEntityId(), weakEntity, relationship.getId(), attributesByIdGroup);
            }
        } finally {
        	this.diagram.isNotValidated();
            if (this.currentOperation == Operations.CreateRelationship) {
                this.diagram.getRelationships().add(relationship);
                for (IDiagramEventListener eventListener : this.listeners) {
                    eventListener.handleRelationshipAdded(this.diagram, relationship);
                }
            } else {
                for (IDiagramEventListener listener : this.listeners) {
                    listener.handleRelationshipUpdated(this.diagram, relationship);
                }
            }
            this.graph.getModel().endUpdate();
        }

        this.currentOperation = Operations.None;
    }

    public void addEntity(double x, double y) throws Exception {
        if (pendingEntity == null) {
            return;
        }

        this.graph.getModel().beginUpdate();
        Object parent = this.graph.getDefaultParent();
        try {
            mxCell entityCell = this.addEntityToGraph(this.pendingEntity, parent, x, y);
            this.addAttributesToElement(parent, entityCell, this.pendingEntity.getAttributes(), this.pendingEntity.getId());
        } finally {
            this.diagram.getEntities().add(this.pendingEntity);
            this.diagram.isNotValidated();
            for (IDiagramEventListener listener : this.listeners) {
                listener.handleEntityAdded(this.diagram, this.pendingEntity);
            }
            this.graph.getModel().endUpdate();
        }

        this.pendingEntity = null;
    }

    private void addAttributesToElement(Object parent, mxCell elementCell, AttributeCollection attributes, UUID elementId) {
        double centerX = elementCell.getGeometry().getCenterX();
        double centerY = elementCell.getGeometry().getCenterY();

        int attributeCount = attributes.count();
        double partialAngle = attributeCount != 0 ? (2 * Math.PI) / attributeCount : 0;
        double currentAngle = 0;

        Map<String, List<Attribute>> attributesByIdGroup = this.getAttributesByIdGroup(attributes);

        for (Attribute attribute : attributes) {
            double xDistance = Math.cos(currentAngle) * StyleConstants.ATTRIBUTE_DEFAULT_DISTANCE;
            double yDistance = Math.sin(currentAngle) * StyleConstants.ATTRIBUTE_DEFAULT_DISTANCE;

            double attributeX = centerX + xDistance;
            double attributeY = centerY + yDistance;

            AttributeCollection childAttributes = attribute.getAttributes();

            boolean isComposite = childAttributes.count() != 0;
            boolean isKey = this.isKeyOnItsOwn(attributesByIdGroup, attribute);
            mxCell attributeCell = this.addAttributeToGraph(attribute, parent, elementId, attributeX, attributeY, isComposite);
            this.addAttributeConnectorToGraph(parent, elementId, elementCell, attribute, attributeCell, isKey, isComposite);

            if (isComposite) {
                this.addAttributesToElement(parent, attributeCell, childAttributes, elementId);
            }

            currentAngle += partialAngle;
        }

        this.addIdGroupConnectorsToElement(parent, elementId, attributesByIdGroup);
    }

    private void addIdGroupConnectorsToElement(Object parent, UUID elementId,
                                               Map<String, List<Attribute>> attributesByIdGroup) {
        for (String key : attributesByIdGroup.keySet()) {
            List<Attribute> attributes = attributesByIdGroup.get(key);

            mxCell lastIdGroupConnectorCell = null;

            for (int i = 1; i < attributes.size(); i++) {
                Attribute currentAttribute = attributes.get(i - 1);
                Attribute nextAttribute = attributes.get(i);

                mxCell currentAttributeCell = this.getAttributeConnectorCell(elementId.toString() + currentAttribute.getName());
                mxCell nextAttributeCell = this.getAttributeConnectorCell(elementId.toString() + nextAttribute.getName());

                String idGroupConnectorId = elementId.toString() + "_" + currentAttribute.getName() + "_" + nextAttribute.getName() + "_" + key;

                lastIdGroupConnectorCell = (mxCell) this.graph.insertEdge(parent, CellConstants.IdGroupConnectorPrefix + idGroupConnectorId, "",
                        i == 1 ? currentAttributeCell : lastIdGroupConnectorCell, nextAttributeCell,
                        i == 1 ? StyleConstants.FIRST_ID_GROUP_CONNECTOR_STYLE : StyleConstants.NON_FIRST_ID_GROUP_CONNECTOR_STYLE);

                this.idGroupConnectorCells.put(CellConstants.IdGroupConnectorPrefix + idGroupConnectorId, lastIdGroupConnectorCell);
            }
        }
    }

    private boolean isKeyOnItsOwn(
            Map<String, List<Attribute>> attributesByIdGroup,
            Attribute attribute) {
        for (List<Attribute> attributeCollection : attributesByIdGroup.values()) {
            if (attributeCollection.size() == 1 && attributeCollection.contains(attribute)) {
                return true;
            }
        }

        return false;
    }

    private Map<String, List<Attribute>> getAttributesByIdGroup(
            AttributeCollection attributes) {
        Map<String, List<Attribute>> attributesByKey = new HashMap<String, List<Attribute>>();

        for (Attribute attribute : attributes) {
            for (IdGroup idGroup : attribute.getIdGroup()) {
                List<Attribute> attributesForKey;
                if (!attributesByKey.containsKey(idGroup.getName())) {
                    attributesForKey = new ArrayList<Attribute>();
                    attributesByKey.put(idGroup.getName(), attributesForKey);
                } else {
                    attributesForKey = attributesByKey.get(idGroup.getName());
                }

                attributesForKey.add(attribute);
            }
        }

        return attributesByKey;
    }

    private mxCell addRelationshipConnectorToGraph(Object parent, Relationship relationship, mxCell relationshipCell,
                                                   RelationshipEntity relationshipEntity, double exitX, double exitY, Boolean useExit, Integer appeareance) {
        String cardinalityDisplay = String.format("(%s,%s)",
                Cardinality.getStringForCardinality(relationshipEntity
                        .getCardinality().getMinimum()),
                Cardinality.getStringForCardinality(relationshipEntity
                        .getCardinality().getMaximum()));

        String displayValue = StringExtensions.isNullOrEmpty(relationshipEntity.getRole()) ?
                cardinalityDisplay
                : String.format("%s %s", relationshipEntity.getRole(), cardinalityDisplay);

        String connectorId = relationship.getId().toString() +
                relationshipEntity.getEntityId().toString() +
                relationshipEntity.getRole() + appeareance.toString();

        mxCell entityCell = this.getEntityCell(relationshipEntity.getEntityId().toString());

        String exitStyle = "";

        if (useExit) {
            exitStyle = ";" + Styler.getEdgeExitStyle(exitX, exitY);
        }

        mxCell connectorCell = (mxCell) this.graph.insertEdge(parent, CellConstants.RelationshipConnectorPrefix + connectorId, displayValue,
                relationshipCell, entityCell, StyleConstants.RELATIONSHIP_LINK_STYLE + exitStyle);

        this.relationshipConnectorCells.put(CellConstants.RelationshipConnectorPrefix + connectorId, connectorCell);

        return connectorCell;
    }

    private mxCell addAttributeConnectorToGraph(Object parent, UUID ownerId, mxCell entityCell,
                                                Attribute attribute, mxCell attributeCell, boolean isKey, boolean isComposite) {
        String attributeConnectorId = ownerId.toString() + attribute.getName();

        mxCell connectorCell = (mxCell) this.graph.insertEdge(parent, CellConstants.AttributeConnectorPrefix + attributeConnectorId,
                attribute.getCardinality() == null || attribute.getCardinality().equals(1, 1) ? "" : attribute.getCardinality().toString(),
                entityCell, attributeCell, Styler.getAttributeConnectorStyle(attribute.getType(), isKey, isComposite));

        this.attributeConnectorCells.put(CellConstants.AttributeConnectorPrefix + attributeConnectorId, connectorCell);

        return connectorCell;
    }

    private mxCell addAttributeToGraph(Attribute attribute, Object parent, UUID ownerId, double x, double y, boolean isComposite) {
        String attributeId = ownerId.toString() + attribute.getName();
        mxCell attributeCell = (mxCell) this.graph.insertVertex(parent, CellConstants.AttributePrefix + attributeId,
                attribute.getName(), x, y,
                isComposite ? StyleConstants.COMPOSED_ATTRIBUTE_WIDTH : StyleConstants.ATTRIBUTE_WIDTH,
                isComposite ? StyleConstants.COMPOSED_ATTRIBUTE_HEIGHT : StyleConstants.ATTRIBUTE_HEIGHT,
                Styler.getAttributeStyle(isComposite));

        this.attributeCells.put(CellConstants.AttributePrefix + attributeId, attributeCell);


        this.graph.updateCellSize(attributeCell);

        return attributeCell;
    }

    private mxCell addRelationshipToGraph(Relationship relationship, Object parent, double x, double y) {
        mxCell relationshipCell = (mxCell) this.graph.insertVertex(parent, CellConstants.RelationshipPrefix + relationship.getId().toString(),
                relationship.getName(), x, y,
                StyleConstants.RELATIONSHIP_WIDTH, StyleConstants.RELATIONSHIP_HEIGHT, Styler.getRelationshipStyle(relationship.isComposition()));

        this.relationshipCells.put(CellConstants.RelationshipPrefix + relationship.getId().toString(), relationshipCell);

        return relationshipCell;
    }

    private mxCell addEntityToGraph(Entity entity, Object parent, double x, double y) throws Exception {
        mxCell entityCell = (mxCell) this.graph.insertVertex(parent, CellConstants.EntityPrefix + entity.getId().toString(),
                entity.getName(), x, y,
                StyleConstants.ENTITY_WIDTH, StyleConstants.ENTITY_HEIGHT, Styler.getFillColor(entity.getType()) + ";" + StyleConstants.ENTITY_STYLE);

        this.entityCells.put(CellConstants.EntityPrefix + entity.getId().toString(), entityCell);

        return entityCell;
    }

    private double[] getRelationshipNodeCoordinates(Iterable<RelationshipEntity> relationshipEntities) {
        double maxX = 0;
        double maxY = 0;
        double minY = Double.POSITIVE_INFINITY;
        double minX = Double.POSITIVE_INFINITY;

        Iterable<RelationshipEntity> distinctEntities = IterableExtensions.distinct(relationshipEntities, new Comparator<RelationshipEntity>() {

            @Override
            public int compare(RelationshipEntity e1, RelationshipEntity e2) {
                if (e1.getEntityId() == e2.getEntityId()) {
                    return 0;
                }

                return 1;
            }
        });

        if (IterableExtensions.count(distinctEntities) == 1) {
            RelationshipEntity entity = IterableExtensions.firstOrDefault(distinctEntities);

            mxCell entityCell = this.getEntityCell(entity.getEntityId().toString());
            double x = entityCell.getGeometry().getX();
            double y = entityCell.getGeometry().getY();

            return new double[]{x, y + StyleConstants.ENTITY_HEIGHT * 3};
        }

        for (RelationshipEntity entity : distinctEntities) {
            mxCell entityCell = this.getEntityCell(entity.getEntityId().toString());
            double x = entityCell.getGeometry().getX();
            double y = entityCell.getGeometry().getY();
            if (x > maxX) {
                maxX = x;
            }

            if (x < minX) {
                minX = x;
            }

            if (y > maxY) {
                maxY = y;
            }

            if (y < minY) {
                minY = y;
            }
        }

        double xPosition = minX + (maxX - minX) / 2;
        double yPosition = minY + (maxY - minY) / 2;

        return new double[]{xPosition, yPosition};
    }

    public mxCell getEntityCell(String id) {
        return this.entityCells.get(CellConstants.EntityPrefix + id);
    }

    public mxCell getAttributeCell(String id) {
        return this.attributeCells.get(CellConstants.AttributePrefix + id);
    }

    public mxCell getAttributeConnectorCell(String id) {
        return this.attributeConnectorCells.get(CellConstants.AttributeConnectorPrefix + id);
    }


    private void addWeakEntityConnectors(Object parent, UUID entityId,
                                         Entity weakEntity, UUID relationshipId, Map<String, List<Attribute>> attributesByIdGroup) {
        mxCell strongEntityCell = this.getEntityCell(entityId.toString());

        mxCell cellToConnectTo = null;
        Attribute firstAttribute = null;

        for (String key : attributesByIdGroup.keySet()) {
            List<Attribute> attributesInGroup = attributesByIdGroup.get(key);
            if (attributesInGroup.size() == 1) {
                // connect strong entity with attribute line
                firstAttribute = attributesInGroup.get(0);
                cellToConnectTo = this.getAttributeConnectorCell(weakEntity.getId() + firstAttribute.getName());
            } else {
                firstAttribute = attributesInGroup.get(0);
                Attribute secondAttribute = attributesInGroup.get(1);

                String idGroupConnectorId = weakEntity.getId().toString() + "_" + firstAttribute.getName() + "_" + secondAttribute.getName() + "_" + key;

                cellToConnectTo = this.getIdGroupConnectorCell(idGroupConnectorId);
            }

            String weakEntityConnectorId = weakEntity.getId().toString() + "_" + relationshipId.toString() + "_" + firstAttribute.getName() + "_" + key;

            mxCell weakEntityConnectorCell = (mxCell) this.graph.insertEdge(parent, CellConstants.WeakEntityConnectorPrefix + weakEntityConnectorId, "",
                    strongEntityCell, cellToConnectTo, StyleConstants.WEAK_ENTITY_CONNECTOR_STYLE);

            this.weakEntityConnectorCells.put(CellConstants.WeakEntityConnectorPrefix + weakEntityConnectorId, weakEntityConnectorCell);
        }
    }

    public boolean hasPendingEntity() {
        return this.pendingEntity != null;
    }

    public mxCell getRelationshipCell(String id) {
        return this.relationshipCells.get(CellConstants.RelationshipPrefix + id);
    }


    public mxCell getRelationshipConnectorCell(String id) {
        return this.relationshipConnectorCells.get(CellConstants.RelationshipConnectorPrefix + id);
    }

    public Diagram getDiagram() {
        return this.diagram;
    }

    public void save() throws ParserConfigurationException {
        Document document = this.xmlFileManager.createDocument();
        Element element =
                this.diagramXmlManager.getElementFromItem(this.diagram, document);

        document.appendChild(element);
        this.xmlFileManager.write(document, this.getComponentFilePath());

        this.graphPersistenceService.save(this.getRepresentationFilePath(), this.graph);

    }

    private String getRepresentationFilePath() {
        return this.projectContext.getDataDirectory() + "/" + this.diagram.getName() + "-rep";
    }

    private String getComponentFilePath() {
        return this.projectContext.getDataDirectory() + "/" + this.diagram.getName() + "-comp";
    }


    public void openDiagram(String path) throws Exception {
        Document document = this.xmlFileManager.read(path);
        Element element = document.getDocumentElement();
        this.diagram = this.diagramXmlManager.getItemFromXmlElement(element);
    }

    @Override
    public void load(Diagram diagram) {

        this.diagram = diagram;
        String fileName = this.getRepresentationFilePath();
        this.graphPersistenceService.load(fileName, this.graph);

        Pattern regex1 = Pattern.compile(CellConstants.EntityPrefix);
        Pattern regex2 = Pattern.compile(CellConstants.RelationshipConnectorPrefix);
        Pattern regex3 = Pattern.compile(CellConstants.RelationshipPrefix);
        Pattern regex4 = Pattern.compile(CellConstants.AttributeConnectorPrefix);
        Pattern regex5 = Pattern.compile(CellConstants.AttributePrefix);
        Pattern regex6 = Pattern.compile(CellConstants.HierarchyConnectorPrefix);
        Pattern regex7 = Pattern.compile(CellConstants.HierarchyNodePrefix);
        Pattern regex8 = Pattern.compile(CellConstants.WeakEntityConnectorPrefix);
        Pattern regex9 = Pattern.compile(CellConstants.IdGroupConnectorPrefix);
        for (Object o : this.graph.getChildCells(this.graph.getDefaultParent())) {
            mxCell cell = (mxCell) o;

            Matcher matcher = regex8.matcher(cell.getId());
            boolean matchFound = matcher.find();
            if (matchFound) {
                this.weakEntityConnectorCells.put(cell.getId(), cell);
                continue;
            }

            matcher = regex1.matcher(cell.getId());
            matchFound = matcher.find();
            if (matchFound) {
                this.entityCells.put(cell.getId(), cell);
                continue;
            }
            matcher = regex2.matcher(cell.getId());
            matchFound = matcher.find();
            if (matchFound) {
                this.relationshipConnectorCells.put(cell.getId(), cell);
                continue;
            }
            matcher = regex3.matcher(cell.getId());
            matchFound = matcher.find();
            if (matchFound) {
                this.relationshipCells.put(cell.getId(), cell);
                continue;
            }
            matcher = regex4.matcher(cell.getId());
            matchFound = matcher.find();
            if (matchFound) {
                this.attributeConnectorCells.put(cell.getId(), cell);
                continue;
            }
            matcher = regex5.matcher(cell.getId());
            matchFound = matcher.find();
            if (matchFound) {
                this.attributeCells.put(cell.getId(), cell);
                continue;
            }
            matcher = regex6.matcher(cell.getId());
            matchFound = matcher.find();
            if (matchFound) {
                this.hierarchyConnectorCells.put(cell.getId(), cell);
                continue;
            }
            matcher = regex7.matcher(cell.getId());
            matchFound = matcher.find();
            if (matchFound) {
                this.hierarchyNodeCells.put(cell.getId(), cell);
                continue;
            }
            matcher = regex9.matcher(cell.getId());
            matchFound = matcher.find();
            if (matchFound) {
                this.idGroupConnectorCells.put(cell.getId(), cell);
                continue;
            }
        }
    }

    @Override
    public void invoke(Object arg0, mxEventObject arg1) {
        // JGraph has a bug "removed" are those added to the selection. "added" are those that were removed from the selection
        ArrayList added = arg1.getProperties().containsKey("removed") ? (ArrayList) arg1.getProperties().get("removed") : null;
        ArrayList removed = arg1.getProperties().containsKey("added") ? (ArrayList) arg1.getProperties().get("added") : null;

        if (removed != null) {
            for (Object cell : removed) {
                this.selectedCells.remove((mxCell) cell);
            }
        }

        if (added != null) {
            for (Object cell : added) {
                if (this.entityCells.containsValue(cell) || this.relationshipCells.containsValue(cell)) {
                    this.selectedCells.add((mxCell) cell);
                }
            }
        }
    }

    private String getElementUUID(String elementId) {
        Matcher matcher = this.regex.matcher(elementId);
        boolean matchFound = matcher.find();

        if (matchFound) {
            return matcher.group(1);
        }
        return null;
    }

    public void handleDrop(Point end) {
        if (this.dragStartPoint != null) {
            double dx = end.getX() - this.dragStartPoint.getX();
            double dy = end.getY() - this.dragStartPoint.getY();

            List<mxCell> attributesCellsToMove = new ArrayList<mxCell>();

            for (mxCell cell : this.selectedCells) {
                String elementId = this.getElementUUID(cell.getId());

                for (String attributeKey : this.attributeCells.keySet()) {
                    if (elementId != null) {
                        if (attributeKey.startsWith(CellConstants.AttributePrefix + elementId)) {
                            mxCell attributeCell = this.attributeCells.get(attributeKey);
                            attributesCellsToMove.add(attributeCell);
                        }
                    }
                }
            }

            if (attributesCellsToMove.size() == 0) {
                return;
            }

            this.graph.getModel().beginUpdate();

            try {
                this.graph.moveCells(attributesCellsToMove.toArray(), dx, dy);
            } finally {
                this.graph.getModel().endUpdate();
            }

            this.dragStartPoint = null;
        }
    }

    public void handleDragStart(Point start) {
        if (this.dragStartPoint == null && this.selectedCells.size() != 0) {
            this.dragStartPoint = new Point(start);
        }
    }

    @Override
    public void createHierarchy() {
        this.currentOperation = Operations.CreateHierarchy;

        IHierarchyController hierarchyController = this.hierarchyControllerFactory.create();
        hierarchyController.addSuscriber(this);
        hierarchyController.create();
    }

    @Override
    public void handleCreatedEvent(Hierarchy hierarchy) {
        this.graph.getModel().beginUpdate();

        try {
            Object parent = this.graph.getDefaultParent();
            mxCell hierarchyNode = this.addHierarchyNode(hierarchy, parent);

            for (UUID childId : hierarchy.getChildren()) {
                this.connectChildToHierarchy(parent, hierarchyNode, hierarchy.getId(), childId);
            }
        } finally {
        	this.diagram.isNotValidated();
            if (this.currentOperation == Operations.CreateHierarchy) {
                this.diagram.getHierarchies().add(hierarchy);
                for (IDiagramEventListener listener : this.listeners) {
                    listener.handleHierarchyAdded(this.diagram, hierarchy);
                }
            } else {
                for (IDiagramEventListener listener : this.listeners) {
                    listener.handleHierarchyUpdated(this.diagram, hierarchy);
                }
            }
            this.graph.getModel().endUpdate();
        }
        this.currentOperation = Operations.CreateHierarchy;
    }

    private void connectChildToHierarchy(Object parent, mxCell hierarchyNode, UUID hierarchyId, UUID childId) {
        String stringId = childId.toString();
        mxCell childCell = this.getEntityCell(stringId);

        mxCell hierarchyConnectorCell = (mxCell) this.graph
                .insertEdge(parent, CellConstants.HierarchyConnectorPrefix + hierarchyId.toString() + childId, "", childCell, hierarchyNode, StyleConstants.HIERARCHY_CHILD_LINK_STYLE);

        this.hierarchyConnectorCells.put(CellConstants.HierarchyConnectorPrefix + hierarchyId.toString() + childId, hierarchyConnectorCell);
    }

    private mxCell addHierarchyNode(Hierarchy hierarchy, Object parent) {
        String parentId = hierarchy.getGeneralEntityId().toString();
        mxCell parentCell = this.getEntityCell(parentId);
        double x = parentCell.getGeometry().getCenterX();
        double y = parentCell.getGeometry().getCenterY() + StyleConstants.ENTITY_HEIGHT / 2 + StyleConstants.HIERARCHY_DISTANCE_TO_PARENT;
        mxCell hierarchyNode = (mxCell) this.graph.insertVertex(parent, CellConstants.HierarchyNodePrefix + hierarchy.getId().toString(), "", x, y, 0, 0);
        mxCell hierarchyConnectorCell = (mxCell) this.graph
                .insertEdge(parent, CellConstants.HierarchyConnectorPrefix + hierarchy.getId().toString() + parentId, hierarchy.getSummary(), hierarchyNode, parentCell, StyleConstants.HIERARCHY_PARENT_LINK_STYLE);
        this.hierarchyNodeCells.put(CellConstants.HierarchyNodePrefix + hierarchy.getId().toString(), hierarchyNode);
        this.hierarchyConnectorCells.put(CellConstants.HierarchyConnectorPrefix + hierarchy.getId().toString() + parentId, hierarchyConnectorCell);

        return hierarchyNode;
    }

    public mxCell getHierarchyNodeCell(String id) {
        return this.hierarchyNodeCells.get(CellConstants.HierarchyNodePrefix + id);
    }

    public mxCell getHierarchyConnectorCell(String id) {
        return this.hierarchyConnectorCells.get(CellConstants.HierarchyConnectorPrefix + id);
    }

    @Override
    public mxCell getIdGroupConnectorCell(String id) {
        return this.idGroupConnectorCells.get(CellConstants.IdGroupConnectorPrefix + id);
    }

    public void addListener(IDiagramEventListener listener) {
        this.listeners.add(listener);
    }

    public void createSubDiagram(String diagramName) {
        for (IDiagramEventListener listener : this.listeners) {
            listener.handleSubDiagramCreated(this.diagram, diagramName);
        }

        try {
            this.save();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateEntity(Entity entity) {
        this.deleteEntityPeripherals(entity);

        IEntityController entityController = this.entityControllerFactory.create();
        entityController.addSubscriber(this);
        entityController.create(entity);
    }

    public void deleteEntityPeripherals(Entity entity) {
        this.currentOperation = Operations.UpdateEntity;
        this.removeAttributes(entity.getAttributes(), entity.getId().toString());
        this.removeIdGroupConnectors(entity.getId().toString());
        this.removeWeakEntityConnectors(entity.getId().toString());
    }

    private void removeWeakEntityConnectors(String entityId) {
        Func<String, String, Boolean> cmp = new Func<String, String, Boolean>() {
            @Override
            public Boolean execute(String mapKey, String id) {
                return mapKey.contains(id);
            }
        };

        mxIGraphModel model = this.graph.getModel();

        for (String weakEntityConnectorKey : IterableExtensions.where(this.weakEntityConnectorCells.keySet(), cmp, entityId)) {
            if (weakEntityConnectorKey.contains(entityId)) {
                mxCell weakEntityConnector = this.weakEntityConnectorCells.remove(weakEntityConnectorKey);
                model.remove(weakEntityConnector);
            }
        }
    }

    @Override
    public void updateHierarchy(Hierarchy hierarchy) {
        this.currentOperation = Operations.UpdateHierarchy;
        this.removeHierarchyConnectors(hierarchy);
        this.removeHierarchyCells(hierarchy);

        IHierarchyController hierarchyController = this.hierarchyControllerFactory.create();
        hierarchyController.addSuscriber(this);
        hierarchyController.create(hierarchy);
    }

    @Override
    public void updateRelationship(Relationship relationship) {
        this.currentOperation = Operations.UpdateRelationship;
        this.removeRelationshipConnectors(relationship);
        this.removeWeakEntityConnectors(relationship);
        this.removeRelationshipCells(relationship);

        this.removeAttributes(relationship.getAttributes(), relationship.getId().toString());

        IRelationshipController relationshipController = this.relationshipControllerFactory.create();
        relationshipController.addCreateListener(this);
        relationshipController.create(relationship);
    }

    private void removeRelationshipCells(Relationship relationship) {
        mxIGraphModel model = this.graph.getModel();

        String relationshipCell = CellConstants.RelationshipPrefix + relationship.getId().toString();
        model.remove(this.relationshipCells.remove(relationshipCell));
    }

    private void removeHierarchyCells(Hierarchy hierarchy) {
        mxIGraphModel model = this.graph.getModel();

        String keyNode = CellConstants.HierarchyNodePrefix + hierarchy.getId().toString();
        model.remove(this.hierarchyNodeCells.remove(keyNode));
    }

    private void removeRelationshipConnectors(Relationship relationship) {
        mxIGraphModel model = this.graph.getModel();
        for (RelationshipEntity relationshipEntity : relationship.getRelationshipEntities()) {
            String keyConnector = CellConstants.RelationshipConnectorPrefix + relationship.getId().toString() + relationshipEntity.getEntityId().toString();

            for (Integer i = 1; ; i++) {
                mxCell cell = this.relationshipConnectorCells.remove(keyConnector + i.toString());
                if (cell == null)
                    break;

                model.remove(cell);
            }
        }
    }

    private void removeHierarchyConnectors(Hierarchy hierarchy) {
        mxIGraphModel model = this.graph.getModel();

        for (UUID uuid : hierarchy.getChildren()) {
            String keyConnector = CellConstants.HierarchyConnectorPrefix + hierarchy.getId().toString() + uuid;
            model.remove(this.hierarchyConnectorCells.remove(keyConnector));
        }

        String keyConnector = CellConstants.HierarchyConnectorPrefix + hierarchy.getId().toString() + hierarchy.getGeneralEntityId().toString();

        model.remove(this.hierarchyConnectorCells.remove(keyConnector));
    }

    private void removeWeakEntityConnectors(Relationship relationship) {
        mxIGraphModel model = this.graph.getModel();

        if (relationship.hasWeakEntity()) {
            RelationshipEntity weakEntity = relationship.getWeakEntity();

            Func<String, String, Boolean> cmp = new Func<String, String, Boolean>() {
                @Override
                public Boolean execute(String mapKey, String beginning) {
                    return mapKey.startsWith(beginning);
                }
            };

            String param = CellConstants.WeakEntityConnectorPrefix + weakEntity.getEntityId().toString() + "_" + relationship.getId().toString();

            Iterable<String> keys = IterableExtensions.where(this.weakEntityConnectorCells.keySet(), cmp, param);

            for (String key : keys) {
                mxCell cellToRemove = this.weakEntityConnectorCells.remove(key);
                model.remove(cellToRemove);
            }
        }
    }

    private void removeAttributes(Iterable<Attribute> attributes, String id) {
        mxIGraphModel model = this.graph.getModel();
        for (Attribute attribute : attributes) {
            String keyConnector = CellConstants.AttributeConnectorPrefix + id + attribute.getName();
            String keyCell = CellConstants.AttributePrefix + id + attribute.getName();
            model.remove(this.attributeConnectorCells.remove(keyConnector));
            model.remove(this.attributeCells.remove(keyCell));
            this.removeAttributes(attribute.getAttributes(), id);
        }
    }

    private void removeIdGroupConnectors(String entityId) {
        mxIGraphModel model = this.graph.getModel();

        for (Object idGroupConnectorKey : this.idGroupConnectorCells.keySet().toArray()) {
            String idGroupKey = (String) idGroupConnectorKey;
            if (idGroupKey.contains(entityId)) {
                mxCell idGroupConnectorCell = this.idGroupConnectorCells.remove(idGroupConnectorKey);
                model.remove(idGroupConnectorCell);
            }
        }
    }

    @Override
    public mxCell getWeakEntityConnectorCell(String id) {
        return this.weakEntityConnectorCells.get(CellConstants.WeakEntityConnectorPrefix + id);
    }

    @Override
    public Iterable<Entity> getAvailableEntities() {
        Iterable<Entity> entities = this.projectContext.getAllEntities();
        List<Entity> entityList = IterableExtensions.getListOf(entities);

        for (Entity entity : this.diagram.getEntities()) {
            entityList.remove(entity);
        }

        return entityList;
    }

    @Override
    public boolean deleteEntity(Entity entity) {
        if (this.diagram.getEntities().get(entity.getId()) == null) {
            this.diagramView.showDeleteDialog("entity " + entity.getName(),
                    " don't belong into current diagram", false);
            return false;
        }
        if (canDeleteEntity(entity)) {
            if (this.diagramView.showDeleteDialog("entity " + entity.getName(), "", true)) {
                this.removeEntity(entity);
                this.diagram.isNotValidated();
                return true;
            }
        } else {
            this.diagramView.showDeleteDialog("entity " + entity.getName(),
                    " belongs to a relationship or a hierarchy", false);
        }
        return false;
    }

    private boolean canDeleteEntity(Entity entity) {
        for (Relationship relationship : this.diagram.getRelationships())
            for (RelationshipEntity relEntity : relationship.getRelationshipEntities())
                if (relEntity.getEntityId().equals(entity.getId()))
                    return false;
        for (Hierarchy hierarchy : this.diagram.getHierarchies()) {
            if (hierarchy.getGeneralEntityId().equals(entity.getId()))
                return false;
            for (UUID entityId : hierarchy.getChildren())
                if (entityId.equals(entity.getId()))
                    return false;
        }
        return true;
    }

    @Override
    public boolean deleteRelationship(Relationship relationship) {
        if (this.diagram.getRelationship(relationship.getId()) == null) {
            this.diagramView.showDeleteDialog("relationship " + relationship.getName(),
                    " don't belong into current diagram", false);
            return false;
        }
        if (this.diagramView.showDeleteDialog("relationship " + relationship.getName(), "", true)) {
            this.removeRelationship(relationship);
            this.diagram.isNotValidated();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteHierarchy(Hierarchy hierarchy) {
        if (this.diagram.getHierarchies().getHierarchy(hierarchy.getId()) == null) {
            this.diagramView.showDeleteDialog("Hierarchy",
                    " don't belong into current diagram", false);
            return false;
        }
        if (this.diagramView.showDeleteDialog("Hierarchy", "", true)) {
            this.removeHierarchy(hierarchy);
            this.diagram.isNotValidated();
            return true;
        }
        return false;
    }

    @Override
    public void validate() {
        String reportHtml = this.validationService.generateIndividualReport(diagram);
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String reportName = this.projectContext.getDataDirectory() + "/" + diagram.getName() + "_" + sdf.format(date) + "_report.html";
        this.fileSystemService.save(reportName, reportHtml);
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File(reportName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeEntity(Entity entity) {
        this.deleteEntityPeripherals(entity);

        mxIGraphModel model = this.graph.getModel();

        String entityCell = CellConstants.EntityPrefix + entity.getId().toString();
        model.remove(this.entityCells.remove(entityCell));

        this.diagram.getEntities().remove(entity.getName());
    }

    private void removeRelationship(Relationship relationship) {
        this.removeRelationshipConnectors(relationship);
        this.removeWeakEntityConnectors(relationship);
        this.removeRelationshipCells(relationship);

        this.removeAttributes(relationship.getAttributes(), relationship.getId().toString());

        try {
            this.diagram.removeRelationship(relationship.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeHierarchy(Hierarchy hierarchy) {
        this.removeHierarchyConnectors(hierarchy);
        this.removeHierarchyCells(hierarchy);

        try {
            this.diagram.getHierarchies().removeHierarchy(hierarchy.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
