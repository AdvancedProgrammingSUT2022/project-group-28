package models.tiles.enums;

import models.tiles.TerrainOrTerrainFeature;
import views.enums.Color;

import java.util.Random;

public enum Terrain implements TerrainOrTerrainFeature {
    DESERT("Desert", 0, 0, 0, 1, -1/3, true, Color.YELLOW_BACKGROUND),
    GRASSLAND("Grassland", 2, 0, 0, 1, -1/3, true, Color.GREEN_BACKGROUND),
    HILL("Hill", 0, 2, 0, 2, 1/4, true, Color.MAGENTA_BACKGROUND),
    MOUNTAIN("Mountain", 0, 0, 0, 0, 0, false, Color.BLACK_BACKGROUND),
    OCEAN("Ocean", 0, 0, 0, 0, 0, false, Color.BLUE_BACKGROUND),
    PLAIN("Plain", 1, 1, 0, 1, -1/3, true, Color.CYAN_BACKGROUND),
    SNOW("Snow", 0, 0, 0, 1, -1/3, true, Color.WHITE_BACKGROUND),
    TUNDRA("Tundra", 1, 0, 0, 1, -1/3, true, Color.GREEN_BACKGROUND);
    private String name;
    private int food;
    private int production;
    private int gold;
    private int movementCost;
    private float combatModifiers;
    private boolean accessible;
    private Color color;
    Terrain(String name, int food, int production, int gold, int movementCost, float combatModifiers, boolean accessible, Color color) {
        this.name = name;
        this.food = food;
        this.production = production;
        this.gold = gold;
        this.movementCost = movementCost;
        this.combatModifiers = combatModifiers;
        this.accessible = accessible;
        this.color = color;
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

    public Color getColor() {
        return color;
    }

    public boolean isAccessible() {
        return accessible;
    }


}
