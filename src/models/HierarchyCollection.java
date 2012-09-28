package models;

import infrastructure.Func;
import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class HierarchyCollection implements Iterable<Hierarchy>{

    public HierarchyCollection() {
        this.hierarchies = new ArrayList<Hierarchy>();
    }

    public Hierarchy createHierarchy(UUID generalEntityUUID, boolean exclusive, boolean total, ArrayList<UUID> childrenUUID) {
        Hierarchy hierarchy = new Hierarchy(generalEntityUUID, exclusive, total, childrenUUID);
        this.hierarchies.add(hierarchy);
        return hierarchy;
    }

    public Hierarchy createHierarchy(UUID generalEntityUUID, boolean exclusive, boolean total) {
        Hierarchy hierarchy = new Hierarchy(generalEntityUUID, exclusive, total);
        this.hierarchies.add(hierarchy);
        return hierarchy;
    }

    public Hierarchy createHierarchy(UUID hierarchyUUID, UUID generalEntityUUID, boolean exclusive, boolean total, ArrayList<UUID> childrenUUID) throws Exception {
        if (!existsHierarchy(hierarchyUUID)) {
            Hierarchy hierarchy = this.createHierarchy(generalEntityUUID, exclusive, total, childrenUUID);
            hierarchy.setHierarchyUUID(hierarchyUUID);
            return hierarchy;
        } else {
            throw new Exception("A hierarchy with uuid: " + hierarchyUUID + " already exists");
        }
    }

    public int count() {
        return IterableExtensions.count(this.hierarchies);
    }

    public Hierarchy getHierarchy(UUID hierarchyUUID) {
        return IterableExtensions.firstOrDefault(this.hierarchies, new HierarchyCmpFunc(), hierarchyUUID);
    }

    public Iterable<Hierarchy> getHierarchiesWithGeneralEntityUUID(UUID generalEntityUUID) {
        return IterableExtensions.where(this.hierarchies, new GeneralHierarchyCmpFunc(), generalEntityUUID);
    }

    public Iterable<Hierarchy> getHierarchies() {
        return this.hierarchies;
    }

    public void addChild(UUID hierarchyUUID, UUID childEntityUUID) throws Exception {
        Hierarchy hierarchy = this.getHierarchy(hierarchyUUID);
        if (hierarchy != null) {
            hierarchy.addChildEntity(childEntityUUID);
        } else {
            throw new Exception("Do not exists a hierarchy with uuid: " + hierarchyUUID);
        }
    }


    public void removeHierarchy(UUID hierarchyUUID) throws Exception {
        if (existsHierarchy(hierarchyUUID)) {
            this.hierarchies.remove(this.getHierarchy(hierarchyUUID));
        } else {
            throw new Exception("Do not exists a hierarchy with uuid: " + hierarchyUUID);
        }
    }

    private ArrayList<Hierarchy> hierarchies;

    private boolean existsHierarchy(UUID entityUUID) {
        return IterableExtensions.firstOrDefault(this.hierarchies, new HierarchyCmpFunc(), entityUUID) != null;
    }


    private class GeneralHierarchyCmpFunc extends Func<Hierarchy, UUID, Boolean> {
        @Override
        public Boolean execute(Hierarchy hierarchy, UUID uuid) {
            return hierarchy.getGeneralEntityId().equals(uuid);
        }

    }

    private class HierarchyCmpFunc extends Func<Hierarchy, UUID, Boolean> {
        @Override
        public Boolean execute(Hierarchy hierarchy, UUID uuid) {
            return hierarchy.getId().equals(uuid);
        }

    }

	public void add(Hierarchy hierarchy) {
		this.hierarchies.add(hierarchy);
	}

	@Override
	public Iterator<Hierarchy> iterator() {
		return this.hierarchies.iterator();
	}
}
