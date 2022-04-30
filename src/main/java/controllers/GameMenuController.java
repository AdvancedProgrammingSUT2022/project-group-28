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
        game.setSelectedCity(null);
        // TODO: Add another next turn stuff such as selected city
    }

    public static ArrayList<CivilizationMessage> startNewTurn() {
        Civilization civilization = game.getCurrentPlayer();
        ArrayList<CivilizationMessage> result = new ArrayList<>();
        if (civilization.getCurrentStudyTechnology() != null) result.add(CivilizationMessage.COMPLETION_OF_THE_STUDY);
        return result;
    }


    public static String getTileShowableResource(Tile tile,Civilization civilization,boolean cheat) {
        if(tile.getResource()!=null){
            if(cheat) return tile.getResource().getResourceTemplate().getMapSign();
            else{
                TechnologyTemplate requiredTechnology = tile.getResource().getResourceTemplate().getRequiredTechnology();
                if(requiredTechnology==null) return tile.getResource().getResourceTemplate().getMapSign();
                else{
                    ArrayList<TechnologyTemplate> userFullTechnologyTemplates = TechnologyController.extractFullProgressTechnology();
                    if(userFullTechnologyTemplates.contains(requiredTechnology)) return tile.getResource().getResourceTemplate().getMapSign();
                }
                return "#######";
            }
        }else
            return "#######";
    }
}