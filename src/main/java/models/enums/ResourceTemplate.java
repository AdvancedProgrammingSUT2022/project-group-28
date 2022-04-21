package models.enums;

public enum ResourceTemplate {
    BANANA("Banana", "BONUS", 1, 0, 0, ImprovementTemplate.PLANTATION),
    COW("Cow", "BONUS", 1, 0, 0, ImprovementTemplate.PASTURE),
    GAZELLE("Gazelle", "BONUS", 1, 0, 0, ImprovementTemplate.CAMP),
    SHEEP("Sheep", "BONUS", 2, 0, 0, ImprovementTemplate.PASTURE),
    WHEAT("Wheat", "BONUS", 1, 0, 0, ImprovementTemplate.FARM),
    COAL("Coal", "STRATEGIC", 0, 1, 0, ImprovementTemplate.MINE),
    HORSE("Horse", "STRATEGIC", 0, 1, 0, ImprovementTemplate.PASTURE),
    IRON("Iron", "STRATEGIC", 0, 1, 0, ImprovementTemplate.MINE),
    COTTON("Cotton", "LUXURY", 0, 0, 2, ImprovementTemplate.PLANTATION),
    DYE("Dye", "LUXURY", 0, 0, 2, ImprovementTemplate.PLANTATION),
    FUR("Fur", "LUXURY", 0, 0, 2, ImprovementTemplate.CAMP),
    GEM("Gem", "LUXURY", 0, 0, 3, ImprovementTemplate.MINE),
    GOLD("Gold", "LUXURY", 0, 0, 2, ImprovementTemplate.MINE),
    INCENSE("Incense", "LUXURY", 0, 0, 2, ImprovementTemplate.PLANTATION),
    IVORY("Ivory", "LUXURY", 0, 0, 2, ImprovementTemplate.CAMP),
    MARBLE("Marble", "LUXURY", 0, 0, 2, ImprovementTemplate.QUARRY),
    SILK("Silk", "LUXURY", 0, 0, 2, ImprovementTemplate.PLANTATION),
    SILVER("Silver", "LUXURY", 0, 0, 2, ImprovementTemplate.MINE),
    SUGAR("Sugar", "LUXURY", 0, 0, 2, ImprovementTemplate.PLANTATION);

    private String name;
    private String type;
    private int food;
    private int production;
    private int gold;
    private int eraNumber;
    private ImprovementTemplate requiredImprovement;
    // TODO: Add required improvement, technology

    ResourceTemplate(String name, String type, int food, int production, int gold, ImprovementTemplate requiredImprovement) {
        this.name = name;
        this.type = type;
        this.food = food;
        this.production = production;
        this.gold = gold;
        this.requiredImprovement = requiredImprovement;
    }
}
