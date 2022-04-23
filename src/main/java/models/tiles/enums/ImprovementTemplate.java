package models.tiles.enums;

public enum ImprovementTemplate {
    ROAD("Road", 3, 0, 0, 0),
    RAILROAD("Railroad", 4, 0, 0, 0), // TODO: change turn cost
    CAMP("Camp", 6, 0, 0, 0),
    FARM("Farm", 6, 1, 0, 0),
    LUMBERMILL("Lumbermill", 6, 0, 1, 0),
    MINE("Mine", 6, 0, 1, 0),
    PASTURE("Pasture", 7, 0, 0, 0),
    PLANTATION("Plantation", 5, 0, 0, 0),
    QUARRY("Quarry", 7, 0, 0, 0),
    TRADING_POST("Trading post", 8, 0, 0, 1),
    REMOVE_JUNGLE("Remove jungle", 7, 0, 0, 0),
    REMOVE_FOREST("Remove forest", 4, 0, 0, 0),
    REMOVE_MARSH("Remove marsh", 6, 0, 0, 0),
    REMOVE_ROAD("Remove Road", 3, 0, 0, 0), // TODO: change turn cost
    REPAIR("Repair", 3, 0, 0, 0);

    private String name;
    private int turnCost;
    private int food;
    private int production;
    private int gold;
    // TODO: Add required tech
    ImprovementTemplate(String name, int turnCost, int food, int production, int gold) {
        this.name = name;
        this.turnCost = turnCost;
        this.food = food;
        this.production = production;
        this.gold = gold;
    }
}
