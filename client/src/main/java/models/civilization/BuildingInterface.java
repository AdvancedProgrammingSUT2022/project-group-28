package models.civilization;

public interface BuildingInterface {
    boolean isAvailableToBuild(City city);
    void giveEffect(City city);
}
