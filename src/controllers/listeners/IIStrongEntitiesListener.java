package controllers.listeners;

import models.der.IStrongEntity;

import java.util.List;

public interface IIStrongEntitiesListener {

    void handleEvent(List<IStrongEntity> param);

}
