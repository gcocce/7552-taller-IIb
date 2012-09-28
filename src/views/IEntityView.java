package views;

import controllers.IEntityController;
import models.EntityType;

public interface IEntityView {

    public void setController(IEntityController entityController);

    public void showView();

    public void addAttributeView(IAttributeView attributeView);

    public IAttributeView getAttributeView();

    public String getEntityName();

    public EntityType getEntityType();

    public boolean isVisible();

    public void setEntityName(String name);

    public void setEntityType(EntityType type);

	public void setModeUpdating();
}
