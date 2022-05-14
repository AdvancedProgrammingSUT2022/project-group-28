package controllers;

import models.Constructable;
import models.Game;
import models.civilization.City;
import models.civilization.Civilization;
import models.civilization.Construction;
import models.tiles.Tile;
import models.tiles.enums.Direction;
import models.tiles.enums.ResourceTemplate;
import models.units.*;
import models.units.enums.UnitTemplate;
import models.units.enums.UnitType;
import views.enums.CityMessage;
import views.notifications.CivilizationNotification;
import views.notifications.GameNotification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class CityController extends GameController {
    public static CityMessage selectCityByName(String name, boolean cheatMode) {
        ArrayList<Civilization> civilizations = game.getCivilizations();
        City targetCity = null;
        for (Civilization civilization : civilizations) {
            for (City city : civilization.getCities()) {
                if (city.getNAME().equals(name)) targetCity = city;
            }
        }
        if (targetCity == null) return CityMessage.INVALID_NAME;
        if (!targetCity.getCivilization().equals(game.getCurrentPlayer()) && !cheatMode) return CityMessage.NO_PERMISSION;
        game.setSelectedCity(targetCity);
        return CityMessage.SUCCESS;
    }

    public static CityMessage selectCityByPosition(int i, int j, boolean cheatMode) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH) return CityMessage.INVALID_POSITION;
        City city = game.getMap()[i][j].getCity();
        if (city == null) return CityMessage.NOT_CITY;
        if (!city.getCivilization().equals(game.getCurrentPlayer()) && !cheatMode) return CityMessage.NO_PERMISSION;
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

        // Delete settler
        Settler settler = (Settler)tile.getCivilian();
        settler.destroy();
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
        if(city.getHitPoint()<20) city.setHitPoint(city.getHitPoint()+1);
        city.setAttacked(false);
    }


    public static GameNotification checkCitiesForNextTurn(ArrayList<City> cities) {
        for (City city : cities) {
            if (city.getConstruction() == null) {
                ArrayList<String> data = new ArrayList<>(Arrays.asList(city.getNAME()));
                return new GameNotification(CivilizationNotification.NO_CONSTRUCTION, data, 0);
            }
        }

        return new GameNotification(CivilizationNotification.SUCCESS, new ArrayList<>(), 0);
    }

    public static void nextTurnCityUpdates(ArrayList<City> cities) {
        for (City city : cities) {
            updateCity(city);
            growCity(city);
            constructConstruction(city);
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

    public static int getTileValue(City city, Tile tile) {
        int value = 0;
        if (tile.getTerrainFeature() != null) value += 10;
        if (tile.getResource() != null) value += 15;
        value += city.getPopulation() * 5 + 15;
        return value;
    }

    // TODO: add buy radius
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
        int consumedFood = city.getPopulation() * 2;
        int producedFood = 0;
        Tile cityTile = city.getTile();
        producedFood += cityTile.getTerrain().getFood();
        if (city.getConstruction() != null &&
            city.getConstruction().getConstructionTemplate() instanceof UnitTemplate) {
            UnitTemplate unitTemplate = (UnitTemplate) city.getConstruction().getConstructionTemplate();
            if (unitTemplate == UnitTemplate.SETTLER) return 0;
        }

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
        if(city.getCivilization().getHappiness()<0 && (producedFood - consumedFood) > 0) return 0;
        return producedFood - consumedFood;
    }

    public static int getCityProductionBalance(City city) {
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

        productionsBalance += city.getCitizens();
        return productionsBalance;
    }

    public static int getCityGoldBalance(City city) {
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

    public static LinkedHashMap<Constructable, CityMessage> getConstructableConstructions(City city) {
        LinkedHashMap<Constructable, CityMessage> result = new LinkedHashMap<>();

        for (UnitTemplate unitTemplate : UnitTemplate.values()) {
            result.put(unitTemplate, unitTemplate.checkPossibilityOfConstruction(city));
        }
        return result;
    }

    public static void startConstructing(City city, Constructable construction) {
        city.setConstruction(new Construction(construction));
    }


    public static void checkCityPopulationChange(Game game, City city) {
        int growthLimit = city.getPopulation() * (city.getPopulation() + 1) / 2 + 12;
        if (city.getGrowthBucket() == growthLimit) {
            city.setGrowthBucket(city.getFoodStore());
            city.setFoodStore(0);

            city.increasePopulation(1);
            assignRandomCitizen(city);
            getTileReward(city);
            ArrayList<String> data = new ArrayList<>(Arrays.asList(city.getNAME()));
            GameNotification populationGrowth = new GameNotification(CivilizationNotification.POPULATION_GROWTH,
                                                           data, game.getTurnNumber());
            city.getCivilization().addGameNotification(populationGrowth);
        } else if (city.getGrowthBucket() == -1) {
            if (city.getPopulation() == 1) {
                city.setFoodStore(0);
                city.setFoodBalance(0);
                return;
            }

            city.setGrowthBucket(city.getFoodStore());
            city.setFoodStore(0);

            city.decreasePopulation(1);
            freeRandomCitizen(city);

            ArrayList<String> data = new ArrayList<>(Arrays.asList(city.getNAME()));
            GameNotification populationLoss = new GameNotification(CivilizationNotification.POPULATION_LOSS,
                                                         data, game.getTurnNumber());
            city.getCivilization().addGameNotification(populationLoss);
        }
        updateCity(city);
        CivilizationController.updateCivilizationFields(city.getCivilization());
    }

    private static void growCity(City city) {
        int happiness = city.getCivilization().getHappiness();
        int growthLimit = city.getPopulation() * (city.getPopulation() + 1) / 2 + 12;
        int cityFoodBalance = getCityFoodBalance(city);
        int growthBucket = city.getGrowthBucket();

        Construction construction = city.getConstruction();
        if ((cityFoodBalance > 0 && happiness < 0) ||
             (construction != null && construction.getConstructionTemplate() == UnitTemplate.SETTLER)) return;

        if (growthBucket + cityFoodBalance >= growthLimit) {
            city.setGrowthBucket(growthLimit);
            city.setFoodStore(growthBucket + cityFoodBalance - growthLimit);
        } else if (growthBucket + cityFoodBalance < 0) {
            city.setGrowthBucket(-1);
            int newGrowthLimit = city.getPopulation() * (city.getPopulation() - 1) / 2 + 12;
            city.setFoodStore(newGrowthLimit + growthBucket + cityFoodBalance);
        } else {
            city.setGrowthBucket(growthBucket + cityFoodBalance);
            city.setFoodStore(0);
        }
    }

    public static void checkCityConstructionCompletion(Game game, City city) {
        Construction construction = city.getConstruction();
        if (construction == null) return;
        if (construction.getSpentProduction() == construction.getConstructionTemplate().getCost()) {
            Constructable constructionTemplate = construction.getConstructionTemplate();
            if (constructionTemplate instanceof UnitTemplate) {
                UnitTemplate unitTemplate = (UnitTemplate) constructionTemplate;
                Civilization civilization = city.getCivilization();
                Tile tile = getNearestFreeTile(city, unitTemplate);
                switch (unitTemplate.getUnitType()) {
                    case MELEE:
                        Melee unit = new Melee(tile,civilization,unitTemplate);
                        tile.setMilitary(unit);
                        civilization.addUnit(unit);
                        break;
                    case CIVILIAN:
                        Civilian civilian;
                        if (unitTemplate == UnitTemplate.WORKER)
                            civilian = new Worker(tile, civilization);
                        else
                            civilian = new Settler(tile, civilization);
                        tile.setCivilian(civilian);
                        civilization.addUnit(civilian);
                        break;
                    case RANGED:
                        Ranged ranged = new Ranged(tile,civilization,unitTemplate);
                        tile.setMilitary(ranged);
                        civilization.addUnit(ranged);
                        break;
                    case SIEGE:
                        Siege siege = new Siege(tile,civilization,unitTemplate);
                        tile.setMilitary(siege);
                        civilization.addUnit(siege);
                        break;

                }
            }

            ArrayList<String> data = new ArrayList<>(Arrays.asList(constructionTemplate.getName(), city.getNAME()));
            GameNotification constructionCompleted = new GameNotification(CivilizationNotification.COMPLETION_OF_CONSTRUCTION,
                                                    data, game.getTurnNumber());
            city.getCivilization().addGameNotification(constructionCompleted);

            city.setConstruction(null);
        }
    }

    private static void constructConstruction(City city) {
        if (city.getConstruction() != null) {
            int productionBalance = getCityProductionBalance(city);
            if (productionBalance < 0) return;
            Construction construction = city.getConstruction();
            Constructable constructionTemplate = city.getConstruction().getConstructionTemplate();
            if (productionBalance + construction.getSpentProduction() < constructionTemplate.getCost()) {
                construction.setSpentProduction(construction.getSpentProduction() + productionBalance +
                                                city.getProductionStore());
                city.setProductionStore(0);
            } else {
                city.setProductionStore(construction.getSpentProduction() + productionBalance -
                                                construction.getConstructionTemplate().getCost());
                construction.setSpentProduction(construction.getConstructionTemplate().getCost());
            }
        }
    }


    // TODO: reform the process
    private static Tile getNearestFreeTile(City city, UnitTemplate unitTemplate) {
        Tile tile = city.getTile();
        if (!isFullTile(tile, unitTemplate)) return tile;
        return null;
    }

    private static boolean isFullTile(Tile tile, UnitTemplate unitTemplate) {
        if (unitTemplate.getUnitType() == UnitType.CIVILIAN && tile.getCivilian() != null) return true;
        if (unitTemplate.getUnitType() != UnitType.CIVILIAN && tile.getMilitary() != null) return true;
        return false;
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
        return "HAVIG ABAD";
    }
}
