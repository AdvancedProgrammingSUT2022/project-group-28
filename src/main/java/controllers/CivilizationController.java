package controllers;

import java.util.ArrayList;

import controllers.units.UnitController;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.tiles.enums.Direction;
import models.tiles.enums.Terrain;
import models.tiles.enums.TerrainFeature;
import models.units.Unit;
import views.enums.CivilizationMessage;

public class CivilizationController extends GameController {

    public static CivilizationMessage checkNextTurnIsPossible() {
        Civilization civilization = game.getCurrentPlayer();
        // TODO: Add all civlization
        CivilizationMessage checkUnits = UnitController.checkUnitsForNextTurn(civilization.getUnits());
        if (checkUnits != CivilizationMessage.SUCCESS)
            return checkUnits;
        CivilizationMessage checkCities = CityController.checkCitiesForNextTurn(civilization.getCities());
        if (checkCities != CivilizationMessage.SUCCESS)
            return checkCities;
        if(civilization.getCurrentStudyTechnology() == null){
            return CivilizationMessage.NO_TECHNOLOGY_TO_STUDY;
        }
        return CivilizationMessage.SUCCESS;
    }

    public static void nextTurnCivilizationUpdates() {
        Civilization civilization = game.getCurrentPlayer();

        UnitController.nextTurnUnitUpdates(civilization.getUnits());

        CityController.nextTurnCityUpdates(civilization.getCities());

        updateDiscoveredTiles();
    }

    public static void updateDiscoveredTiles() {
        for(Civilization civilization: game.getCivilizations()){
            int turnNumber = game.getTurnNumber();
            Tile[][] map = game.getMap();
            for (Unit unit : civilization.getUnits()) {
                ArrayList<Tile> nearbytiles=new ArrayList<>();
                for (Direction direction : Direction.getDirections()) {
                    if(unit.getTile().getCoordinates()[0] + direction.i < game.MAP_HEIGHT &&
                       unit.getTile().getCoordinates()[0] + direction.i >= 0 &&
                       unit.getTile().getCoordinates()[1] + direction.j < game.MAP_WIDTH && 
                       unit.getTile().getCoordinates()[1] + direction.j >= 0 ){
                        Tile newTile=new Tile(map[unit.getTile().getCoordinates()[0] + direction.i][unit.getTile().getCoordinates()[1]+direction.j]);
                        nearbytiles.add(newTile);
                    }
                }
                for (Tile tile : nearbytiles) {
                    if(tile.getTerrain()!=Terrain.MOUNTAIN &&
                       tile.getTerrain()!=Terrain.HILL &&
                       tile.getTerrainFeature()!=TerrainFeature.JUNGLE &&
                       tile.getTerrainFeature()!=TerrainFeature.FOREST)
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
                Tile newTile=new Tile(unit.getTile());
                civilization.updateDiscoveredTiles(newTile, turnNumber);
            }
        }
    }

}
