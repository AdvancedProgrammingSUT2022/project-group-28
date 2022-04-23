package models;

import models.tiles.Resource;
import models.tiles.Tile;
import models.tiles.enums.ResourceTemplate;
import models.tiles.enums.ResourceType;
import models.tiles.enums.Terrain;
import models.tiles.enums.TerrainFeature;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private final int MAP_WIDTH = 100;
    private final int MAP_HEIGHT = 100;
    private final int ARRAY_WIDTH;
    private final int ARRAY_HEIGHT;
    private TileOrRiver[][] map;
    private ArrayList<Civilization> civilizations;

    public Game(ArrayList<User> users, int seed) {
        Random random = new Random(seed);

        this.ARRAY_WIDTH = this.MAP_WIDTH + this.MAP_WIDTH/2;
        this.ARRAY_HEIGHT = this.MAP_HEIGHT * 2;

        this.map = generateRandomMap(random);

        this.civilizations = new ArrayList<>();
        for (User user : users) {
            this.civilizations.add(new Civilization(user, "CIVILIZATION"));
        }
    }


    private TileOrRiver[][] generateRandomMap(Random random) {
        TileOrRiver[][] map = new TileOrRiver[this.ARRAY_HEIGHT][this.ARRAY_WIDTH];

        for (int i = 0; i < this.ARRAY_HEIGHT; i++) {
            for (int j = 0; j  < this.ARRAY_WIDTH; j++) {
                if ((i%2 == 1) || (j%2 == 1)) map[i][j] = new River();
                else map[i][j] = null;
            }
        }

        for (int i = 0; i < this.ARRAY_HEIGHT; i++) {
            for (int j = 0; j< this.ARRAY_WIDTH; j++) {
                if ((i%2 == 0) && (j%2 == 0)) {
                    Terrain terrain = Terrain.generateRandomTerrain(random);
                    TerrainFeature terrainFeature = getPossibleTerrainFeature(terrain, i, j, map, random);
                    Resource resource = getPossibleResource(terrain, terrainFeature, random);
                    map[i][j] = new Tile(terrain, terrainFeature, resource);
                }
            }
        }

        return map;
    }

    private TerrainFeature getPossibleTerrainFeature(Terrain terrain, int i, int j, TileOrRiver map[][], Random random) {
        TerrainFeature terrainFeature = TerrainFeature.generateRandomTerrainFeature(random);
        if (terrainFeature == TerrainFeature.FOOD_PLAIN && !hasAdjacentRiver(map, i, j)) return null;
        else if (terrainFeature == TerrainFeature.OASIS && terrain != Terrain.DESERT) return null;
        else if (terrain == Terrain.OCEAN) return null;
        return terrainFeature;
    }

    private Resource getPossibleResource(Terrain terrain, TerrainFeature terrainFeature, Random random) {
        ResourceTemplate resourceTemplate = ResourceTemplate.generateRandomResourceTemplate(random);
        if (!resourceTemplate.getPossiblePlaces().contains(terrain) &&
            !resourceTemplate.getPossiblePlaces().contains(terrainFeature)) return null;
        else {
            int count;
            if (resourceTemplate.getType().equals(ResourceType.STRATEGIC)) {
                count = (random.nextInt(3) + 1) * 2;
            } else count = 1;
            return new Resource(resourceTemplate, count);
        }
    }

    private boolean hasAdjacentRiver(TileOrRiver map[][], int i, int j) {
        if (i - 1 >= 0 && map[i - 1][j] instanceof River) return true;
        if (i + 1 < 100 && map[i + 1][j] instanceof River) return true;
        if (j - 1 >= 0 && map[i][j - 1] instanceof River) return true;
        if (j + 1 < 100 && map[i][j + 1] instanceof River) return true;
        if (j + 1 < 100 && i - 1 >= 0 && map[i - 1][j + 1] instanceof River) return true;
        if (j - 1 >= 0 && i + 1 < 100 && map[i + 1][j - 1] instanceof River) return true;
        return false;
    }
}
