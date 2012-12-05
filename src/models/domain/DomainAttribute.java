package models.domain;

import java.util.UUID;

import models.Cardinality;

import models.INameable;

public class DomainAttribute implements INameable{

    private String name;
    private Cardinality cardinality;
    private UUID myID;
    //podria haber un tipo de atributo si se le quiere poner int o string u otra cosa pero por ahora son solo 1 nombre
    
    public DomainAttribute ( String name , double  maxCardinality , UUID id){
    	this.setName(name);
    	this.setMyID(id);
    	try{
    		cardinality = new Cardinality(maxCardinality);
    	}
    	catch (Exception e) {e.printStackTrace();}
    	
    }
    
    public DomainAttribute ( String name , Cardinality cardinality , UUID id){
    	this.setName(name);
    	this.setMyID(id);
    	this.setCardinality(cardinality);    	
    }

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UUID getMyID() {
		return myID;
	}

	public void setMyID(UUID myID) {
		this.myID = myID;
	}

	public Cardinality getCardinality() {
		return cardinality;
	}

	public void setCardinality(Cardinality cardinality) {
		this.cardinality = cardinality;
	}

}
