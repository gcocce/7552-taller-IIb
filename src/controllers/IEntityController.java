package controllers;

import models.Entity;
import controllers.listeners.IEntityEventListener;
import views.IEntityView;

public interface IEntityController {

    void create(); //should be public void create()

    void addSubscriber(IEntityEventListener listener);

    boolean addEntity();

    boolean validateEntityName(String name);

    void setEntityView(IEntityView entityView);

    void selectKeys();

	void create(Entity entity);
}