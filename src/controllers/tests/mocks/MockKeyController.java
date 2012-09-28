package controllers.tests.mocks;

import controllers.IKeysController;
import models.IKey;
import models.IdGroup;
import views.IKeysView;

public class MockKeyController implements IKeysController {
    private boolean createdCall = false;
	private Iterable<IKey> possibleKeys;

    @Override
    public void create() {
        this.createdCall = true;
    }

    @Override
    public void addIdGroupToKey() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setKeyView(IKeysView keysView) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeIdGroupFromKey() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean validIdGroupName(String name) {
        return false;
    }

    @Override
    public void removeIdGroupFromAllIdGroups(IdGroup selectedValue) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setKeys(Iterable<IKey> iKeys) {
    }

    public Iterable<IKey> getKeys() {
        return this.possibleKeys;
    }
    public boolean createdCalled(){
        return this.createdCall;
    }

	@Override
	public void setPossibleKeys(Iterable<IKey> possibleKeys) {
		this.possibleKeys = possibleKeys;
	}
}
