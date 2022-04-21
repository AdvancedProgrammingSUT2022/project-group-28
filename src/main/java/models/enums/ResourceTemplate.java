package models.enums;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public enum ResourceTemplate {
    BANANA("Banana", "BONUS", 1, 0, 0, ImprovementTemplate.PLANTATION,
            new Object[]{TerrainFeature.FOREST}),
    COW("Cow", "BONUS", 1, 0, 0, ImprovementTemplate.PASTURE,
            new Object[]{Terrain.GRASSLAND}),
    GAZELLE("Gazelle", "BONUS", 1, 0, 0, ImprovementTemplate.CAMP,
            new Object[]{Terrain.TUNDRA, Terrain.HILL, TerrainFeature.FOREST}),
    SHEEP("Sheep", "BONUS", 2, 0, 0, ImprovementTemplate.PASTURE,
            new Object[]{Terrain.PLAIN, Terrain.DESERT, Terrain.HILL, Terrain.GRASSLAND}),
    WHEAT("Wheat", "BONUS", 1, 0, 0, ImprovementTemplate.FARM,
            new Object[]{Terrain.PLAIN, TerrainFeature.FOOD_PLAIN}),
    COAL("Coal", "STRATEGIC", 0, 1, 0, ImprovementTemplate.MINE,
            new Object[]{Terrain.PLAIN, Terrain.HILL, Terrain.GRASSLAND}),
    HORSE("Horse", "STRATEGIC", 0, 1, 0, ImprovementTemplate.PASTURE,
            new Object[]{Terrain.TUNDRA, Terrain.PLAIN, Terrain.GRASSLAND}),
    IRON("Iron", "STRATEGIC", 0, 1, 0, ImprovementTemplate.MINE,
            new Object[]{Terrain.TUNDRA, Terrain.PLAIN, Terrain.DESERT, Terrain.HILL, Terrain.GRASSLAND, Terrain.SNOW}),
    COTTON("Cotton", "LUXURY", 0, 0, 2, ImprovementTemplate.PLANTATION,
            new Object[]{Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND}),
    DYE("Dye", "LUXURY", 0, 0, 2, ImprovementTemplate.PLANTATION,
            new Object[]{TerrainFeature.FOREST, TerrainFeature.JUNGLE}),
    FUR("Fur", "LUXURY", 0, 0, 2, ImprovementTemplate.CAMP,
            new Object[]{Terrain.TUNDRA, TerrainFeature.FOREST}),
    GEM("Gem", "LUXURY", 0, 0, 3, ImprovementTemplate.MINE,
            new Object[]{TerrainFeature.JUNGLE, Terrain.TUNDRA, Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND, Terrain.HILL}),
    GOLD("Gold", "LUXURY", 0, 0, 2, ImprovementTemplate.MINE,
            new Object[]{Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND, Terrain.HILL}),
    INCENSE("Incense", "LUXURY", 0, 0, 2, ImprovementTemplate.PLANTATION,
            new Object[]{Terrain.PLAIN, Terrain.DESERT}),
    IVORY("Ivory", "LUXURY", 0, 0, 2, ImprovementTemplate.CAMP,
            new Object[]{Terrain.PLAIN}),
    MARBLE("Marble", "LUXURY", 0, 0, 2, ImprovementTemplate.QUARRY,
            new Object[]{Terrain.TUNDRA, Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND, Terrain.HILL}),
    SILK("Silk", "LUXURY", 0, 0, 2, ImprovementTemplate.PLANTATION,
            new Object[]{TerrainFeature.FOREST}),
    SILVER("Silver", "LUXURY", 0, 0, 2, ImprovementTemplate.MINE,
            new Object[]{Terrain.TUNDRA, Terrain.DESERT, Terrain.HILL}),
    SUGAR("Sugar", "LUXURY", 0, 0, 2, ImprovementTemplate.PLANTATION,
            new Object[]{TerrainFeature.FOOD_PLAIN, TerrainFeature.MARSH});

    private String name;
    private String type;
    private int food;
    private int production;
    private int gold;
    private int eraNumber;
    private ImprovementTemplate requiredImprovement;
    private ArrayList<Object> possiblePlaces;
    // TODO: Add required technology

    ResourceTemplate(String name, String type, int food, int production, int gold, ImprovementTemplate requiredImprovement,
                     Object[] possiblePlaces) {
        this.name = name;
        this.type = type;
        this.food = food;
        this.production = production;
        this.gold = gold;
        this.requiredImprovement = requiredImprovement;
        this.possiblePlaces = new ArrayList<>(Arrays.asList(possiblePlaces));
    }

    public static ResourceTemplate generateRandomResourceTemplate(Random random) {
        ResourceTemplate[] values = ResourceTemplate.values();
        int length = values.length;
        int randomIndex = random.nextInt(length);
        return values[randomIndex];
    }
}
