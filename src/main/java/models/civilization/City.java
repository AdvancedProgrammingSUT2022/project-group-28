package models.civilization;

import controllers.GameController;
import models.Game;
import models.civilization.enums.BuildingTemplate;
import models.tiles.Tile;
import models.tiles.enums.Direction;

import java.util.ArrayList;

public class City{
    private final String NAME;
    private Civilization civilization;
    private final Civilization FOUNDER;
    private final Tile tile;
    private ArrayList<Tile> tiles;

    private int citizens = 0;
    private int population = 1;

    // TODO: add all initial values
    private int strength;
    private int hitPoint;

    private int foodBalance;
    private int growthBucket = 0;
    private int foodStore = 0;

    private int productionBalance;
    private int productionStore;

    private Construction construction;

    private ArrayList<BuildingTemplate> buildings = new ArrayList<>();

    public City(String name, Civilization civilization, Tile tile) {
        this.NAME = name;
        this.civilization = civilization;
        this.FOUNDER = civilization;
        this.tile = tile;
        this.tiles = getInitialTiles(tile);
        this.hitPoint = 20;
    }

    public Civilization getFOUNDER() {
        return FOUNDER;
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

    public int getCitizens() { 
        return citizens; 
    }

    public int getPopulation() { 
        return population; 
    }

    public int getGrowthBucket() { 
        return growthBucket; 
    }

    public int getFoodStore() { return foodStore; }

    public int getStrength() {
        return strength;
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public void setHitPoint(int hitPoint) {
        this.hitPoint = hitPoint;
    }

    public int getFoodBalance() {
        return foodBalance;
    }

    public int getProductionBalance() {
        return productionBalance;
    }

    public int getProductionStore() { return productionStore; }

    public Construction getConstruction() { return construction; }

    public ArrayList<BuildingTemplate> getBuildings() {
        return buildings;
    }

    public void setGrowthBucket(int growthBucket) { 
        this.growthBucket = growthBucket; 
    }

    public void setFoodBalance(int foodBalance) { 
        this.foodBalance = foodBalance; 
    }

    public void setFoodStore(int foodStore) { this.foodStore = foodStore; }

    public void setProductionBalance(int productionBalance) { 
        this.productionBalance = productionBalance; 
    }

    public void setProductionStore(int productionStore) { this.productionStore = productionStore; }

    public void setConstruction(Construction construction) { 
        this.construction = construction; 
    }

    public void increasePopulation(int value) { 
        population += value; 
    }

    public void decreasePopulation(int value) {
        population -= value;
    }

    public void increaseCitizens(int value) {
        citizens += value; 
    }

    public void decreaseCitizens(int value) {
        citizens -= value; 
    }

    public void addTile(Tile tile) { 
        tiles.add(tile); 
    }

    public void destroy(){
        // TODO: complete method
        this.civilization.removeCity(this);
        tile.setCity(null);
        for(Tile t:this.getTiles()){
            t.setCivilization(null);  
            this.civilization.updateDiscoveredTiles(tile, GameController.getGame().getTurnNumber());    
        }
        this.civilization.updateDiscoveredTiles(this.tile, GameController.getGame().getTurnNumber());
    }

    public int getCombatStrength() {
        // TODO: complete method
        return 5;
    }

    public void setCivilization(Civilization civilization) {
        this.civilization = civilization;
    }
}
