package controllers;

import models.Game;
import models.civilization.City;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.Settler;
import models.units.Unit;
import views.enums.CityMessage;
import views.enums.CivilizationMessage;

import java.util.ArrayList;

public class CityController extends GameController {
    // TODO: check position is in the visible tiles
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
        assignRandomCitizen(city);

        // TODO: check there is no reference to settler
        // Delete settler
        Settler settler = (Settler)tile.getCivilian();
        civilization.removeUnit((Unit)settler);
        tile.setCivilian(null);
    }
    // TODO: assign citizens

    // call for each population
    public static void assignRandomCitizen(City city) {
        for (Tile tile : city.getTiles()) {
            if (!tile.isWorking()) {
                tile.setWorking(true);
                break;
            }
        }
        city.decreaseCitizens(1);
    }

    public static void freeRandomCitizen(City city) {
        for (Tile tile : city.getTiles()) {
            if (tile.isWorking()) {
                tile.setWorking(false);
                break;
            }
        }
        city.increaseCitizens(1);
    }

    public static CityMessage assignCitizen(int i, int j) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH) return CityMessage.INVALID_POSITION;
        City city = game.getSelectedCity();
        if (city == null) return CityMessage.NO_SELECTED_CITY;
        if (!city.getCivilization().equals(game.getCurrentPlayer())) return CityMessage.NO_PERMISSION;
        Tile tile = game.getMap()[i][j];
        if (!city.getTiles().contains(tile)) return CityMessage.NOT_CITY_TILE;
        if (tile.isWorking()) return CityMessage.WORKING_TILE;
        if (city.getCitizens() == 0) return CityMessage.NO_FREE_CITIZEN;
        tile.setWorking(true);
        city.decreaseCitizens(1);
        return CityMessage.SUCCESS;
    }

    public static CityMessage freeCitizen(int i, int j) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH) return CityMessage.INVALID_POSITION;
        City city = game.getSelectedCity();
        if (city == null) return CityMessage.NO_SELECTED_CITY;
        if (!city.getCivilization().equals(game.getCurrentPlayer())) return CityMessage.NO_PERMISSION;
        Tile tile = game.getMap()[i][j];
        if (!city.getTiles().contains(tile)) return CityMessage.NOT_CITY_TILE;
        if (!tile.isWorking()) return CityMessage.NOT_WORKING_TILE;
        tile.setWorking(false);
        city.increaseCitizens(1);
        return CityMessage.SUCCESS;
    }

    // call for each population loss

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
