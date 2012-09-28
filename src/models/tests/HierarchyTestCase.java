package models.tests;

import junit.framework.Assert;
import models.Hierarchy;
import org.junit.Test;

import java.util.UUID;

public class HierarchyTestCase {

    @Test
    public void testCreateHierarchy() {

        UUID uuid = UUID.randomUUID();
        Hierarchy hierarchy = new Hierarchy(uuid, false, false);

        Assert.assertEquals(uuid, hierarchy.getGeneralEntityId());
        Assert.assertEquals(false, hierarchy.isExclusive());
        Assert.assertEquals(false, hierarchy.isTotal());

        hierarchy = new Hierarchy(uuid, true, true);
        Assert.assertEquals(true, hierarchy.isExclusive());
        Assert.assertEquals(true, hierarchy.isTotal());
    }

    @Test
    public void testHierarchyAddChild() {
        UUID uuid = UUID.randomUUID();
        Hierarchy hierarchy = new Hierarchy(uuid, true, true);
        try {
            hierarchy.addChildEntity(UUID.randomUUID());
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals(1, hierarchy.count());
    }

    @Test(expected = Exception.class)
    public void testHierarchyAddChildFail() throws Exception {
        UUID uuid = UUID.randomUUID();
        Hierarchy hierarchy = new Hierarchy(uuid, true, true);
        try {
            hierarchy.addChildEntity(uuid);
        } catch (Exception e) {
            Assert.fail();
        }
        hierarchy.addChildEntity(uuid);
    }

    @Test
    public void testHierarchyRemoveChild() {
        UUID uuid = UUID.randomUUID();
        Hierarchy hierarchy = new Hierarchy(uuid, true, true);
        try {
            hierarchy.addChildEntity(uuid);
        } catch (Exception e) {
            Assert.fail();
        }
        try {
            hierarchy.removeChild(uuid);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = Exception.class)
    public void testHierarchyRemoveChildFailed() throws Exception {
        UUID uuid = UUID.randomUUID();
        Hierarchy hierarchy = new Hierarchy(uuid, true, true);
        hierarchy.removeChild(uuid);
    }

    @Test
    public void testHasChild() {
        UUID uuid = UUID.randomUUID();
        Hierarchy hierarchy = new Hierarchy(uuid, true, true);
        Assert.assertEquals(false, hierarchy.hasChild(uuid));
        UUID child = UUID.randomUUID();
        try {
            hierarchy.addChildEntity(child);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals(true, hierarchy.hasChild(child));
    }
}
