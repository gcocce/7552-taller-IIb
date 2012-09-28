package controllers;

import models.IKey;
import models.IdGroup;
import views.IKeysView;

public interface IKeysController {

    public void create();

    public void addIdGroupToKey();

    public void setKeyView(IKeysView keysView);

    public void removeIdGroupFromKey();

    boolean validIdGroupName(String name);

    void removeIdGroupFromAllIdGroups(IdGroup selectedValue);

	void setPossibleKeys(Iterable<IKey> possibleKeys);
}
