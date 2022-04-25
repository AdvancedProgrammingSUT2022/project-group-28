package models;

import models.civilization.Civilization;
import models.tiles.Resource;
import models.tiles.Tile;
import models.tiles.enums.*;
import models.units.Settler;
import models.units.Unit;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    public final int MAP_WIDTH = 100;
    public final int MAP_HEIGHT = 100;

    private Tile[][] map;
    private ArrayList<Civilization> civilizations;

    private Civilization currentPlayer;
    private int turnNumber;

    private Unit selectedUnit = null;

    public Game(ArrayList<User> users, int seed) {
        Random random = new Random(seed);;

        this.map = generateRandomMap(random);

        this.civilizations = new ArrayList<>();
        int i = 2;
        for (User user : users) {
            Civilization civilization = new Civilization(user, "CIVILIZATION");
            Settler settler = new Settler(civilization, this.map[10 + 10 * i][10 + 10 * i]);
            civilization.addUnit(settler);
            this.civilizations.add(civilization);
            i--;
        }

        this.currentPlayer = this.civilizations.get(0);
        this.turnNumber = 1;
    }


    private Tile[][] generateRandomMap(Random random) {
        Tile[][] map = new Tile[MAP_HEIGHT][MAP_WIDTH];
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = MAP_WIDTH - 1; j >= 0; j--) {
                ArrayList<Direction> rivers = getRandomRivers(random);
                Terrain terrain = Terrain.generateRandomTerrain(random);
                TerrainFeature terrainFeature = getPossibleTerrainFeature(random, terrain, rivers);
                Resource resource = getPossibleResource(random, terrain, terrainFeature);
                map[i][j] = new Tile(i, j, terrain, terrainFeature, resource, rivers);
                addAdjacentRivers(i, j, map, rivers);
            }
        }
        return map;
    }

    private ArrayList<Direction> getRandomRivers(Random random) {
        ArrayList<Direction> result = new ArrayList<>();
        if (random.nextInt(50) == 0) result.add(Direction.UP);
        if (random.nextInt(50) == 0) result.add(Direction.UP_RIGHT);
        if (random.nextInt(50) == 0) result.add(Direction.RIGHT);
        return result;
    }

    private TerrainFeature getPossibleTerrainFeature(Random random, Terrain terrain, ArrayList<Direction> rivers) {
        TerrainFeature terrainFeature = TerrainFeature.generateRandomTerrainFeature(random);
        if (terrainFeature == TerrainFeature.FOOD_PLAIN && rivers.size() == 0) return null;
        else if (terrainFeature == TerrainFeature.OASIS && terrain != Terrain.DESERT) return null;
        else if (terrain == Terrain.OCEAN) return null;
        return terrainFeature;
    }

    private Resource getPossibleResource(Random random, Terrain terrain, TerrainFeature terrainFeature) {
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

    private void addAdjacentRivers(int i , int j, Tile[][] map, ArrayList<Direction> rivers) {
        if (rivers.contains(Direction.UP) && i - 1 >= 0)
            map[i - 1][j].getRivers().add(Direction.DOWN);
        if (rivers.contains(Direction.RIGHT) && j + 1 < MAP_WIDTH)
            map[i][j + 1].getRivers().add(Direction.LEFT);
        if (rivers.contains(Direction.UP_RIGHT) && i - 1 >= 0 && j + 1 < MAP_WIDTH)
            map[i - 1][j + 1].getRivers().add(Direction.DOWN_LEFT);
    }



    public Tile[][] getMap() { return map; }

    public ArrayList<Civilization> getCivilizations() { return civilizations; }

    public Civilization getCurrentPlayer() { return currentPlayer; }

    public int getTurnNumber() { return turnNumber; }

    public void setCurrentPlayer(Civilization currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }
}
