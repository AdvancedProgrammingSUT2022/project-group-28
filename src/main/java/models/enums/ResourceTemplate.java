package models.enums;

public enum ResourceTemplate {
    BANANA("Banana", "BONUS", 1, 0, 0),
    COW("Cow", "BONUS", 1, 0, 0),
    GAZELLE("Gazelle", "BONUS", 1, 0, 0),
    SHEEP("Sheep", "BONUS", 2, 0, 0),
    WHEAT("Wheat", "BONUS", 1, 0, 0),
    COAL("Coal", "STRATEGIC", 0, 1, 0),
    HORSE("Horse", "STRATEGIC", 0, 1, 0),
    IRON("Iron", "STRATEGIC", 0, 1, 0),
    COTTON("Cotton", "LUXURY", 0, 0, 2),
    DYE("Dye", "LUXURY", 0, 0, 2),
    FUR("Fur", "LUXURY", 0, 0, 2),
    GEM("Gem", "LUXURY", 0, 0, 3),
    GOLD("Gold", "LUXURY", 0, 0, 2),
    INCENSE("Incense", "LUXURY", 0, 0, 2),
    IVORY("Ivory", "LUXURY", 0, 0, 2),
    MARBLE("Marble", "LUXURY", 0, 0, 2),
    SILK("Silk", "LUXURY", 0, 0, 2),
    SILVER("Silver", "LUXURY", 0, 0, 2),
    SUGAR("Sugar", "LUXURY", 0, 0, 2);

    private String name;
    private String type;
    private int food;
    private int production;
    private int gold;
    private int eraNumber;
    // TODO: Add required improvement, technology

    ResourceTemplate(String name, String type, int food, int production, int gold) {
        this.name = name;
        this.type = type;
        this.food = food;
        this.production = production;
        this.gold = gold;
    }
}
