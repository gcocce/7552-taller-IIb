package models.domain;
import java.util.UUID;

import models.INameable;;

public class DomainRelationship implements INameable{
	
	private String name;
	private UUID id;
	private Boolean isComposition;
	private RelationshipWithClass relatedClass1;		//clase que es relacionada por esta relacion
	private RelationshipWithClass relatedClass2;	    //clase que es relacionada por esta relacion
	private DomainClass compositionClass;   			//clase que puede surguir de si esta relacion es una composicion
	
	public DomainRelationship(UUID id, String name, RelationshipWithClass rel1, RelationshipWithClass rel2) {
		this.name = name;
		this.id = id;
		this.setRelatedClass1(rel1);
		this.setRelatedClass2(rel2);
		this.setCompositionClass(null);
		this.isComposition = false;
	
	}
	
	public DomainRelationship(UUID id, String name, RelationshipWithClass rel1, RelationshipWithClass rel2, DomainClass claseComposition) {
		this(id,name,rel1,rel2);
		this.setCompositionClass(claseComposition);		
		
	}
	

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public UUID getId() {
		return this.id;
	}
	
	public void isComposition(Boolean isComposition) {
		this.isComposition = isComposition;
	}
	
	public Boolean isComposition() {
		return isComposition;
	}

	public RelationshipWithClass getRelatedClass1() {
		return relatedClass1;
	}

	public void setRelatedClass1(RelationshipWithClass relatedClass1) {
		this.relatedClass1 = relatedClass1;
	}

	public RelationshipWithClass getRelatedClass2() {
		return relatedClass2;
	}

	public void setRelatedClass2(RelationshipWithClass relatedClass2) {
		this.relatedClass2 = relatedClass2;
	}

	public DomainClass getCompositionClass() {
		return compositionClass;
	}

	public void setCompositionClass(DomainClass compositionClass) {
		this.compositionClass = compositionClass;
		this.isComposition = true;
	}

}
