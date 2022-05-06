package controllers;

import models.Game;
import models.civilization.City;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.tiles.enums.Direction;
import models.tiles.enums.ResourceTemplate;
import models.units.Settler;
import models.units.Unit;
import views.enums.CityMessage;
import views.enums.CivilizationMessage;
import views.enums.UnitMessage;

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
        if (!targetCity.getCivilization().equals(game.getCurrentPlayer())) return CityMessage.NO_PERMISSION;
        game.setSelectedCity(targetCity);
        return CityMessage.SUCCESS;
    }

    public static CityMessage selectCityByPosition(int i, int j) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH) return CityMessage.INVALID_POSITION;
        City city = game.getMap()[i][j].getCity();
        if (city == null) return CityMessage.NOT_CITY;
        if (!city.getCivilization().equals(game.getCurrentPlayer())) return CityMessage.NO_PERMISSION;
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

        // for initial population
        assignRandomCitizen(city);

        // TODO: check there is no reference to settler
        // Delete settler
        Settler settler = (Settler)tile.getCivilian();
        civilization.removeUnit((Unit)settler);
        tile.setCivilian(null);
    }

    // call for each population gain
    public static void assignRandomCitizen(City city) {
        for (Tile tile : city.getTiles()) {
            if (!tile.isWorking()) {
                tile.setWorking(true);
                break;
            }
        }
        updateCity(city);
    }

    // call for each population loss
    public static void freeRandomCitizen(City city) {
        for (Tile tile : city.getTiles()) {
            if (tile.isWorking()) {
                tile.setWorking(false);
                break;
            }
        }
        updateCity(city);
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
        updateCity(city);
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
        updateCity(city);
        return CityMessage.SUCCESS;
    }

    public static void updateCity(City city) {
        city.setFoodBalance(getCityFoodBalance(city));
        city.setProductionBalance(getCityProductionBalance(city));
        // TODO: Add all updates
    }


    public static CivilizationMessage checkCitiesForNextTurn(ArrayList<City> cities) {
        return CivilizationMessage.SUCCESS;
    }

    public static void nextTurnCityUpdates(ArrayList<City> cities) {
        for (City city : cities) {
            updateCity(city);
            growCity(city);
            // TODO: some tasks
        }
    }

    public static ArrayList<Tile> getAvailableTilesToBuy(City city) {
        ArrayList<Tile> availableTiles = new ArrayList<>();

        for (Tile tile : city.getTiles()) {
            int i = tile.getCoordinates()[0];
            int j = tile.getCoordinates()[1];
            for (Direction direction : Direction.values()) {
                if (i + direction.i < game.MAP_HEIGHT && i + direction.i >= 0 &&
                    j + direction.j < game.MAP_WIDTH && j + direction.j >= 0) {
                    Tile availableTile = game.getMap()[direction.i + i][direction.j + j];
                    if (!city.getTiles().contains(availableTile) && !city.getTile().equals(availableTile) &&
                            availableTile.getCivilization() == null && !availableTiles.contains(availableTile)) {
                        availableTiles.add(availableTile);
                    }
                }
            }
        }
        return availableTiles;
    }

    // TODO: change formula
    public static int getTileValue(City city, Tile tile) {
        int value = 0;
        if (tile.getTerrainFeature() != null) value += 10;
        if (tile.getResource() != null) value += 15;
        value += city.getPopulation() * 5 + 15;
        return value;
    }


    public static CityMessage buyTile(City city, Tile tile) {
        if (!city.getCivilization().equals(game.getCurrentPlayer())) return CityMessage.NO_PERMISSION;
        Civilization civilization = city.getCivilization();
        if (civilization.getGold() < getTileValue(city, tile)) return CityMessage.NOT_ENOUGH_GOLD;
        civilization.setGold(civilization.getGold() - getTileValue(city, tile));
        city.getTiles().add(tile);
        tile.setCivilization(city.getCivilization());
        return CityMessage.SUCCESS;
    }


    public static int getCityFoodBalance(City city) {
        // TODO: Full check
        // TODO: Add buildings, unhappiness
        // TODO: Check if settler is under construction
        int consumedFood = city.getPopulation() * 2;
        int producedFood = 0;
        Tile cityTile = city.getTile();
        producedFood += cityTile.getTerrain().getFood();
        if (cityTile.getTerrainFeature() != null) producedFood += cityTile.getTerrainFeature().getFood();
        for (Tile tile : city.getTiles()) {
            if (tile.isWorking()) {
                producedFood += tile.getTerrain().getFood();
                if (tile.getTerrainFeature() != null) producedFood += tile.getTerrainFeature().getFood();
            }

            if (tile.getResource() != null) {
                ResourceTemplate resourceTemplate = tile.getResource().getResourceTemplate();
                if (resourceTemplate.getRequiredImprovement().equals(tile.getImprovement()))
                    producedFood += resourceTemplate.getFood();
            }

            if (tile.getImprovement() != null) producedFood += tile.getImprovement().getFood();
        }

        return producedFood - consumedFood;
    }

    public static int getCityProductionBalance(City city) {
        // TODO: full check
        // TODO: add buildings
        int productionsBalance = 0;
        Tile cityTile = city.getTile();
        productionsBalance += cityTile.getTerrain().getProduction();
        if (cityTile.getTerrainFeature() != null) productionsBalance += cityTile.getTerrainFeature().getProduction();
        for (Tile tile : city.getTiles()) {
            if (tile.isWorking()) {
                productionsBalance += tile.getTerrain().getProduction();
                if (tile.getTerrainFeature() != null) productionsBalance += tile.getTerrainFeature().getProduction();
            }

            if (tile.getResource() != null) {
                ResourceTemplate resourceTemplate = tile.getResource().getResourceTemplate();
                if (resourceTemplate.getRequiredImprovement().equals(tile.getImprovement()))
                    productionsBalance += resourceTemplate.getProduction();
            }
            if (tile.getImprovement() != null) productionsBalance += tile.getImprovement().getProduction();
        }
        return productionsBalance;
    }

    public static int getCityGoldBalance(City city) {
        // TODO: add tranding routes
        // TODO: add building maintainance and additional
        int spentGold = 0;
        int earnedGold = 0;
        Tile cityTile = city.getTile();
        earnedGold += cityTile.getTerrain().getGold();
        if (cityTile.getTerrainFeature() != null) earnedGold += cityTile.getTerrainFeature().getGold();
        for (Tile tile : city.getTiles()) {
            if (tile.isWorking()) {
                earnedGold += tile.getTerrain().getGold();
                if (tile.getTerrainFeature() != null) earnedGold += tile.getTerrainFeature().getGold();
            }

            if (tile.getResource() != null) {
                ResourceTemplate resourceTemplate = tile.getResource().getResourceTemplate();
                if (resourceTemplate.getRequiredImprovement().equals(tile.getImprovement()))
                    earnedGold += resourceTemplate.getGold();
            }

            if (tile.getImprovement() != null) earnedGold += tile.getImprovement().getGold();
        }
        return earnedGold - spentGold;
    }

    private static void growCity(City city) {
        // TODO: change formula
        int growthLimit = city.getPopulation() * (city.getPopulation() + 1) / 2 + 12;
        if (city.getGrowthBucket() + getCityFoodBalance(city) >= growthLimit) {
            city.increasePopulation(1);
            city.setGrowthBucket(city.getGrowthBucket() + getCityFoodBalance(city) - growthLimit);

            assignRandomCitizen(city);
            getTileReward(city);
        } else if (city.getGrowthBucket() + getCityFoodBalance(city) < 0) {
            if (city.getPopulation() > 1) {
                city.decreasePopulation(1);
                freeRandomCitizen(city);
                int newGrowthLimit = city.getPopulation() * (city.getPopulation() + 1) / 2 + 12;
                city.setGrowthBucket(newGrowthLimit + city.getGrowthBucket() + getCityFoodBalance(city));
            } else city.setGrowthBucket(0);
        } else city.setGrowthBucket(city.getGrowthBucket() + getCityFoodBalance(city));

        updateCity(city);
        CivilizationController.updateCivilization(city.getCivilization());
    }

    private static void getTileReward(City city) {
        for (Tile tile : city.getTiles()) {
            int i = tile.getCoordinates()[0];
            int j = tile.getCoordinates()[1];
            for (Direction direction : Direction.values()) {
                if (direction.i + i < game.MAP_HEIGHT && direction.i + i >= 0 ||
                    direction.j + j < game.MAP_WIDTH && direction.j + j >= 0) {
                    Tile reward = game.getMap()[direction.i + i][direction.j + j];
                    if (!reward.equals(city.getTile()) && reward.getCivilization() == null) {
                        city.addTile(reward);
                        reward.setCivilization(city.getCivilization());
                        return;
                    }
                }
            }
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
