package controllers.units;

import controllers.GameController;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.Settler;

public class SettlerController extends GameController {
    public static void createSettler(Civilization civilization, Tile tile) {
        Settler settler = new Settler(civilization, tile);
        civilization.addUnit(settler);
        tile.setCivilian(settler);
    }
}
