package controllers.tests.mocks;

import controllers.IKeysController;
import controllers.factories.IKeysControllerFactory;

public class MockKeyControllerFactory implements IKeysControllerFactory {

    private boolean createCalled = false;
    private MockKeyController keyController;

    @Override
    public IKeysController create() {
        this.createCalled = true;
//        this.keyController.setKeys(objects);
        return this.keyController;
    }

    public boolean createCalled() {
        return this.createCalled;
    }

    public void setKeyController(MockKeyController keyController) {
        this.keyController = keyController;
    }
}
