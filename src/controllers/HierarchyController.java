package controllers;

import java.util.ArrayList;
import java.util.List;

import controllers.listeners.IHierarchyEventListener;
import models.Entity;
import models.Hierarchy;
import views.IHierarchyView;
import infrastructure.IProjectContext;

public class HierarchyController extends BaseController implements IHierarchyController {

	private IHierarchyView hierarchyView;
	private Hierarchy pendingHierarchy;
	private List<IHierarchyEventListener> listeners;
	
	public HierarchyController(IProjectContext projectContext,
			IHierarchyView hierachyView) {
		super(projectContext);
		this.pendingHierarchy = new Hierarchy();
		this.setHierachyView(hierachyView);
		this.listeners = new ArrayList<IHierarchyEventListener>();
	}
	
	@Override
	public void setHierachyView(IHierarchyView hierarchyView)
	{
		this.hierarchyView = hierarchyView;
		this.hierarchyView.setController(this);
	}
	
	@Override
	public void create(Hierarchy hierarchy) {
		this.pendingHierarchy = hierarchy;
		this.hierarchyView.update();
		this.hierarchyView.showView();
	}
	
	@Override
	public void create() {
		this.hierarchyView.create();
		this.hierarchyView.showView();
	}
	
	@Override
	public void addSuscriber(IHierarchyEventListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public boolean addHierarchy() {
		if (this.pendingHierarchy.getGeneralEntityId() == null)
			return false;
		if (this.pendingHierarchy.count() == 0)
			return false;
		if (this.pendingHierarchy.count() == 1) {
			this.setExclusive(false);
			this.setTotal(false);
		}
		for (IHierarchyEventListener listeners : this.listeners)
			listeners.handleCreatedEvent(this.pendingHierarchy);
		return true;
	}

	@Override
	public boolean setGeneralEntity(Entity entity) {
		if (entity == null)
		{
			this.pendingHierarchy.setGeneralEntityId(null);
			return true;
		}
		if (this.pendingHierarchy.hasChild(entity.getId()))
			return false;
		
		this.pendingHierarchy.setGeneralEntityId(entity.getId());
		return true;
	}
	
	@Override
	public boolean addSpecificEntity(Entity entity) {
		if (this.pendingHierarchy.getGeneralEntityId() != null)
			if (this.pendingHierarchy.getGeneralEntityId().equals(entity.getId()))
				return false;
		
		try {
			this.pendingHierarchy.addChildEntity(entity.getId());
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean removeSpecificEntity(Entity entity) {
		try {
			this.pendingHierarchy.removeChild(entity.getId());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public Hierarchy getPendingHierarchy() {
		return this.pendingHierarchy;
	}

	@Override
	public void setTotal(boolean total) {
		this.pendingHierarchy.setTotal(total);
	}

	@Override
	public void setExclusive(boolean exclusive) {
		this.pendingHierarchy.setExclusive(exclusive);
	}

	@Override
	public boolean relationshipIsTotal() {
		return this.pendingHierarchy.isTotal();
	}
	
	@Override
	public boolean relationshipIsExclusive() {
		return this.pendingHierarchy.isExclusive();
	}
	
	@Override
	public Iterable<Entity> getAvailableEntities() {
		return this.projectContext.getContextEntities();
	}

	@Override
	public boolean hasSpecificEntity(Entity entity) {
		return this.pendingHierarchy.hasChild(entity.getId());
	}

	@Override
	public boolean isGeneralEntity(Entity entity) {
		if (this.pendingHierarchy.getGeneralEntityId() == null || entity == null)
			return false;
		if (this.pendingHierarchy.getGeneralEntityId().equals(entity.getId()))
			return true;
		return false;
	}
}
