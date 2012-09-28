package models;

public class IdGroup {
    String name;

    public IdGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Id Group: " + this.name;
    }
}
