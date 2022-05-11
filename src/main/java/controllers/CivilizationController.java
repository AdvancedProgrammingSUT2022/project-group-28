package controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import controllers.units.UnitController;
import models.civilization.City;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.tiles.enums.Direction;
import models.tiles.enums.ResourceTemplate;
import models.tiles.enums.Terrain;
import models.tiles.enums.TerrainFeature;
import models.units.Civilian;
import models.units.Melee;
import models.units.Ranged;
import models.units.Settler;
import models.units.Siege;
import models.units.Unit;
import models.units.Worker;
import models.units.enums.UnitTemplate;
import models.units.enums.UnitType;
import views.enums.CivilizationMessage;

public class CivilizationController extends GameController {

    public static CivilizationMessage checkNextTurnIsPossible() {
        Civilization civilization = game.getCurrentPlayer();
        // TODO: Add all civilization
        // TODO: check city to construct something
        CivilizationMessage checkUnits = UnitController.checkUnitsForNextTurn(civilization.getUnits());
        if (checkUnits != CivilizationMessage.SUCCESS)
            return checkUnits;
        CivilizationMessage checkCities = CityController.checkCitiesForNextTurn(civilization.getCities());
        if (checkCities != CivilizationMessage.SUCCESS)
            return checkCities;
        if(civilization.getCities().size() != 0 && civilization.getCurrentStudyTechnology() == null){
            return CivilizationMessage.NO_TECHNOLOGY_TO_STUDY;
        }
        return CivilizationMessage.SUCCESS;
    }

    public static void nextTurnCivilizationUpdates(Civilization civilization) {
        updateCivilization(civilization);

        // TODO: add gold and sciences

        UnitController.nextTurnUnitUpdates(civilization.getUnits());

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
        Tile newTile=new Tile(tile);
        civilization.updateDiscoveredTiles(newTile, turnNumber);
    }

    public static void updateCivilization(Civilization civilization) {
        civilization.setGoldBalance(getCivilizationGoldBalance(civilization));
        updateResources(civilization);

    }

    public static int getCivilizationGoldBalance(Civilization civilization) {
        // TODO: add trade route, road maintenance
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
}
