package models.tiles.enums;

import models.tiles.TerrainOrTerrainFeature;

import java.util.Random;

public enum TerrainFeature implements TerrainOrTerrainFeature {
    FOOD_PLAIN("Food plain", "food_plain", "FP#", 2, 0, 0, 1, -1/3f, true),
    FOREST("Forest","forest", "FOR", 1, 1, 0, 2, 1/4f, true),
    ICE("Ice","ice","ICE", 0, 0, 0, 0, 0, false),
    JUNGLE("Jungle","jungle", "JG#", 1, -1, 0, 2, 1/4f, true),
    MARSH("Marsh","marsh", "MAR", -1, 0, 0, 2, -1/3f, true),
    OASIS("Oasis","oasis", "OAS", 3, 0, 1, 1, -1/3f, true);

    private String name;
    private String filename;
    private String mapSign;
    private int food;
    private int production;
    private int gold;
    private int movementCost;
    private float combatModifier;
    private boolean accessible;

    TerrainFeature(String name, String filename, String mapSign, int food, int production, int gold, int movementCost, float combatModifier, boolean accessible) {
        this.name = name;
        this.filename = filename;
        this.mapSign = mapSign;
        this.food = food;
        this.production = production;
        this.gold = gold;
        this.movementCost = movementCost;
        this.combatModifier = combatModifier;
        this.accessible = accessible;

    }

    public String getMapSign() {
        return mapSign;
    }

    public static TerrainFeature generateRandomTerrainFeature(Random random) {
        TerrainFeature[] values = TerrainFeature.values();
        int length = values.length;
        int randomIndex = random.nextInt(length*2);
        if (randomIndex>=length)return null;
        return values[randomIndex];
    }

    public String getName() {
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

    public int getMovementCost() {
        return movementCost;
    }

    public float getCombatModifier() {
        return combatModifier;
    }

    public boolean isAccessible() {
        return accessible;
    }
}
