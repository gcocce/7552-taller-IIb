package models;

import infrastructure.Func;
import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.UUID;

public class Hierarchy {

	public Hierarchy(UUID generalEntityUUID, boolean exclusive, boolean total) {
		this.exclusive = exclusive;
		this.total = total;
		this.hierarchyUUID = UUID.randomUUID();
		this.children = new ArrayList<UUID>();
		this.generalEntityUUID = generalEntityUUID;
	}

	public Hierarchy(UUID generalEntityUUID, boolean exclusive, boolean total,
			ArrayList<UUID> childrenUUID) {
		this.exclusive = exclusive;
		this.total = total;
		this.hierarchyUUID = UUID.randomUUID();
		this.children = new ArrayList<UUID>();
		this.generalEntityUUID = generalEntityUUID;
		for (UUID childUUID : childrenUUID) {
			try {
				if (!hierarchyHasChild(childUUID))
					this.addChildEntity(childUUID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Hierarchy() {
		this(null, true, true);
	}

	public void setGeneralEntityId(UUID generalEntityUUID) {
		this.generalEntityUUID = generalEntityUUID;
	}

	public UUID getGeneralEntityId() {
		return generalEntityUUID;
	}

	public UUID getId() {
		return hierarchyUUID;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public boolean isTotal() {
		return total;
	}

	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}

	public void setTotal(boolean total) {
		this.total = total;
	}

	public void setHierarchyUUID(UUID hierarchyUUID) {
		this.hierarchyUUID = hierarchyUUID;
	}

	public void addChildEntity(UUID childEntityUUID) throws Exception {
		if (!hierarchyHasChild(childEntityUUID)) {
			this.children.add(childEntityUUID);
		} else {
			throw new Exception("The child with id: " + childEntityUUID
					+ " already exits");
		}
	}

	public Iterable<UUID> getChildren() {
		return children;
	}

	public void removeChild(UUID uuid) throws Exception {
		if (hierarchyHasChild(uuid)) {
			this.children.remove(uuid);
		} else {
			throw new Exception("Child with id: " + uuid + "do not exits");
		}
	}

	public boolean hasChild(UUID childUUID) {
		return this.hierarchyHasChild(childUUID);
	}

	public int count() {
		return this.children.size();
	}

	private boolean exclusive;
	private boolean total;
	private UUID generalEntityUUID;

	private UUID hierarchyUUID;
	private ArrayList<UUID> children;

	private boolean hierarchyHasChild(UUID uuid) {
		class UUIDCmpFunc extends Func<UUID, UUID, Boolean> {
			@Override
			public Boolean execute(UUID uuid, UUID n) {
				return uuid.equals(n);
			}
		}
		return IterableExtensions.firstOrDefault(this.children,
				new UUIDCmpFunc(), uuid) != null;
	}

	public String getSummary() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");

		if (this.total)
		{
			builder.append("T");
		} 
		else
		{
			builder.append("P");
		}
		
		builder.append(",");
		
		if (this.exclusive)
		{
			builder.append("E");
		} 
		else
		{
			builder.append("NE");
		}
		
		builder.append(")");
		
		return builder.toString();
	}

}
