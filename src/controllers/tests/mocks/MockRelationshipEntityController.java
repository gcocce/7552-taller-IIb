package controllers.tests.mocks;

import infrastructure.IProjectContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import models.Cardinality;
import models.Entity;
import models.EntityCollection;
import models.RelationshipEntity;
import views.IRelationshipEntityView;
import controllers.BaseController;
import controllers.IRelationshipEntityController;
import controllers.listeners.IRelationshipEntityEventListener;

public class MockRelationshipEntityController extends BaseController implements
		IRelationshipEntityController {
	
	private List<RelationshipEntity> relationshipEntity;
	private EntityCollection entCol;	
	public boolean isAreAllSame() {
		return areAllSame;
	}

	public void setAreAllSame(boolean areAllSame) {
		this.areAllSame = areAllSame;
	}

	private boolean areAllSame;
	
	public MockRelationshipEntityController(IProjectContext projectContext) {
		super(projectContext);
		entCol = new EntityCollection();
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub

	}

	@Override

	public void add(UUID uuid, Cardinality card, String role,boolean isStrong) {
		relationshipEntity.add(new RelationshipEntity (uuid,card,role,isStrong));
	}

	@Override
	public void modify(UUID uuid, Cardinality card, String role,boolean isStrong)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(UUID uuid) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<RelationshipEntity> getRelationshipEntities() {
		return relationshipEntity;
	}
	
	public void setRelationshipEntities(List<RelationshipEntity> list) {
		relationshipEntity = list;
	}


	@Override
	public void setRelationshipEntityView(IRelationshipEntityView view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSuscriber(IRelationshipEntityEventListener listener) {
		// TODO Auto-generated method stub

	}

	public void createTestingList () {
		Entity ent1 = new Entity ("Entity1");
		Entity ent2 = new Entity ("Entity2");
		Entity ent3 = new Entity ("Entity3");
		Entity ent4 = new Entity ("Entity4");
		Entity ent5 = new Entity ("Entity5");
		Entity ent6 = new Entity ("Entity6");
		entCol = new EntityCollection();
		entCol.add(ent1); 
		entCol.add(ent2);
		entCol.add(ent3);
		entCol.add(ent4);
		entCol.add(ent5);
		entCol.add(ent6);
		System.out.println("Creating test List "+ entCol.count());
		relationshipEntity = new ArrayList<RelationshipEntity>();
		
		try {
			relationshipEntity.add(new RelationshipEntity(ent1,new Cardinality(0, 5),"role1"));
			relationshipEntity.add(new RelationshipEntity(ent2,new Cardinality(0, Double.POSITIVE_INFINITY),"role2"));
			relationshipEntity.add(new RelationshipEntity(ent3));
			RelationshipEntity rel1 = new RelationshipEntity(ent4);
			relationshipEntity.add(rel1);
			relationshipEntity.add(new RelationshipEntity(ent5));
			relationshipEntity.add(new RelationshipEntity(ent6));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private Entity findEntity(UUID id) {
		
		for (Entity ent : entCol) {
			if (ent.getId().equals(id)) {
				System.out.println("Encontrado " + id);
				return ent;
			}
			
		}
		return null;
	}
	
	
	@Override
	public List<Object[]> getListForModel() {
		List<Object[]> list = new ArrayList<Object[]>();
		for (RelationshipEntity relEnt : relationshipEntity) {
				Entity ent  = findEntity(relEnt.getEntityId()); 			
								
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
			
		return list;
	}

	@Override
	public Iterable<Entity> getEntities() {
		return entCol ;
	}

	@Override
	public boolean entitiesAreSameType() {
		return areAllSame;
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
					
					String role = (row[3].toString().equals(""))?"":row[3].toString();
					boolean strong = (Boolean)row[4];
					RelationshipEntity rel = new RelationshipEntity (id,new Cardinality (minCard,maxCard),role,strong);
					listAux.add(rel);
			}
		}
		
		validateModel (listAux);
		
		relationshipEntity = listAux;
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
				throw new Exception ("The entity that is weak entity should have 1..1 cardinality");
		}
		
	}

	@Override
	public void setRelatinshipEntities(
			List<RelationshipEntity> relationshipEntities) {
		this.relationshipEntity = relationshipEntities;
	}

	@Override
	public boolean isUnary() {
		// TODO Auto-generated method stub
		return false;
	}

}
