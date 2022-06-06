package models.tiles.enums;

import models.tiles.TerrainOrTerrainFeature;
import views.enums.Color;

import java.util.Random;

public enum Terrain implements TerrainOrTerrainFeature {
    DESERT("Desert", "desert", 0, 0, 0, 1, -1/3f, true, Color.YELLOW_BACKGROUND),
    GRASSLAND("Grassland", "grassland",2, 0, 0, 1, -1/3f, true, Color.GREEN_BACKGROUND_BRIGHT),
    HILL("Hill", "hill", 0, 2, 0, 2, 1/4f, true, Color.MAGENTA_BACKGROUND),
    MOUNTAIN("Mountain", "mountain", 0, 0, 0, 0, 0, false, Color.BLACK_BACKGROUND_BRIGHT),
    OCEAN("Ocean", "ocean", 0, 0, 0, 0, 0, false, Color.BLUE_BACKGROUND),
    PLAIN("Plain", "plain", 1, 1, 0, 1, -1/3f, true, Color.GREEN_BACKGROUND),
    SNOW("Snow", "snow",0, 0, 0, 1, -1/3f, true, Color.WHITE_BACKGROUND),
    TUNDRA("Tundra", "tundra", 1, 0, 0, 1, -1/3f, true, Color.CYAN_BACKGROUND);
    
    private String name;
    private String filename;
    private int food;
    private int production;
    private int gold;
    private int movementCost;
    private float combatModifiers;
    private boolean accessible;
    private Color color;

    Terrain(String name, String filename, int food, int production, int gold, int movementCost, float combatModifiers, boolean accessible, Color color) {
        this.name = name;
        this.filename = filename;
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
