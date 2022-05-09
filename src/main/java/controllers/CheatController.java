package controllers;

import models.Game;
import models.civilization.Civilization;

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
                CivilizationController.nextTurnCivilizationUpdates(currentPlayer);
                TechnologyController.updateNextTurnTechnology();
                GameMenuController.changePlayerTurn(game);
            }
        }
    }
}
