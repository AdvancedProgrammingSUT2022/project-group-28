package models.units.enums;

import models.civilization.enums.TechnologyTemplate;
import models.tiles.enums.ResourceTemplate;

public enum UnitTemplate {
    WORKER("Worker", 70, 0, 0, 0, 2, 1, null,null),
    SETTLER("Settler", 89, 0, 0, 0, 2, 1, null,null),
    WARRIOR("Warrior", 40, 6, 0, 0, 2, 1, null,null),
    ARCHER("Archer", 70, 4, 6, 2, 2, 1, null,null),
    SCOUT("Scout", 25, 4, 0, 0, 2, 1, null,null),
    CHARIOT_ARCHER("Chariot Archer", 60, 3, 6, 2, 4, 1, ResourceTemplate.HORSE,TechnologyTemplate.THE_WHEEL),
    SPEARMAN("Spearman", 50, 7, 0, 0, 2, 1, null,TechnologyTemplate.BRONZE_WORKING),

    CATAPULT("Catapult", 100, 4, 14, 2, 2, 2, ResourceTemplate.IRON, TechnologyTemplate.MATHEMATICS),
    HORSEMAN("Horseman", 80, 12, 0, 0, 4, 2, ResourceTemplate.HORSE, TechnologyTemplate.HORSEBACK_RIDING),
    SWORDSMAN("Swordsman", 80, 11, 0, 0, 2, 2, ResourceTemplate.IRON, TechnologyTemplate.IRON_WORKING),
    
    CROSSBOWMAN("Crossbowman", 120, 6, 12, 2, 2, 3, null, TechnologyTemplate.MACHINERY),
    KNIGHT("Knight", 150, 18, 0, 0, 3, 3, ResourceTemplate.HORSE, TechnologyTemplate.CHIVALRY),
    LONGSWORDSMAN("Longswordsman", 150, 18, 0, 0, 3, 3, ResourceTemplate.IRON, TechnologyTemplate.STEEL),
    PIKEMAN("Pikeman", 100, 10, 0, 0, 2, 3, null, TechnologyTemplate.CIVIL_SERVICE),
    TREBUCHET("Trebuchet", 170, 6, 20, 2, 2, 3, ResourceTemplate.IRON, TechnologyTemplate.PHYSICS),
    
    CANON("Canon", 250, 10, 26, 2, 2, 4, null, TechnologyTemplate.CHEMISTRY),
    CAVALRY("Cavalry", 260, 25, 0, 0, 3, 4, ResourceTemplate.HORSE, TechnologyTemplate.MILITARY_SCIENCE),
    Lancer("Lancer", 220, 22, 0, 0, 4, 4, ResourceTemplate.HORSE, TechnologyTemplate.METALLURGY),
    MUSKETMAN("Musketman", 120, 16, 0, 0, 2, 4, null, TechnologyTemplate.GUNPOWDER),
    RIFLEMAN("Rifleman", 200, 25, 0, 0, 2, 4, null, TechnologyTemplate.RIFLING),
    
    ANTI_TANK("Anti-Tank Gun", 300, 32, 0, 0, 2, 5, null, TechnologyTemplate.REPLACEABLE_PARTS),
    ARTILLERY("Artillery", 420, 16, 32, 3, 2, 5, null, TechnologyTemplate.DYNAMITE),
    INFANTRY("Infantry", 300, 36, 0, 0, 2, 5, null,TechnologyTemplate.REPLACEABLE_PARTS),
    PANZER("Panzer", 450, 60, 0, 0, 5, 5, null, TechnologyTemplate.COMBUSTION),
    TANK("Tank", 450, 50, 0, 0, 4, 5, null, TechnologyTemplate.COMBUSTION);
    
    // TODO: Notes!!

    private String name;
    private int cost;
    private int combatStrength;
    private int rangedCombatStrength;
    private int range;
    private int movementPoint;
    private int eraNumber;
    private ResourceTemplate requiredResource;
    private TechnologyTemplate requiredTechnology;

    UnitTemplate(String name, int cost, int combatStrength, int rangedCombatStrength, int range, int movementPoint,
                 int eraNumber, ResourceTemplate requiredResource, TechnologyTemplate requiredTechnology) {
        this.name = name;
        this.cost = cost;
        this.combatStrength = combatStrength;
        this.rangedCombatStrength = rangedCombatStrength;
        this.range = range;
        this.movementPoint = movementPoint;
        this.eraNumber = eraNumber;
        this.requiredResource = requiredResource;
        this.requiredTechnology = requiredTechnology;
    }

    public TechnologyTemplate getRequiredTechnology() {
        return requiredTechnology;
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