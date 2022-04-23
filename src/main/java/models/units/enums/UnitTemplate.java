package models.units.enums;

import models.tiles.enums.ResourceTemplate;

public enum UnitTemplate {
    WORKER("Worker", 70, 0, 0, 0, 2, 1, null),
    SETTLER("Settler", 89, 0, 0, 0, 2, 1, null),
    WARRIOR("Warrior", 40, 6, 0, 0, 2, 1, null),
    ARCHER("Archer", 70, 4, 6, 2, 2, 1, null),
    SCOUT("Scout", 25, 4, 0, 0, 2, 1, null);
    // TODO: add all units

    private String name;
    private int cost;
    private int combatStrength;
    private int rangedCombatStrength;
    private int range;
    private int movementPoint;
    private int eraNumber;
    private ResourceTemplate requiredResource;
    // TODO: Add requiredTechnology
    UnitTemplate(String name, int cost, int combatStrength, int rangedCombatStrength, int range, int movementPoint,
                 int eraNumber, ResourceTemplate requiredResource) {
        this.name = name;
        this.cost = cost;
        this.combatStrength = combatStrength;
        this.rangedCombatStrength = rangedCombatStrength;
        this.range = range;
        this.movementPoint = movementPoint;
        this.eraNumber = eraNumber;
        this.requiredResource = requiredResource;
    }
}