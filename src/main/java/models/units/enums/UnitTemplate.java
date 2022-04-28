package models.units.enums;

import models.tiles.enums.ResourceTemplate;

public enum UnitTemplate {
    WORKER("Worker", 70, 0, 0, 0, 2, 1, null),
    SETTLER("Settler", 89, 0, 0, 0, 2, 1, null),
    WARRIOR("Warrior", 40, 6, 0, 0, 2, 1, null),
    ARCHER("Archer", 70, 4, 6, 2, 2, 1, null),
    SCOUT("Scout", 25, 4, 0, 0, 2, 1, null),
    CHARIOT_ARCHER("Chariot Archer", 60, 3, 6, 2, 4, 1, null),
    
    SPEARMAN("Spearman", 50, 7, 0, 0, 2, 1, null),
    CATAPULT("Catapult", 100, 4, 14, 2, 2, 2, null),
    HORSEMAN("Horseman", 80, 12, 0, 0, 4, 2, null),
    SWORDSMAN("Swordsman", 80, 11, 0, 0, 2, 2, null),
    
    CROSSBOWMAN("Crossbowman", 120, 6, 12, 2, 2, 3, null),
    KNIGHT("Knight", 150, 18, 0, 0, 3, 3, null),
    LONGSWORDSMAN("Longswordsman", 150, 18, 0, 0, 3, 3, null),
    PIKEMAN("Pikeman", 100, 10, 0, 0, 2, 3, null),
    TREBUCHET("Trebuchet", 170, 6, 20, 2, 2, 3, null),
    
    CANON("Canon", 250, 10, 26, 2, 2, 4, null),
    CAVALRY("Cavalry", 260, 25, 0, 0, 3, 4, null),
    Lancer("Lancer", 220, 22, 0, 0, 4, 4, null),
    MUSKETMAN("Musketman", 120, 16, 0, 0, 2, 4, null),
    RIFLEMAN("Rifleman", 200, 25, 0, 0, 2, 4, null),
    
    ANTI_TANK("Anti-Tank Gun", 300, 32, 0, 0, 2, 5, null),
    ARTILLERY("Artillery", 420, 16, 32, 3, 2, 5, null),
    INFANTRY("Infantry", 300, 36, 0, 0, 2, 5, null),
    PANZER("Panzer", 450, 60, 0, 0, 5, 5, null),
    TANK("Tank", 450, 50, 0, 0, 4, 5, null);
    
    // TODO: add required resource and tech
    // TODO: Notes!!

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

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getCombatStrength() {
        return combatStrength;
    }

    public int getRangedCombatStrength() {
        return rangedCombatStrength;
    }

    public int getRange() {
        return range;
    }

    public int getMovementPoint() {
        return movementPoint;
    }

    public int getEraNumber() {
        return eraNumber;
    }

    public ResourceTemplate getRequiredResource() {
        return requiredResource;
    }
}