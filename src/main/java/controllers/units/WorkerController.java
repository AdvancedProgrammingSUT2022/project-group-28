package controllers.units;

import controllers.GameController;
import models.civilization.Civilization;
import models.tiles.Project;
import models.tiles.Tile;
import models.tiles.enums.ImprovementTemplate;
import models.units.Worker;
import models.units.enums.UnitState;
import views.enums.UnitMessage;

public class WorkerController extends GameController {
    public static UnitMessage checkImprovementIsPossibleToBuild() {
        if (game.getSelectedUnit() == null) return UnitMessage.NO_SELECTED_UNIT;
        if (!(game.getSelectedUnit() instanceof Worker)) return UnitMessage.NOT_WORKER_UNIT;
        if (game.getSelectedUnit().getMovePoint() == 0) return UnitMessage.NO_MOVE_POINT;
        Tile tile = game.getSelectedUnit().getTile();
        if (tile.getCity() != null) return UnitMessage.CITY_TILE;
        Civilization civilization = game.getCurrentPlayer();
        if (!tile.getCivilization().equals(civilization)) return UnitMessage.NOT_PLAYER_TILE;
        return UnitMessage.SUCCESS;
    }

    public static void startImprovement(Worker worker, ImprovementTemplate improvementTemplate) {
        Tile tile = worker.getTile();
        worker.setMovePoint(0);
        improvementTemplate.startImprovement(tile);
        worker.setUnitState(UnitState.WORKING);
    }

    public static void nextTurnWorkerUpdates(Worker worker) {
        Tile tile = worker.getTile();
        Project project = tile.getProject();
        if (project == null) {
            worker.setUnitState(UnitState.FREE);
            return;
        }
        project.setSpentTurns(project.getSpentTurns() + 1);
        if (project.getSpentTurns() == project.getImprovement().getTurnCost()) {
            project.getImprovement().completeImprovement(tile);
            worker.setUnitState(UnitState.FREE);
        }
    }
}
