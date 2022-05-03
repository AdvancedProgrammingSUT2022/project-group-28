package controllers.units;

import controllers.GameController;
import controllers.CityController;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.tiles.enums.Direction;
import models.units.Settler;
import models.units.Unit;
import views.enums.UnitMessage;

public class SettlerController extends GameController {
    public static void createSettler(Civilization civilization, Tile tile) {
        Settler settler = new Settler(tile,civilization);
        civilization.addUnit(settler);
        tile.setCivilian(settler);
    }

    public static UnitMessage foundCity() {
        Unit unit = game.getSelectedUnit();
        if (unit == null) return UnitMessage.NO_SELECTED_UNIT;
        if (unit.getCivilization() != game.getCurrentPlayer()) return UnitMessage.NOT_PLAYERS_TURN;
        if (!(unit instanceof Settler)) return UnitMessage.NOT_SETTLER_UNIT;
        UnitMessage checkMessage = checkTileToFoundCity((Settler) unit);
        if (checkMessage != UnitMessage.SUCCESS) return checkMessage;
        CityController.createCity(unit.getCivilization(), unit.getTile());
        return UnitMessage.SUCCESS;
    }

    public static UnitMessage checkTileToFoundCity(Settler settler) {
        // TODO: test method
        Tile tile = settler.getTile();
        int i = tile.getCoordinates()[0];
        int j = tile.getCoordinates()[1];
        if (tile.getCity() != null) return UnitMessage.NEAR_CITY_BOARDERS;
        for (Direction direction : Direction.values()) {
            if (i + direction.i < game.MAP_HEIGHT && i + direction.i >= 0 &&
                j + direction.j < game.MAP_WIDTH && j + direction.j >= 0) {
                Tile checkTile = game.getMap()[i + direction.i][j + direction.j];
                if (checkTile.getCivilization() != null) return UnitMessage.NEAR_CITY_BOARDERS;
                if (checkTile.getCivilization() != null && checkTile.getCivilian().getCivilization() != settler.getCivilization())
                    return UnitMessage.NEAR_ENEMY_UNITS;
                if (checkTile.getMilitary() != null && checkTile.getMilitary().getCivilization() != settler.getCivilization())
                    return UnitMessage.NEAR_ENEMY_UNITS;
            }
        }
        return UnitMessage.SUCCESS;
    }
}
