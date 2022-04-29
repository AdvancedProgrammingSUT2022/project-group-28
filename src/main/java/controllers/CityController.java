package controllers;

import models.Game;
import models.civilization.City;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.Settler;

import java.util.ArrayList;

public class CityController extends GameController {
    public static void createCity(Civilization civilization, Tile tile) {
        String name = getNewCityName(civilization);
        City city = new City(name, civilization, tile);
        civilization.addCity(city);
        tile.setCity(city);
        for (Tile cityTile : city.getTiles()) {
            cityTile.setCivilization(civilization);
        }

        // TODO: check there is no reference to settler
        // Delete settler
        tile.setCivilian(null);
        Settler settler = (Settler)tile.getCivilian();
        civilization.removeUnit(settler);
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
