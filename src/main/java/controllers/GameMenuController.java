package controllers;

import models.Game;
import models.User;
import models.civilization.Civilization;
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
        // TODO: redirect errors to map
        if (!checkCivilizationToNextTurn()) return Message.FAILURE;
        if (!checkCitiesToNextTurn()) return Message.FAILURE;
        if (!checkUnitsToNextTurn()) return Message.FAILURE;

        nextTurnUpdatesAndTasks();
        changePlayerTurn();

        return Message.SUCCESS;
    }


    // TODO: Change return types to Message
    private static boolean checkUnitsToNextTurn() {
        // TODO: Check all units stuff
        ArrayList<Unit> units = game.getCurrentPlayer().getUnits();
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
        CivilizationController.updateCivilizations();
    }

    private static void changePlayerTurn() {
        ArrayList<Civilization> civilizations = game.getCivilizations();
        Civilization currentCivilization = game.getCurrentPlayer();
        int playerIndex = civilizations.indexOf(currentCivilization);
        if (playerIndex == civilizations.size() - 1) {
            game.setTurnNumber(game.getTurnNumber() + 1);
            game.setCurrentPlayer(civilizations.get(0));
        } else game.setCurrentPlayer(civilizations.get(playerIndex + 1));
        game.setSelectedUnit(null);
        // TODO: Add another next turn stuff such as selected city
    }
}