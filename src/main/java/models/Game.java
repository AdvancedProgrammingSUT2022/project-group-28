package models;

import models.enums.*;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Object[][] map;
    private ArrayList<Civilization> civilizations;

    // TODO: Change map length to const
    Game(ArrayList<User> users, int seed) {
        Random random = new Random(seed);
        this.map = new Object[100][100];

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                if ((i%2 + j%2) == 1) {
                    if (random.nextInt(20) == 0) {
                        this.map[i][j] = "R";
                    }
                    else this.map[i][j] = null;
                }
            }
        }

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                if ((i%2 + j%2) == 0) {
                    Terrain terrain = Terrain.generateRandomTerrain(random);
                    TerrainFeature terrainFeature = getPossibleTerrainFeature(terrain, i, j, random);
                    Resource resource = getPossibleResource(terrain, terrainFeature, random);
                    this.map[i][j] = new Tile(terrain, terrainFeature, resource);
                }
            }
        }
        this.civilizations = new ArrayList<>();
        for (User user : users) {
            this.civilizations.add(new Civilization(user, "CIVILIZATION"));
        }
    }

    Game(ArrayList<User> users, String filePath) { }

    private TerrainFeature getPossibleTerrainFeature(Terrain terrain, int i, int j, Random random) {
        TerrainFeature terrainFeature = TerrainFeature.generateRandomTerrainFeature(random);
        if (terrainFeature == TerrainFeature.FOOD_PLAIN && !hasAdjacentRiver(i, j)) return null;
        else if (terrainFeature == TerrainFeature.OASIS && terrain != Terrain.DESERT) return null;
        else if (terrain == Terrain.OCEAN) return null;
        return terrainFeature;
    }

    private Resource getPossibleResource(Terrain terrain, TerrainFeature terrainFeature, Random random) {
        ResourceTemplate resourceTemplate = ResourceTemplate.generateRandomResourceTemplate(random);
        if (!ResourceTemplate.getPossiblePlaces(resourceTemplate).contains(terrain) &&
            !ResourceTemplate.getPossiblePlaces(resourceTemplate).contains(terrainFeature)) return null;
        else {
            int count;
            if (ResourceTemplate.getType(resourceTemplate).equals("Strategic")) {
                count = (random.nextInt(3) + 1) * 2;
            } else count = 1;
            return new Resource(resourceTemplate, count);
        }
    }

    private boolean hasAdjacentRiver(int i, int j) {
        if (i - 1 >= 0 && this.map[i - 1][j].equals("R")) return true;
        if (i + 1 < 100 && this.map[i + 1][j].equals("R")) return true;
        if (j - 1 >= 0 && this.map[i][j - 1].equals("R")) return true;
        if (j + 1 < 100 && this.map[i][j + 1].equals("R")) return true;
        return false;
    }
}
