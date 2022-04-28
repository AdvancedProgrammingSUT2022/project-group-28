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

    private int population = 1;
    private int citizens = 1;
    // TODO: add all initial values
    private int strength;
    private int hitPoint;

    private int foodBalance;
    private int productionBalance;

    private ArrayList<Building> buildings = new ArrayList<>();

    public City(Civilization civilization, Tile tile) {
        this.NAME = getNewCityName(civilization);
        civilization.addCity(this);
        this.civilization = civilization;
        tile.setCity(this);
        this.tile = tile;
        this.tiles = getInitialTiles(tile);
    }

    private String getNewCityName(Civilization civilization) {
        Game game = GameController.getGame();
        ArrayList<String> allCitiesNames = new ArrayList<>();
        for (Civilization gameCivilization : game.getCivilizations()) {
            for (City city : gameCivilization.getCities()) {
                allCitiesNames.add(city.getNAME());
            }
        }
        if (!allCitiesNames.contains(civilization.getCivilizationNames().getCapital()))
            return civilization.getCivilizationNames().getCapital();
        for (String cityName : civilization.getCivilizationNames().getCities()) {
            if (!allCitiesNames.contains(cityName)) return cityName;
        }
        // TODO: handle this
        return "HAVIG ABAD";
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

    public int getPopulation() {
        return population;
    }

    public int getCitizens() {
        return citizens;
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
}
