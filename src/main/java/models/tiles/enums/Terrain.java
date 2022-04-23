package models.tiles.enums;

import models.tiles.TerrainOrTerrainFeature;

import java.util.Random;

public enum Terrain implements TerrainOrTerrainFeature {
    DESERT("Desert", 0, 0, 0, 1, -1/3, true),
    GRASSLAND("Grassland", 2, 0, 0, 1, -1/3, true),
    HILL("Hill", 0, 2, 0, 2, 1/4, true),
    MOUNTAIN("Mountain", 0, 0, 0, 0, 0, false),
    OCEAN("Ocean", 0, 0, 0, 0, 0, false),
    PLAIN("Plain", 1, 1, 0, 1, -1/3, true),
    SNOW("Snow", 0, 0, 0, 1, -1/3, true),
    TUNDRA("Tundra", 1, 0, 0, 1, -1/3, true);
    private String name;
    private int food;
    private int production;
    private int gold;
    private int movementCost;
    private float combatModifiers;
    private boolean accessible;

    Terrain(String name, int food, int production, int gold, int movementCost, float combatModifiers, boolean accessible) {
        this.name = name;
        this.food = food;
        this.production = production;
        this.gold = gold;
        this.movementCost = movementCost;
        this.combatModifiers = combatModifiers;
        this.accessible = accessible;
    }

    public static Terrain generateRandomTerrain(Random random) {
        Terrain[] values = Terrain.values();
        int length = values.length;
        int randIndex = random.nextInt(length);
        return  values[randIndex];
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

    public float getCombatModifiers() {
        return combatModifiers;
    }

    public boolean isAccessible() {
        return accessible;
    }
}
