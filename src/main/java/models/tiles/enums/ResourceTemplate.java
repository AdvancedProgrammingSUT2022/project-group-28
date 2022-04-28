package models.tiles.enums;

import models.civilization.enums.TechnologyTemplate;
import models.tiles.TerrainOrTerrainFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public enum ResourceTemplate {
    BANANA("Banana","banana#", ResourceType.BONUS, 1, 0, 0, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{TerrainFeature.FOREST}, null),
    COW("Cow","##cow##", ResourceType.BONUS, 1, 0, 0, ImprovementTemplate.PASTURE,
            new TerrainOrTerrainFeature[]{Terrain.GRASSLAND}, null),
    GAZELLE("Gazelle","gazelle", ResourceType.BONUS, 1, 0, 0, ImprovementTemplate.CAMP,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.HILL, TerrainFeature.FOREST}, null),
    SHEEP("Sheep","#sheep#", ResourceType.BONUS, 2, 0, 0, ImprovementTemplate.PASTURE,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.DESERT, Terrain.HILL, Terrain.GRASSLAND}, null),
    WHEAT("Wheat","#wheat#", ResourceType.BONUS, 1, 0, 0, ImprovementTemplate.FARM,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, TerrainFeature.FOOD_PLAIN}, null),
    COAL("Coal","##coal#", ResourceType.STRATEGIC, 0, 1, 0, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.HILL, Terrain.GRASSLAND},TechnologyTemplate.SCIENTIFIC_THEORY),
    HORSE("Horse","#horse#", ResourceType.STRATEGIC, 0, 1, 0, ImprovementTemplate.PASTURE,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.PLAIN, Terrain.GRASSLAND}, TechnologyTemplate.ANIMAL_HUSBANDRY),
    IRON("Iron","##iron#", ResourceType.STRATEGIC, 0, 1, 0, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.PLAIN, Terrain.DESERT, Terrain.HILL,
                                          Terrain.GRASSLAND, Terrain.SNOW},TechnologyTemplate.IRON_WORKING),
    COTTON("Cotton","#cotton", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND}, null),
    DYE("Dye","##dye##", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{TerrainFeature.FOREST, TerrainFeature.JUNGLE}, null),
    FUR("Fur","##fur##", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.CAMP,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, TerrainFeature.FOREST}, null),
    GEM("Gem","##gem##", ResourceType.LUXURY, 0, 0, 3, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{TerrainFeature.JUNGLE, Terrain.TUNDRA, Terrain.PLAIN,
                                          Terrain.DESERT, Terrain.GRASSLAND, Terrain.HILL}, null),
    GOLD("Gold","##gold#", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND, Terrain.HILL}, null),
    INCENSE("Incense","incense", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN, Terrain.DESERT}, null),
    IVORY("Ivory","#ivory#", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.CAMP,
            new TerrainOrTerrainFeature[]{Terrain.PLAIN}, null),
    MARBLE("Marble","#marble", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.QUARRY,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.PLAIN, Terrain.DESERT, Terrain.GRASSLAND, Terrain.HILL}, null),
    SILK("Silk","##silk#", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{TerrainFeature.FOREST}, null),
    SILVER("Silver","#silver", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.MINE,
            new TerrainOrTerrainFeature[]{Terrain.TUNDRA, Terrain.DESERT, Terrain.HILL}, null),
    SUGAR("Sugar","#sugar#", ResourceType.LUXURY, 0, 0, 2, ImprovementTemplate.PLANTATION,
            new TerrainOrTerrainFeature[]{TerrainFeature.FOOD_PLAIN, TerrainFeature.MARSH}, null);

    private String name;
    private String mapSign;
    private ResourceType type;
    private int food;
    private int production;
    private int gold;
    private ImprovementTemplate requiredImprovement;
    private ArrayList<TerrainOrTerrainFeature> possiblePlaces;
    private TechnologyTemplate requiredTechnology;

    ResourceTemplate(String name,String mapSign, ResourceType type, int food, int production, int gold, ImprovementTemplate requiredImprovement,
                     TerrainOrTerrainFeature[] possiblePlaces, TechnologyTemplate requiredTechnology) {
        this.name = name;
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
