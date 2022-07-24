package controllers;

import controllers.units.UnitController;
import models.Game;
import models.User;
import models.civilization.City;
import models.civilization.Civilization;
import models.civilization.enums.TechnologyTemplate;
import models.tiles.Tile;
import models.units.Unit;
import views.notifications.CivilizationNotification;
import views.notifications.GameNotification;

import java.util.ArrayList;

public class GameMenuController extends GameController {

    public static void setGame(Game game) { 
        GameController.game = game; 
    }

    public static void startNewGame(ArrayList<User> players, int seed) {
         setGame(new Game(players, seed));
         CivilizationController.updateDiscoveredTiles();
    }

    public static GameNotification nextTurn() {
        // TODO: redirect errors to map
        CivilizationController.removeLostCivilizations();
        GameNotification checkResult = CivilizationController.checkNextTurnIsPossible();
        if (checkResult.getNotificationTemplate() != CivilizationNotification.SUCCESS) return checkResult;

        Civilization civilization = GameController.getGame().getCurrentPlayer();
        CivilizationController.nextTurnCivilizationUpdates(civilization);
        changePlayerTurn(game);

        return checkResult;
    }

    public static void changePlayerTurn(Game game) {
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

    public static void startNewTurn() {
        Civilization civilization = game.getCurrentPlayer();

        TechnologyController.checkCompletionOfTechnology(game, civilization);

        for (City city : civilization.getCities()) {
            CityController.checkCityPopulationChange(game, city);
            CityController.checkCityConstructionCompletion(game, city);
            // TODO: alert city of enemy
        }

        for (Unit unit : civilization.getUnits()) {
            UnitController.checkAlertUnit(game, unit);
        }
        // TODO: check ........

    }

    public static Civilization isGameEnded(){
        Game game = GameController.getGame();
        ArrayList<Civilization> civilizations = game.getCivilizations();
        if (civilizations.size() == 1) return civilizations.get(0);
        ArrayList<Civilization> hasOwnCapital = new ArrayList<>();
        for (Civilization civilization : civilizations) {
            for(City city : civilization.getCities()){
                if (civilization.getCivilizationNames().getCapital().equals(city.getNAME())){
                    hasOwnCapital.add(civilization);
                    break;
                }
            }
        }
        if (hasOwnCapital.size() == 1) return hasOwnCapital.get(0);
        // TODO: check year condition
        return null;
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
                return "###";
            }
        }else
            return "###";
    }
}