package controllers;

import controllers.units.UnitController;
import models.Game;
import models.User;
import models.civilization.Civilization;
import models.civilization.enums.TechnologyTemplate;
import models.tiles.Tile;
import models.units.Unit;
import models.units.enums.UnitState;
import views.enums.CivilizationMessage;
import views.enums.Message;

import java.util.ArrayList;

public class GameMenuController extends GameController {

    private static void setGame(Game game) { GameController.game = game; }

    public static void startNewGame(ArrayList<User> players, int seed) {
         setGame(new Game(players, seed));
    }

    public static CivilizationMessage nextTurn() {
        // TODO: redirect errors to map
        CivilizationMessage checkResult = CivilizationController.checkNextTurnIsPossible();
        if (checkResult != CivilizationMessage.SUCCESS) return checkResult;


        CivilizationController.nextTurnCivilizationUpdates();
        changePlayerTurn();

        return CivilizationMessage.SUCCESS;
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

    public static String getTileShowableResource(Tile tile,Civilization civilization) {
        if(tile.getResource()!=null){
            TechnologyTemplate requiredTechnology = tile.getResource().getResourceTemplate().getRequiredTechnology();
            return tile.getResource().getResourceTemplate().getMapSign();
            //TODO: Add check for required technology
        }else
            return "#######";
    }
}