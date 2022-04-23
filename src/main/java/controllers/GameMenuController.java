package controllers;

import models.Game;
import models.User;

import java.util.ArrayList;

public class GameMenuController {
    private static Game game = null;

    public static Game getGame() { return GameMenuController.game; }

    private static void setGame(Game game) { GameMenuController.game = game; }

    public static void startNewGame(ArrayList<User> players, int seed) {
         setGame(new Game(players, seed));
    }
}