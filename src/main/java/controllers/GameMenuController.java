package controllers;

import models.Game;
import models.User;
import models.units.Unit;
import models.units.enums.UnitState;
import views.enums.Message;

import java.util.ArrayList;

public class GameMenuController extends GameController {

    private static void setGame(Game game) { GameController.game = game; }

    public static void startNewGame(ArrayList<User> players, int seed) {
         setGame(new Game(players, seed));
    }

    public static Message nextTurn() {
        // TODO: map errors to map
        if (!checkCivilizationToNextTurn()) return Message.FAILURE;
        if (!checkCitiesToNextTurn()) return Message.FAILURE;
        if (!checkUnitsToNextTurn()) return Message.FAILURE;

        nextTurnUpdatesAndTasks();
        changeTurnPlayer();


        return Message.SUCCESS;
    }


    // TODO: Change return types to Message
    private static boolean checkUnitsToNextTurn() {
        // TODO: Check all units stuff
        ArrayList<Unit> units = game.getTurn().getUnits();
        for (Unit unit : units) {
            if (unit.getUnitState() == UnitState.FREE && unit.getMovePoint() > 0) return false;
        }
        return true;
    }

    private static boolean checkCitiesToNextTurn() {
        // TODO: Check all city stuff
        return true;
    }

    private static boolean checkCivilizationToNextTurn() {
        // TODO: Check all civilization stuff
        return true;
    }

    public static void nextTurnUpdatesAndTasks() {
        UnitController.nextTurnUnitUpdates();
    }
}