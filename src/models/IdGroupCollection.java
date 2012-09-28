package models;

import infrastructure.Func;
import infrastructure.IterableExtensions;

import java.util.ArrayList;
import java.util.Iterator;

public class IdGroupCollection implements Iterable<IdGroup> {


    public IdGroupCollection() {
        this.idGroups = new ArrayList<IdGroup>();
    }

    public Iterable<IdGroup> getIdGroups() {
        return this.idGroups;
    }

    public IdGroup addIdGroup(IdGroup idGroup) throws Exception {
        if (!this.exists(idGroup.getName())) {
            this.idGroups.add(idGroup);
            return idGroup;
        } else {
            throw new Exception("Group's name : " + idGroup.getName() + "already exists for this id");
        }
    }

    public void removeIdGroup(IdGroup idGroup) throws Exception {

        if (this.exists(idGroup.getName())) {
            this.idGroups.remove(idGroup);
        } else {
            throw new Exception("Group's number: " + idGroup.getName() + "do not exists");
        }

    }

    public IdGroup getIdGroup(String name){
        return IterableExtensions.firstOrDefault(this.idGroups, new IntegerCmpFunc(), name);
    }

    public boolean exists(String name) {
        return IterableExtensions.firstOrDefault(this.idGroups, new IntegerCmpFunc(), name) != null;
    }

    public int count() {
        return this.idGroups.size();
    }


    private ArrayList<IdGroup> idGroups;

    private class IntegerCmpFunc extends Func<IdGroup, String, Boolean> {

        @Override
        public Boolean execute(IdGroup idGroup, String idGroupNumber) {
            return idGroup.getName().equals(idGroupNumber);
        }
    }

	@Override
	public Iterator<IdGroup> iterator() {
		return this.idGroups.iterator();
	}


}
