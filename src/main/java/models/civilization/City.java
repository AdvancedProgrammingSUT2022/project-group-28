package models.civilization;

import controllers.GameController;
import models.Game;
import models.tiles.Tile;
import models.tiles.enums.Direction;

import java.util.ArrayList;

public class City {
    private final String NAME;
    private Civilization civilization;
    private Tile tile;
    private ArrayList<Tile> tiles;

    private int citizens = 1;
    private int population = 1;
    // TODO: add all initial values
    private int strength;
    private int hitPoint;

    private int foodBalance;
    private int productionBalance;

    private ArrayList<Building> buildings = new ArrayList<>();

    public City(String name, Civilization civilization, Tile tile) {
        this.NAME = name;
        this.civilization = civilization;
        this.tile = tile;
        this.tiles = getInitialTiles(tile);
    }

    private ArrayList<Tile> getInitialTiles(Tile tile) {
        Game game = GameController.getGame();
        int[] coordinates = tile.getCoordinates();
        int i = coordinates[0];
        int j = coordinates[1];
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (i + direction.i < game.MAP_HEIGHT && i + direction.i >= 0 &&
                j + direction.j < game.MAP_WIDTH && j + direction.j >= 0) {
                tiles.add(game.getMap()[i + direction.i][j + direction.j]);
            }
        }
        return tiles;
    }
    public String getNAME() {
        return NAME;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public Tile getTile() {
        return tile;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public int getCitizens() { return citizens; }

    public int getPopulation() {
        return population;
    }

    public int getStrength() {
        return strength;
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public int getFoodBalance() {
        return foodBalance;
    }

    public int getProductionBalance() {
        return productionBalance;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public void increaseCitizens(int value) { citizens += value; }

    public void decreaseCitizens(int value) { citizens -= value; }
}
