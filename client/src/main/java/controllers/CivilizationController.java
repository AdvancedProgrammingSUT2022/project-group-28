package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import controllers.units.UnitController;
import models.civilization.City;
import models.civilization.Civilization;
import models.civilization.enums.BuildingTemplate;
import models.tiles.Tile;
import models.tiles.enums.*;
import models.units.Civilian;
import models.units.Melee;
import models.units.Ranged;
import models.units.Settler;
import models.units.Siege;
import models.units.Unit;
import models.units.Worker;
import models.units.enums.UnitTemplate;
import models.units.enums.UnitType;
import views.notifications.CivilizationNotification;
import views.notifications.GameNotification;

public class CivilizationController extends GameController {

    public static GameNotification checkNextTurnIsPossible() {
        Civilization civilization = game.getCurrentPlayer();
        GameNotification checkUnits = UnitController.checkUnitsForNextTurn(civilization.getUnits());
        if (checkUnits.getNotificationTemplate() != CivilizationNotification.SUCCESS)
            return checkUnits;

        GameNotification checkCities = CityController.checkCitiesForNextTurn(civilization.getCities());
        if (checkCities.getNotificationTemplate() != CivilizationNotification.SUCCESS)
            return checkCities;

        if (civilization.getCities().size() != 0 && civilization.getCurrentStudyTechnology() == null){
            return new GameNotification(CivilizationNotification.NO_TECHNOLOGY, new ArrayList<>(), 0);
        }
        Civilization winner = GameMenuController.isGameEnded();
        if (winner !=null){
            return new GameNotification(CivilizationNotification.GAME_ENDED, new ArrayList<>(Arrays.asList(winner.getCivilizationNames().getName())), 0);
        }
        return new GameNotification(CivilizationNotification.SUCCESS, new ArrayList<>(), 0);
    }

    public static void nextTurnCivilizationUpdates(Civilization civilization) {
        // updates all fields of civilization
        updateCivilizationFields(civilization);
        // increase gold
        civilization.setGold(civilization.getGold() + civilization.getGoldBalance());
        // science progress
        TechnologyController.updateNextTurnTechnology();
        // units: move, work, heal, ...
        UnitController.nextTurnUnitUpdates(civilization.getUnits());
        // cities: construct, repair, ...
        CityController.nextTurnCityUpdates(civilization.getCities());

        updateDiscoveredTiles();
    }

    public static void updateDiscoveredTiles() {
        for(Civilization civilization: game.getCivilizations()){
            int turnNumber = game.getTurnNumber();
            Tile[][] map = game.getMap();
            for (Unit unit : civilization.getUnits()) {
                ArrayList<Tile> nearbyTiles=new ArrayList<>();
                for (Direction direction : Direction.getDirections()) {
                    if(unit.getTile().getCoordinates()[0] + direction.i < game.MAP_HEIGHT &&
                       unit.getTile().getCoordinates()[0] + direction.i >= 0 &&
                       unit.getTile().getCoordinates()[1] + direction.j < game.MAP_WIDTH && 
                       unit.getTile().getCoordinates()[1] + direction.j >= 0 ){
                        Tile newTile=new Tile(map[unit.getTile().getCoordinates()[0] + direction.i][unit.getTile().getCoordinates()[1]+direction.j]);
                        nearbyTiles.add(newTile);
                    }
                }
                for (Tile tile : nearbyTiles) {
                    if(tile.getTerrain()!=Terrain.MOUNTAIN &&
                       tile.getTerrain()!=Terrain.HILL &&
                       tile.getTerrainFeature()!=TerrainFeature.JUNGLE &&
                       tile.getTerrainFeature()!=TerrainFeature.FOREST){
                        addTilesAroundCoordinates(tile, civilization);
                    }
                    Tile newTile=new Tile(tile);
                    civilization.updateDiscoveredTiles(newTile, turnNumber);
                }
                Tile newTile=new Tile(unit.getTile());
                civilization.updateDiscoveredTiles(newTile, turnNumber);
            }

            for (City city : civilization.getCities()) {
                ArrayList<Tile> nearbyTiles = city.getTiles();
                for (Tile tile : nearbyTiles) {
                    addTilesAroundCoordinates(tile, civilization);
                }
                Tile newTile=new Tile(city.getTile());
                civilization.updateDiscoveredTiles(newTile, turnNumber);
            }
        }
    }

    private static void addTilesAroundCoordinates(Tile tile,Civilization civilization){
        int turnNumber = game.getTurnNumber();
        Tile[][] map = game.getMap();
        for (Direction direction : Direction.getDirections()) {
            if(tile.getCoordinates()[0] + direction.i < game.MAP_HEIGHT &&
                tile.getCoordinates()[0] + direction.i >= 0 &&
                tile.getCoordinates()[1] + direction.j < game.MAP_WIDTH && 
                tile.getCoordinates()[1] + direction.j >= 0 ){
                Tile newTile=new Tile(map[tile.getCoordinates()[0] + direction.i][tile.getCoordinates()[1]+direction.j]);
                civilization.updateDiscoveredTiles(newTile, turnNumber);
            }
        }
    }

    public static void updateCivilizationFields(Civilization civilization) {
        TechnologyController.updateScienceBalance();

        civilization.setGoldBalance(getCivilizationGoldBalance(civilization));

        updateResources(civilization);

        civilization.setHappiness(getCivilizationHappiness(civilization));
    }

    public static int getCivilizationGoldBalance(Civilization civilization) {
        int spentGold = civilization.getUnits().size() * 3; // depend on difficulty level
        int producedGold  = 0;

        int goldBalance = 0;
        for (City city : civilization.getCities()) {
            goldBalance += CityController.getCityGoldBalance(city);
        }

        return goldBalance + producedGold - spentGold;
    }

    public static void updateResources(Civilization civilization) {
        for (ResourceTemplate resourceTemplate : civilization.getResources().keySet()) {
            int count = getResourceCount(civilization, resourceTemplate);
            civilization.setResourceCount(resourceTemplate, count);
        }
    }

    public static int getResourceCount(Civilization civilization, ResourceTemplate resourceTemplate) {
        int count = 0;
        for (City city : civilization.getCities()) {
            for (Tile tile : city.getTiles()) {
                if (tile.getResource() != null && tile.getResource().getResourceTemplate().equals(resourceTemplate)) {
                    if (tile.getResource().getResourceTemplate().getRequiredImprovement().equals(tile.getImprovement()))
                        count += 1;
                }
            }
        }

        for (Unit unit : civilization.getUnits()) {
            if (resourceTemplate.equals(unit.getUnitTemplate().getRequiredResource())) count -= 1;
        }
        return count;
    }

    public static int getCivilizationHappiness(Civilization civilization) {
        int happiness = civilization.getInitialHappiness();
        HashMap<ResourceTemplate, Integer> resources = civilization.getResources();
        for (ResourceTemplate resource : resources.keySet()) {
            if (resource.getType() == ResourceType.LUXURY && resources.get(resource) > 0) happiness += 5;
        }
        int rawPopulation = 0;
        for (City city : civilization.getCities()) {
            rawPopulation += city.getPopulation();
            for (BuildingTemplate building : city.getBuildings()) {
                happiness += building.continuousEffect(city).getHappiness();
            }
        }

        happiness -= rawPopulation * 0.2;
        happiness -= civilization.getCities().size() * 0.1;
        happiness -= civilization.getAttachedCities() * 0.5;
        return happiness;
    }

    public static LinkedHashMap<UnitTemplate,String> getAvailableUnitTemplates(Civilization currentPlayer, Tile tile) {
        LinkedHashMap<UnitTemplate,String> availableUnitTemplates = new LinkedHashMap<>();
        for (UnitTemplate unitTemplate : UnitTemplate.values()) {
            if (unitTemplate.getRequiredResource() != null) {
                if (currentPlayer.getResources().get(unitTemplate.getRequiredResource()) == 0) {
                    availableUnitTemplates.put(unitTemplate," resource " + unitTemplate.getRequiredResource().getName()+" needed.");
                    continue;
                }
            }
            if (unitTemplate.getRequiredTechnology() != null){
                if (!TechnologyController.extractFullProgressTechnology().contains(unitTemplate.getRequiredTechnology())){
                    availableUnitTemplates.put(unitTemplate," technology " + unitTemplate.getRequiredTechnology().getName()+" needed.");
                    continue;
                }
            }
            if (unitTemplate.getUnitType() == UnitType.CIVILIAN && tile.getCivilian() != null){
                availableUnitTemplates.put(unitTemplate,"tile is full");
                continue;
            } 
            if (unitTemplate.getUnitType() != UnitType.CIVILIAN && tile.getMilitary() != null) {
                availableUnitTemplates.put(unitTemplate,"tile is full");
                continue;
            }
            if (unitTemplate.getCost()>currentPlayer.getGold()){
                availableUnitTemplates.put(unitTemplate,"not enough gold");
                continue;
            }
            availableUnitTemplates.put(unitTemplate,null);
        }
        return availableUnitTemplates;
    }

    public static void buyUnit(Civilization civilization,Tile tile, UnitTemplate unitTemplate) {
        switch(unitTemplate.getUnitType()){
            case MELEE:
                Melee unit = new Melee(tile,civilization,unitTemplate);
                tile.setMilitary(unit);
                civilization.addUnit(unit);
                break;
            case CIVILIAN:
                Civilian civilian;
                if (unitTemplate == UnitTemplate.WORKER)
                    civilian = new Worker(tile,civilization);
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
            default:
                break;
        }
        civilization.setGold(civilization.getGold()-unitTemplate.getCost());
    }

    public static boolean isTileVisible(Tile tile, Civilization civilization) {
        updateDiscoveredTiles();
        for (Map.Entry<Tile,Integer> entry : civilization.getDiscoveredTiles().entrySet()) {
            if(tile.getCoordinates()[0]==entry.getKey().getCoordinates()[0] &&
               tile.getCoordinates()[1]==entry.getKey().getCoordinates()[1] && 
               entry.getValue()==game.getTurnNumber()){
                return true;
            }
        }
        return false;
    }

    public static void removeLostCivilizations(){
        //TODO: remove lost civilization units?
        ArrayList<Civilization> civilizations = game.getCivilizations();

        ArrayList<Civilization> lostCivilizations = new ArrayList<>();
        for (Civilization civilization : civilizations) {
            if (civilization.getCities().size() == 0) {
                boolean hasSettler = false;
                for (Unit unit : civilization.getUnits()) {
                    if (unit instanceof Settler) {
                        hasSettler = true;
                        break;
                    }
                }
                if (!hasSettler) {
                    lostCivilizations.add(civilization);
                }
            }
        }
        for (Civilization civilization : lostCivilizations) {
            civilizations.remove(civilization);
        }
    }
}
