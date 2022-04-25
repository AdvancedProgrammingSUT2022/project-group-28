package models.tiles.enums;

import models.tiles.TerrainOrTerrainFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public enum ResourceTemplate {
    BANANA("Banana","Banana ", ResourceType.BONUS, 1, 0, 0, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{TerrainFeature.FOREST}),
    COW("Cow","  Cow  ", ResourceType.BONUS, 1, 0, 0, ImprovementTemplate.PASTURE,
            new TerrainOrTerrainFeature[]{Terrain.GRASSLAND}),
    GAZELLE("Gazelle","Gazelle", ResourceType.BONUS, 1, 0, 0, ImprovementTemplate.CAMP,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.HILL, TerrainFeature.FOREST}),
    SHEEP("Sheep"," Sheep ", ResourceType.BONUS, 2, 0, 0, ImprovementTemplate.PASTURE,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.DESERT, Terrain.HILL, Terrain.GRASSLAND}),
    WHEAT("Wheat"," Wheat ", ResourceType.BONUS, 1, 0, 0, ImprovementTemplate.FARM,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, TerrainFeature.FOOD_PLAIN}),
    COAL("Coal","  Coal ", ResourceType.STRATEGIC, 0, 1, 0, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.HILL, Terrain.GRASSLAND}),
    HORSE("Horse"," Horse ", ResourceType.STRATEGIC, 0, 1, 0, ImprovementTemplate.PASTURE,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.PLAIN, Terrain.GRASSLAND}),
    IRON("Iron","  Iron ", ResourceType.STRATEGIC, 0, 1, 0, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.PLAIN, Terrain.DESERT, Terrain.HILL,
                                          Terrain.GRASSLAND, Terrain.SNOW}),
    COTTON("Cotton"," Cotton", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND}),
    DYE("Dye","  Dye  ", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{TerrainFeature.FOREST, TerrainFeature.JUNGLE}),
    FUR("Fur","  Fur  ", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.CAMP,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, TerrainFeature.FOREST}),
    GEM("Gem","  Gem  ", ResourceType.LUXURY, 0, 0, 3, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{TerrainFeature.JUNGLE, Terrain.TUNDRA, Terrain.PLAIN,
                                          Terrain.DESERT, Terrain.GRASSLAND, Terrain.HILL}),
    GOLD("Gold","  Gold ", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND, Terrain.HILL}),
    INCENSE("Incense","Incense", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.DESERT}),
    IVORY("Ivory"," Ivory ", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.CAMP,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN}),
    MARBLE("Marble"," Marble", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.QUARRY,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND, Terrain.HILL}),
    SILK("Silk","  Silk ", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{TerrainFeature.FOREST}),
    SILVER("Silver"," Silver", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.DESERT, Terrain.HILL}),
    SUGAR("Sugar"," Sugar ", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{TerrainFeature.FOOD_PLAIN, TerrainFeature.MARSH});

    private String name;
    private String mapSign;
    private ResourceType type;
    private int food;
    private int production;
    private int gold;
    private ImprovementTemplate requiredImprovement;
    private ArrayList<TerrainOrTerrainFeature> possiblePlaces;
    // TODO: Add required technology

    ResourceTemplate(String name,String mapSign, ResourceType type, int food, int production, int gold, ImprovementTemplate requiredImprovement,
                     TerrainOrTerrainFeature[] possiblePlaces) {
        this.name = name;
        this.mapSign = mapSign;
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

    public ArrayList<TerrainOrTerrainFeature> getPossiblePlaces() {
        return possiblePlaces;
    }

    public ResourceType getType() {
        return type;
    }
}
