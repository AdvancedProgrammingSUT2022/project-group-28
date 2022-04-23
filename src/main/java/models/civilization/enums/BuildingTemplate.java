package models.civilization.enums;

public enum BuildingTemplate {
    BARRACKS("Barracks", 80, 1, 0, 1),
    GRANARY("Granary", 100, 1, 0,  1);

    // TODO: Add all buildings

    private String name;
    private int cost;
    private int maintenance;
    private int slots;
    private int eraNumber;
    // TODO: Add required techs
    BuildingTemplate (String name, int cost, int maintenance, int slots, int eraNumber) {
        this.name = name;
        this.cost = cost;
        this.maintenance = maintenance;
        this.slots = slots;
        this.eraNumber = eraNumber;
    }
}
