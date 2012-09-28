package controllers.tests;

import controllers.KeysController;
import controllers.tests.mocks.MockKeyView;
import infrastructure.ProjectContext;
import models.Attribute;
import models.IKey;
import models.IdGroup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class KeyControllerTest {

    private MockKeyView mockKeyView;
    private IdGroup idGroup;
    private Attribute key;
    private KeysController keysController;

    @Before
    public void setUp() {
        mockKeyView = new MockKeyView();
        idGroup = new IdGroup("1");
        key = new Attribute("att1");
        mockKeyView.setIdGroupSelected(idGroup);
        mockKeyView.setKeySelectedToAdd(key);
        mockKeyView.setKeySelectedToRemove(key);
        keysController = new KeysController(new ProjectContext(), mockKeyView);
    }

    @Test
    public void addIdGroupToKey() {
        keysController.addIdGroupToKey();
        Assert.assertTrue(key.getIdGroup().exists("1"));
    }

    @Test
    public void removeIdGroupFromKey() {
        try {
            key.getIdGroup().addIdGroup(idGroup);
        } catch (Exception e) {
            Assert.fail();
        }
        keysController.removeIdGroupFromKey();
        Assert.assertFalse(key.getIdGroup().exists(idGroup.getName()));
    }

    @Test
    public void validIdGroupName() throws Exception {
        Attribute att1 = new Attribute("att1");
        Attribute att2 = new Attribute("att2");
        att1.getIdGroup().addIdGroup(new IdGroup("id1"));
        att1.getIdGroup().addIdGroup(new IdGroup("id2"));
        att2.getIdGroup().addIdGroup(new IdGroup("id1"));

        List<IKey> iKeyList = new ArrayList<IKey>();
        iKeyList.add(att1);
        iKeyList.add(att2);

        keysController = new KeysController(new ProjectContext(), mockKeyView);
        this.keysController.setPossibleKeys(iKeyList);
        Assert.assertFalse(keysController.validIdGroupName(""));
        Assert.assertFalse(keysController.validIdGroupName("id1"));
        Assert.assertFalse(keysController.validIdGroupName("id2"));
        Assert.assertTrue(keysController.validIdGroupName("name"));
    }
}
