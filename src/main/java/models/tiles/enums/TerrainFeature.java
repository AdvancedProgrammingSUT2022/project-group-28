package models.tiles.enums;

import models.tiles.TerrainOrTerrainFeature;

import java.util.Random;

public enum TerrainFeature implements TerrainOrTerrainFeature {
    FOOD_PLAIN("Food plain","FPlain ", 2, 0, 0, 1, -1/3, true),
    FOREST("Forest","Forest ", 1, 1, 0, 2, 1/4, true),
    ICE("Ice","  Ice  ", 0, 0, 0, 0, 0, false),
    JUNGLE("Jungle","Jungle ", 1, -1, 0, 2, 1/4, true),
    MARSH("Marsh"," Marsh ", -1, 0, 0, 2, -1/3, true),
    OASIS("Oasis"," Oasis ", 3, 0, 1, 1, -1/3, true);

    private String name;
    private String mapSign;
    private int food;
    private int production;
    private int gold;
    private int movementCost;
    private float combatModifier;
    private boolean accessible;

    TerrainFeature(String name,String mapSign, int food, int production, int gold, int movementCost, float combatModifier, boolean accessible) {
        this.name = name;
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
        int randomIndex = random.nextInt(length);
        return values[randomIndex];
    }

    public String getName() {
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
