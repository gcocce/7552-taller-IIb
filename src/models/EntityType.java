package models;

public enum EntityType {
    None("None", 0), Thing("Thing", 1), Domain("Domain", 2), Historic("Historic", 3), Programmed("Programmed", 4);
    private String description;
    private int id;

    EntityType(String name, int id) {
        this.description = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.description;
    }

    public int getId() {
        return this.id;
    }
}
