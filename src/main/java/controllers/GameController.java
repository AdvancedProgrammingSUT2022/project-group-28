package controllers;

import models.Game;

public class GameController {
    protected static Game game;
    public static Game getGame() {
        return game;
    }
}
