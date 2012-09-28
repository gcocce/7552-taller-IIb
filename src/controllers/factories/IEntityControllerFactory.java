package controllers.factories;

import controllers.IEntityController;

public interface IEntityControllerFactory {
	IEntityController create();
}
