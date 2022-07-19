package models.tiles.enums;

import models.civilization.enums.TechnologyTemplate;
import models.tiles.TerrainOrTerrainFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public enum ResourceTemplate {
    BANANA("Banana", "banana", "BAN", ResourceType.BONUS, 1, 0, 0, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{TerrainFeature.FOREST}, null),
    COW("Cow","cow", "COW", ResourceType.BONUS, 1, 0, 0, ImprovementTemplate.PASTURE,
            new TerrainOrTerrainFeature[]{Terrain.GRASSLAND}, null),
    GAZELLE("Gazelle","gazelle", "GAZ", ResourceType.BONUS, 1, 0, 0, ImprovementTemplate.CAMP,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.HILL, TerrainFeature.FOREST}, null),
    SHEEP("Sheep", "sheep", "SHP", ResourceType.BONUS, 2, 0, 0, ImprovementTemplate.PASTURE,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.DESERT, Terrain.HILL, Terrain.GRASSLAND}, null),
    WHEAT("Wheat","wheat", "WHT", ResourceType.BONUS, 1, 0, 0, ImprovementTemplate.FARM,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, TerrainFeature.FOOD_PLAIN}, null),
    COAL("Coal","coal", "COL", ResourceType.STRATEGIC, 0, 1, 0, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.HILL, Terrain.GRASSLAND},TechnologyTemplate.SCIENTIFIC_THEORY),
    HORSE("Horse","horse", "HRS", ResourceType.STRATEGIC, 0, 1, 0, ImprovementTemplate.PASTURE,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.PLAIN, Terrain.GRASSLAND}, TechnologyTemplate.ANIMAL_HUSBANDRY),
    IRON("Iron","iron", "IRN", ResourceType.STRATEGIC, 0, 1, 0, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.PLAIN, Terrain.DESERT, Terrain.HILL,
                                          Terrain.GRASSLAND, Terrain.SNOW},TechnologyTemplate.IRON_WORKING),
    COTTON("Cotton","cotton", "CTN", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND}, null),
    DYE("Dye","dye", "DYE", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{TerrainFeature.FOREST, TerrainFeature.JUNGLE}, null),
    FUR("Fur","fur", "FUR", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.CAMP,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, TerrainFeature.FOREST}, null),
    GEM("Gem","gem", "GEM", ResourceType.LUXURY, 0, 0, 3, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{TerrainFeature.JUNGLE, Terrain.TUNDRA, Terrain.PLAIN,
                                          Terrain.DESERT, Terrain.GRASSLAND, Terrain.HILL}, null),
    GOLD("Gold","gold", "GLD", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND, Terrain.HILL}, null),
    INCENSE("Incense", "incense", "INC", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.DESERT}, null),
    IVORY("Ivory","ivory", "IVR", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.CAMP,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN}, null),
    MARBLE("Marble","marble", "MRB", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.QUARRY,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND, Terrain.HILL}, null),
    SILK("Silk","silk", "SLK", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{TerrainFeature.FOREST}, null),
    SILVER("Silver","silver", "SLR", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.DESERT, Terrain.HILL}, null),
    SUGAR("Sugar","sugar", "SGR", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{TerrainFeature.FOOD_PLAIN, TerrainFeature.MARSH}, null);

    private final String name;
    private final String filename;
    private final String mapSign;
    private final ResourceType type;
    private final int food;
    private final int production;
    private final int gold;
    private final ImprovementTemplate requiredImprovement;
    private final ArrayList<TerrainOrTerrainFeature> possiblePlaces;
    private final TechnologyTemplate requiredTechnology;

    ResourceTemplate(String name, String filename, String mapSign, ResourceType type, int food, int production, int gold, ImprovementTemplate requiredImprovement,
                     TerrainOrTerrainFeature[] possiblePlaces, TechnologyTemplate requiredTechnology) {
        this.name = name;
        this.filename = filename;
        this.mapSign = mapSign;
        this.type = type;
        this.food = food;
        this.production = production;
        this.gold = gold;
        this.requiredImprovement = requiredImprovement;
        this.possiblePlaces = new ArrayList<>(Arrays.asList(possiblePlaces));
        this.requiredTechnology = requiredTechnology;
    }

    public String getMapSign() {
        return mapSign;
    }

    public static ResourceTemplate generateRandomResourceTemplate(Random random) {
        ResourceTemplate[] values = ResourceTemplate.values();
        int length = values.length;
        int randomIndex = random.nextInt(length);
        return values[randomIndex];
    }

    public ArrayList<TerrainOrTerrainFeature> getPossiblePlaces() {
        return possiblePlaces;
    }

    public ResourceType getType() {
        return type;
    }

    public String getName(){
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public int getFood() {
        return food;
    }

    public int getProduction() {
        return production;
    }

    public int getGold() {
        return gold;
    }

    public ImprovementTemplate getRequiredImprovement() {
        return requiredImprovement;
    }

    public TechnologyTemplate getRequiredTechnology() {
        return requiredTechnology;
    }
}
