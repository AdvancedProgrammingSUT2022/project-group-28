package controllers;

import models.Game;
import models.civilization.City;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.Settler;
import views.enums.CityMessage;
import views.enums.CivilizationMessage;

import java.util.ArrayList;

public class CityController extends GameController {
    public static CityMessage selectCityByName(String name) {
        ArrayList<Civilization> civilizations = game.getCivilizations();
        City targetCity = null;
        for (Civilization civilization : civilizations) {
            for (City city : civilization.getCities()) {
                if (city.getNAME().equals(name)) targetCity = city;
            }
        }
        if (targetCity == null) return CityMessage.INVALID_NAME;
        game.setSelectedCity(targetCity);
        return CityMessage.SUCCESS;
    }

    public static CityMessage selectCityByPosition(int i, int j) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH) return CityMessage.INVALID_POSITION;
        City city = game.getMap()[i][j].getCity();
        if (city == null) return CityMessage.NOT_CITY;
        game.setSelectedCity(city);
        return CityMessage.SUCCESS;
    }


    public static void createCity(Civilization civilization, Tile tile) {
        String name = getNewCityName(civilization);
        City city = new City(name, civilization, tile);
        civilization.addCity(city);
        tile.setCity(city);
        for (Tile cityTile : city.getTiles()) {
            cityTile.setCivilization(civilization);
        }
        if (civilization.getCurrentCapital() == null) civilization.setCurrentCapital(city);

        // for initial populations
        assignCitizen(city);

        // TODO: check there is no reference to settler
        // Delete settler
        tile.setCivilian(null);
        Settler settler = (Settler)tile.getCivilian();
        civilization.removeUnit(settler);
    }
    // TODO: assign citizens

    // call for each population
    public static void assignCitizen(City city) {
        for (Tile tile : city.getTiles()) {
            if (!tile.isWorking()) {
                tile.setWorking(true);
                break;
            }
        }
    }

    // call for each population loss
    public static void removeCitizen(City city) {
        for (Tile tile : city.getTiles()) {
            if (tile.isWorking()) {
                tile.setWorking(false);
                break;
            }
        }
    }

    public static CityMessage reassignCitizen(int startI, int startJ, int targetI, int targetJ) {
        if (startI < 0 || startI >= game.MAP_HEIGHT || startJ < 0 || startJ >= game.MAP_WIDTH ||
            targetI < 0 || targetI >= game.MAP_HEIGHT || targetJ < 0 || targetJ >= game.MAP_WIDTH)
            return CityMessage.INVALID_POSITION;
        City city = game.getSelectedCity();
        if (city == null) return CityMessage.NO_SELECTED_CITY;
        Tile source = game.getMap()[startI][startJ];
        Tile destination = game.getMap()[startI][startJ];
        if (!city.getTiles().contains(source) || !city.getTiles().contains(destination)) return CityMessage.NOT_CITY_TILE;
        if (!source.isWorking()) return CityMessage.NOT_WORKING_TILE;
        if (destination.isWorking()) return CityMessage.WORKING_TILE;
        source.setWorking(false);
        destination.setWorking(true);
        return CityMessage.SUCCESS;
    }

    public static CivilizationMessage checkCitiesForNextTurn(ArrayList<City> cities) {
        return CivilizationMessage.SUCCESS;
    }

    public static void nextTurnCityUpdates(ArrayList<City> cities) {
        for (City city : cities) {
            // TODO: some tasks
        }
    }

    private static String getNewCityName(Civilization civilization) {
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
}
