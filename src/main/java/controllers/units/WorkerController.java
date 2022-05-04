package controllers.units;

import controllers.GameController;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.Worker;
import views.enums.UnitMessage;

public class WorkerController extends GameController {
    public static UnitMessage buildImprovements() {
        if (game.getSelectedUnit() == null) return UnitMessage.NO_SELECTED_UNIT;
        if (!(game.getSelectedUnit() instanceof Worker)) return UnitMessage.NOT_WORKER_UNIT;
        Tile tile = game.getSelectedUnit().getTile();
        Civilization civilization = game.getCurrentPlayer();
        if (!tile.getCivilization().equals(civilization)) return UnitMessage.NOT_PLAYER_TILE;
        return UnitMessage.SUCCESS;
    }
}
