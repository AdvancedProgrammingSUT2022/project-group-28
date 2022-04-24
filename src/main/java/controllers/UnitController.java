package controllers;

import models.Game;
import models.tiles.Tile;
import views.enums.Message;

public class UnitController {
    public static Message selectCombatUnit(int i, int j) {
        Game game = GameMenuController.getGame();
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return Message.INVALID_POSITION;
        Tile tile = game.getMap()[i][j];
        if (tile.getMilitary() == null) return Message.NO_COMBAT_UNIT;
        game.setSelectedUnit(tile.getMilitary());
        return Message.SUCCESS;
    }
}
