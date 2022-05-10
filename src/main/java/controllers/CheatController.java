package controllers;

import models.Game;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.Unit;
import views.enums.UnitMessage;

public class CheatController {
    private static CheatController instance = null;

    private CheatController() {}

    public static CheatController getInstance() {
        if (instance == null) instance = new CheatController();
        return instance;
    }

    public void nextTurnCheat(Game game, int count) {
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < game.getCivilizations().size(); j++) {
                Civilization currentPlayer = game.getCurrentPlayer();
                CivilizationController.updateDiscoveredTiles();
                CivilizationController.nextTurnCivilizationUpdates(currentPlayer);
                TechnologyController.updateNextTurnTechnology();
                CivilizationController.updateDiscoveredTiles();
                GameMenuController.changePlayerTurn(game);
            }
        }
    }

    public UnitMessage moveUnitCheat(Game game, int i, int j) {
        Unit unit = game.getSelectedUnit();
        if (unit == null) return UnitMessage.NO_SELECTED_UNIT;
        if (i >= game.MAP_HEIGHT || i < 0 || j >= game.MAP_WIDTH || j < 0) return UnitMessage.INVALID_POSITION;
        Tile targetTile = game.getMap()[i][j];
        Tile startTile = unit.getTile();
        if (startTile.equals(targetTile)) return UnitMessage.SAME_TARGET_TILE;
        if (TileController.isFullTile(targetTile, unit)) return UnitMessage.FULL_TARGET_TILE;
        TileController.freeTileUnit(startTile, unit);
        unit.setTile(targetTile);
        TileController.setTileUnit(targetTile, unit);
        return UnitMessage.SUCCESS;
    }
}
