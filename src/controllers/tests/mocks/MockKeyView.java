package controllers.tests.mocks;

import controllers.IKeysController;
import models.IKey;
import models.IdGroup;
import views.IKeysView;

public class MockKeyView implements IKeysView{

    private IdGroup idGroupSelected;
    private IKey keySelectedToAdd;
    private IKey keySelectedToRemove;

    @Override
    public void showView() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setController(IKeysController controller) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setPossibleKeys(Iterable<IKey> keys) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IdGroup getIdGroupSelected() {
        return this.idGroupSelected;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IKey getKeySelectedToAdd() {
        return this.keySelectedToAdd;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IKey getKeySelectedToRemove() {
        return this.keySelectedToRemove;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setExistIdGroup(Iterable<IdGroup> idGroupFromKeys) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setIdGroupSelected(IdGroup idGroupSelected){
        this.idGroupSelected = idGroupSelected;
    }

    public void setKeySelectedToAdd(IKey key){
        this.keySelectedToAdd = key;
    }

    public void setKeySelectedToRemove(IKey key){
        this.keySelectedToRemove = key;
    }
}
