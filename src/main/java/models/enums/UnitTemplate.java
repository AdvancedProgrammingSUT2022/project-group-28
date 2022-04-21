package models.enums;

public enum UnitTemplate {
    WORKER("Worker", 70, 0, 0, 0, 2, 1),
    SETTLER("Settler", 89, 0, 0, 0, 2, 1),
    WARRIOR("Warrior", 40, 6, 0, 0, 2, 1),
    ARCHER("Archer", 70, 4, 6, 2, 2, 1),
    SCOUT("Scout", 25, 4, 0, 0, 2, 1);
    // TODO: add all units

    private String name;
    private int cost;
    private int combatStrength;
    private int rangedCombatStrength;
    private int range;
    private int movementPoint;
    private int eraNumber;

    // TODO: add requiredResource requiredTechnology
    UnitTemplate(String name, int cost, int combatStrength, int rangedCombatStrength, int range, int movementPoint, int eraNumber) {
        this.name = name;
        this.cost = cost;
        this.combatStrength = combatStrength;
        this.rangedCombatStrength = rangedCombatStrength;
        this.range = range;
        this.movementPoint = movementPoint;
        this.eraNumber = eraNumber;
    }
}