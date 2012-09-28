package controllers;

import controllers.factories.IAttributeControllerFactory;
import controllers.factories.IKeysControllerFactory;
import controllers.listeners.IEntityEventListener;
import models.*;
import views.IEntityView;

import infrastructure.IProjectContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityController extends BaseController implements IEntityController {

    private Iterable<Entity> entityCollection;
    private IEntityView entityView;
    private Entity pendingEntity;
    private List<IEntityEventListener> listeners;
    private IKeysControllerFactory keysControllerFactory;
    private IAttributeControllerFactory attributeControllerFactory;
    private IAttributeController attributeController;
	private Operations operation;
    
    private enum Operations {
    	Creating,
    	Updating,
    	None
    }

    public EntityController(IProjectContext projectContext, IEntityView entityView, IAttributeControllerFactory attributeControllerFactory, IKeysControllerFactory keysControllerFactory) {
        super(projectContext);
        this.pendingEntity = new Entity("");
        this.attributeControllerFactory = attributeControllerFactory;
        this.keysControllerFactory = keysControllerFactory;
        this.listeners = new ArrayList<IEntityEventListener>();
        this.entityCollection = projectContext.getAllEntities(this.pendingEntity);
        this.setEntityView(entityView);
        this.operation = Operations.None;
    }


    @Override
    public void create() {
        this.attributeController = this.attributeControllerFactory.create();
        List<Attribute> attributes = this.pendingEntity.getAttributes().getAttributes();
        this.attributeController.setAttributes(attributes);

        this.entityView.addAttributeView(attributeController.getAttributeView());
        this.entityView.setEntityName(this.pendingEntity.getName());
        this.entityView.showView();
        this.operation = Operations.Creating;
    }
    
    @Override
    public void create(Entity entity) {
    	this.pendingEntity = entity;
        this.attributeController = this.attributeControllerFactory.create();
        
        List<Attribute> attributes = this.pendingEntity.getAttributes().getAttributes();
        this.attributeController.setAttributes(attributes);

        this.entityView.addAttributeView(attributeController.getAttributeView());
        this.entityView.setEntityName(this.pendingEntity.getName());
        this.entityView.setEntityType(this.pendingEntity.getType());
        this.entityView.setModeUpdating();
        this.entityView.showView();
        this.operation = Operations.Updating;
    }

    @Override
    public void addSubscriber(IEntityEventListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public boolean validateEntityName(String name) {
        if (name.equals("")) {
            return false;
        }
        Iterator<Entity> iterator = this.entityCollection.iterator();

        boolean isRepeated = false;

        while (iterator.hasNext() && !isRepeated) {
            isRepeated = iterator.next().getName().equals(name);
        }

        return !isRepeated;
    }

    @Override
    public boolean addEntity() {

        String entityName = this.entityView.getEntityName();
        if (this.operation == Operations.Creating && !this.validateEntityName(entityName)) {
            return false;
        }
        this.pendingEntity.setName(entityName);
        pendingEntity.setType(this.entityView.getEntityType());

        for (IEntityEventListener listener : this.listeners) {
            listener.handleCreatedEvent(pendingEntity);
        }

        return true;

    }

    @Override
    public void setEntityView(IEntityView entityView) {
        this.entityView = entityView;
        this.entityView.setController(this);
    }

    @Override
    public void selectKeys() {
        List<IKey> possibleKeys = new ArrayList<IKey>();
        for (Attribute attribute : this.attributeController.getAttributes()) {
            possibleKeys.add(attribute);
        }
        if (possibleKeys.size() > 0) {
        	IKeysController keysController = keysControllerFactory.create();
        	keysController.setPossibleKeys(possibleKeys);
        	keysController.create();
        }

    }
}
