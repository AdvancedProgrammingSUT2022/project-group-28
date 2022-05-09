package models.civilization;

import controllers.GameController;
import models.Combatable;
import models.Game;
import models.civilization.enums.BuildingTemplate;
import models.tiles.Tile;
import models.tiles.enums.Direction;

import java.util.ArrayList;

public class City implements Combatable {
    private final String NAME;
    private Civilization civilization;
    private final Tile tile;
    private ArrayList<Tile> tiles;

    private int citizens = 0;
    private int population = 1;
    private int growthBucket = 0;

    // TODO: add all initial values
    private int strength;
    private int hitPoint;

    private int foodBalance;
    private int productionBalance;

    private Construction construction;

    private ArrayList<BuildingTemplate> buildings = new ArrayList<>();

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

    public int getPopulation() { return population; }

    public int getGrowthBucket() { return growthBucket; }

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

    public Construction getConstruction() { return construction; }

    public ArrayList<BuildingTemplate> getBuildings() {
        return buildings;
    }

    public void setGrowthBucket(int growthBucket) { this.growthBucket = growthBucket; }

    public void setFoodBalance(int foodBalance) { this.foodBalance = foodBalance; }

    public void setProductionBalance(int productionBalance) { this.productionBalance = productionBalance; }

    public void setConstruction(Construction construction) { this.construction = construction; }

    public void increasePopulation(int value) { population += value; }

    public void decreasePopulation(int value) { population -= value;}

    public void increaseCitizens(int value) { citizens += value; }

    public void decreaseCitizens(int value) { citizens -= value; }

    public void addTile(Tile tile) { 
        tiles.add(tile); 
    }



}
