package models.domain;

import models.Cardinality;

public class RelationshipWithClass {
	
	private Cardinality cardinality;
	private DomainClass clase;
	
	public RelationshipWithClass(DomainClass clase, double maxCardinality){
		this.setClase(clase);
		try{
			this.setCardinality(new Cardinality(maxCardinality));
		}
		catch (Exception e) {e.printStackTrace();}
		 
		
	}
	public RelationshipWithClass(DomainClass clase, Cardinality cardinality){
		this.setClase(clase);
		this.setCardinality(cardinality);		
	}

	public Cardinality getCardinality() {
		return cardinality;
	}

	public void setCardinality(Cardinality cardinality) {
		this.cardinality = cardinality;
	}

	public DomainClass getClase() {
		return clase;
	}

	public void setClase(DomainClass clase) {
		this.clase = clase;
	}

}
