package controllers;

import models.Game;
import models.User;

import java.util.ArrayList;

public class GameMenuController extends GameController {

    private static void setGame(Game game) { GameController.game = game; }

    public static void startNewGame(ArrayList<User> players, int seed) {
         setGame(new Game(players, seed));
    }
}