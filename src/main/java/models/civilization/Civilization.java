package models.civilization;

import models.User;
import models.civilization.enums.TechnologyTemplate;
import models.civilization.enums.CivilizationNames;
import models.tiles.Tile;
import models.tiles.enums.ResourceTemplate;
import models.tiles.enums.ResourceType;
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
    private int gold = 50;

    private int scienceBalance;
    private Technology currentStudyTechnology;

    private int happinessBalance;
    private int happiness;

    private HashMap<ResourceTemplate, Integer> resources;

    // TODO: Complete fields
    public Civilization(User user, CivilizationNames civilizationNames) {
        this.user = user;
        this.civilizationNames = civilizationNames;
        this.currentStudyTechnology = null;
        this.addTechnology(new Technology(TechnologyTemplate.AGRICULTURE , TechnologyTemplate.AGRICULTURE.getCost()));
        this.setScienceBalance(0);
        this.resources = createResources();
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

    public int getGoldBalance() { return goldBalance; }

    public int getGold() {return gold; }

    public int getScienceBalance() {
        return scienceBalance;
    }

    public int getHappinessBalance() {
        return happinessBalance;
    }

    public int getHappiness() {
        return happiness;
    }

    public HashMap<ResourceTemplate, Integer> getResources() {
        return resources;
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
    }

    public void removeUnit(Unit unit) {this.units.remove(unit);}

    public void addCity(City city) { this.cities.add(city); }

    public void addTechnology(Technology technology){
        this.studiedTechnologies.add(technology);
    }

    public void setCurrentStudyTechnology(Technology currentStudyTechnology) {
        this.currentStudyTechnology = currentStudyTechnology;
    }

    public Technology getCurrentStudyTechnology() {
        return currentStudyTechnology;
    }

    public void setScienceBalance(int scienceBalance) {
        this.scienceBalance = scienceBalance;
    }

    public void setCurrentCapital(City currentCapital) {
        this.currentCapital = currentCapital;
    }

    public void setGold(int gold) { this.gold = gold; }

    public void setGoldBalance(int goldBalance) { this.goldBalance = goldBalance; }

    public void setResourceCount(ResourceTemplate resourceTemplate, int count) {
        this.resources.replace(resourceTemplate, count);
    }
    
    private HashMap<ResourceTemplate, Integer> createResources() {
        HashMap<ResourceTemplate, Integer> resources = new HashMap<>();
        for (ResourceTemplate resourceTemplate : ResourceTemplate.values()) {
            if (resourceTemplate.getType().equals(ResourceType.LUXURY) ||
                resourceTemplate.getType().equals(ResourceType.STRATEGIC)) {
                resources.put(resourceTemplate, 0);
            }
        }
        return resources;
    }

    public void removeCity(City city) {
        this.cities.remove(city);
    }
}
