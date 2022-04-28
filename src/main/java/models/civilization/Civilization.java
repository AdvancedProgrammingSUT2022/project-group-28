package models.civilization;

import models.User;
import models.civilization.enums.CivilizationNames;
import models.tiles.Tile;
import models.units.Unit;

import java.util.ArrayList;
import java.util.HashMap;

public class Civilization {
    private User user;
    private CivilizationNames civilizationNames;

    private City currentCapital;

    private ArrayList<City> cities = new ArrayList<>();
    private ArrayList<Unit> units = new ArrayList<>();

    private ArrayList<Technology> studiedTechnologies = new ArrayList<>();
    private HashMap<Tile,Integer> discoveredTiles = new HashMap<>();

    private int goldBalance;
    private int scienceBalance;
    private int happinessBalance;
    private int happiness;


    // TODO: Complete fields
    public Civilization(User user, CivilizationNames civilizationNames) {
        this.user = user;
        this.civilizationNames = civilizationNames;
    }

    public HashMap<Tile,Integer> getDiscoveredTiles() {
        return discoveredTiles;
    }

    public void updateDiscoveredTiles(Tile tile, int value) {
        for (Tile oldTile : discoveredTiles.keySet()) {
            if (tile.getCoordinates()[0] == oldTile.getCoordinates()[0] &&
                tile.getCoordinates()[1] == oldTile.getCoordinates()[1]) {
                discoveredTiles.remove(oldTile);
                break;
            }
        }
        discoveredTiles.put(tile, value);
    }

    public User getUser() {
        return user;
    }

    public CivilizationNames getCivilizationNames() {
        return civilizationNames;
    }

    public City getCurrentCapital() {
        return currentCapital;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public ArrayList<Technology> getStudiedTechnologies() {
        return studiedTechnologies;
    }

    public int getGoldBalance() {
        return goldBalance;
    }

    public int getScienceBalance() {
        return scienceBalance;
    }

    public int getHappinessBalance() {
        return happinessBalance;
    }

    public int getHappiness() {
        return happiness;
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
    }
}
