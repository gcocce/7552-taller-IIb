package controllers;

import infrastructure.IProjectContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import models.Cardinality;
import models.Entity;
import models.EntityType;
import models.RelationshipEntity;
import views.IRelationshipEntityView;
import controllers.listeners.IRelationshipEntityEventListener;

public class RelationshipEntityController extends BaseController implements
		IRelationshipEntityController {

	private List<RelationshipEntity> listRelEnt;
	private IRelationshipEntityView relationshipEntityView;
	private List<IRelationshipEntityEventListener> listeners;

	public RelationshipEntityController(IProjectContext projectContext,
			IRelationshipEntityView relationshipEntityView) {
		super(projectContext);
		listeners = new ArrayList<IRelationshipEntityEventListener>();
		this.listRelEnt = new ArrayList<RelationshipEntity>();
		this.setRelationshipEntityView(relationshipEntityView);

	}

	@Override
	public void create() {
		this.relationshipEntityView.show();
	}

	@Override
	public void add(UUID uuid, Cardinality card, String role, boolean isStrong) {
		RelationshipEntity relEntity = new RelationshipEntity(uuid, card, role,
				isStrong);
		this.listRelEnt.add(relEntity);
		this.updateSuscribers(relEntity);
	}

	@Override
	public void modify(UUID uuid, Cardinality card, String role,
			boolean isStrong) throws Exception {
		RelationshipEntity aux = this.findRelationshipEntity(uuid);
		try {
			aux.setCardinality(card);
			aux.setRole(role);
			aux.setStrongEntity(isStrong);
		} catch (NullPointerException e) {
			throw new Exception("Error: Relationship-Entity with UUID " + uuid
					+ " not found");
		}

		this.updateSuscribers(aux);
	}

	@Override
	public List<Object[]> getListForModel() {

		List<Object[]> list = new ArrayList<Object[]>();
		for (RelationshipEntity relEnt : listRelEnt) {
			Entity ent = findEntity(relEnt.getEntityId());
			
			if (ent != null) {
				String minCard =  (relEnt.getCardinality()!=null) ?Cardinality.getStringForCardinality(relEnt.getCardinality().getMinimum()):"0"; 
				String maxCard =  (relEnt.getCardinality()!=null) ?Cardinality.getStringForCardinality(relEnt.getCardinality().getMaximum()):"0";
				String role = (relEnt.getRole()!= null) ? relEnt.getRole():"";
				
								
				Object[] obj = new Object[] {
					ent,
					minCard,
					maxCard,
					role, 
					relEnt.isStrongEntity() 
				};

				list.add(obj);
			}
		}
		
		return list;
	}
			
	
	

	private Entity findEntity(UUID id) {
		for (Entity ent : projectContext.getAllEntities()) {
			if (ent.getId().equals(id)) return ent;
		}
		return null;
	}

	@Override
	public void remove(UUID uuid) throws Exception {
		RelationshipEntity aux = this.findRelationshipEntity(uuid);
		if (aux != null)
			listRelEnt.remove(aux);
		else
			throw new Exception("Error: Relationship-Entity with UUID " + uuid
					+ " not found");

		this.updateSuscribers(aux);
	}

	@Override
	public List<RelationshipEntity> getRelationshipEntities() {
		return listRelEnt;
	}

	@Override
	public void setRelationshipEntityView(IRelationshipEntityView view) {
		relationshipEntityView = view;
		relationshipEntityView.setController(this);
	}

	private RelationshipEntity findRelationshipEntity(UUID uuid) {
		Iterator<RelationshipEntity> ite = listRelEnt.iterator();
		while (ite.hasNext()) {
			RelationshipEntity aux = ite.next();
			if (aux.getEntityId() == uuid)
				return aux;
		}
		return null;
	}

	@Override
	public void addSuscriber(IRelationshipEntityEventListener listener) {
		this.listeners.add(listener);
	}

	protected void updateSuscribers(RelationshipEntity relationshipEntity) {
		for (IRelationshipEntityEventListener listener : listeners)
			listener.handleCreatedEvent(relationshipEntity);
	}

	@Override
	public Iterable<Entity> getEntities() {
		return projectContext.getContextEntities();
	}

	
	@Override
	public boolean isUnary () {
		List<Object []> list = relationshipEntityView.getModelList();
		for (Object [] row1 : list) {
			for (Object [] row2 : list) {
				Entity ent1 = (Entity)row1[0];
				Entity ent2 = (Entity)row2[0];
				if (row1 != row2 && ent1.getId() == ent2.getId()) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	@Override
	public boolean entitiesAreSameType() {
		List<Object []> list = relationshipEntityView.getModelList();
		for (int i = 1 ; i < list.size(); i++ ){
			Entity ent1 = (Entity) list.get(i-1)[0];
			Entity ent2 = (Entity ) list.get(i)[0];
			if (ent1.getType() != ent2.getType()) 
				return false;
			if (ent1.getType() == EntityType.None || ent2.getType() == EntityType.None )
				return false;
		}
		return true;
	}

	@Override
	public void updateModel (List<Object[]> list) throws Exception {
		List <RelationshipEntity> listAux = new ArrayList<RelationshipEntity> ();
		
		for (int i =0 ; i < list.size();i++) {
			Object [] row = list.get(i);
			if (row[0] instanceof Entity) {
					UUID id = ((Entity)row[0]).getId();
					double minCard = (row[1].toString().equals(""))?0:Cardinality.getCardinalityFromString(row[1].toString());
					double maxCard = (row[2].toString().equals(""))?0:Cardinality.getCardinalityFromString(row[2].toString());
					
					if (minCard > maxCard) { 
						maxCard = minCard;
						row[2] = Cardinality.getStringForCardinality(maxCard);
					}
			
					String role = (row[3].toString().equals("") || row[3] == null )?"":row[3].toString();
					boolean strong = (Boolean)row[4];
					RelationshipEntity rel = new RelationshipEntity (id,new Cardinality (minCard,maxCard),role,strong);
					listAux.add(rel);
			}
		}
		
		validateModel (listAux);
		
		listRelEnt = listAux;
	}

	private boolean areThereAnyStrongEntities (List<RelationshipEntity> list) {
		
		for (RelationshipEntity rel : list) {
			if (rel.isStrongEntity()) return true;
		}
		
		return false;
	}
	
	private int getHowManyStrongEntitiesAre(List<RelationshipEntity> list) {
		int counter = 0;
		for (RelationshipEntity rel : list) {
			if (rel.isStrongEntity()) counter++;
		}
		return counter;
	}
	
	private boolean validateWeakEntityCardinality(List<RelationshipEntity> list) {
		for (RelationshipEntity rel : list) {
			if (rel.isStrongEntity() == false) {
				if (rel.getCardinality().getMaximum()==1 && rel.getCardinality().getMinimum() ==1 )
					return true;
			}
		}
		return false;
	}
	
	private void validateModel(List<RelationshipEntity> list) throws Exception{
		//Validate Strong Entities 
		if (areThereAnyStrongEntities(list)) {
			if (  list.size() != 2)
				throw new Exception ("There should be only two entities in a strong relationship");
			if ( getHowManyStrongEntitiesAre ( list) != 1) 
				throw new Exception ("The use of more than one strong entity per relationship is not permitted");
			if ( !validateWeakEntityCardinality (list)) 
				throw new Exception ("The weak entity should have 1..1 cardinality");
		}
		
	}

	@Override
	public void setRelatinshipEntities(
			List<RelationshipEntity> relationshipEntities) {
		this.listRelEnt = relationshipEntities;
	}
	
}
