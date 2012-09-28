package models;

import java.util.UUID;

public interface IKey {

    public String getName();

    public UUID getId();

    public IdGroupCollection getIdGroup();
}
