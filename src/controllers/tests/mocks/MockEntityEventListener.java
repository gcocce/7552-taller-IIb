package controllers.tests.mocks;

import controllers.listeners.IEntityEventListener;
import models.Entity;

public class MockEntityEventListener implements IEntityEventListener {
    public boolean called = false;
    private Entity entity;

    @Override
    public void handleCreatedEvent(Entity entity) {
        called = true;
        this.entity = entity;

    }
    
    public Entity get(){
        return this.entity;
    }
}
