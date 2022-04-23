package models.tiles.enums;

import models.tiles.TerrainOrTerrainFeature;

import java.util.Random;

public enum TerrainFeature implements TerrainOrTerrainFeature {
    FOOD_PLAIN("Food plain", 2, 0, 0, 1, -1/3, true),
    FOREST("Forest", 1, 1, 0, 2, 1/4, true),
    ICE("Ice", 0, 0, 0, 0, 0, false),
    JUNGLE("Jungle", 1, -1, 0, 2, 1/4, true),
    MARSH("Marsh", -1, 0, 0, 2, -1/3, true),
    OASIS("Oasis", 3, 0, 1, 1, -1/3, true);

    private String name;
    private int food;
    private int production;
    private int gold;
    private int movementCost;
    private float combatModifier;
    private boolean accessible;

    TerrainFeature(String name, int food, int production, int gold, int movementCost, float combatModifier, boolean accessible) {
        this.name = name;
        this.food = food;
        this.production = production;
        this.gold = gold;
        this.movementCost = movementCost;
        this.combatModifier = combatModifier;
        this.accessible = accessible;

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
