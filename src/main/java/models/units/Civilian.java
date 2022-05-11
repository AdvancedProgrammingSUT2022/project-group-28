package models.units;

import controllers.GameController;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitTemplate;

public abstract class Civilian extends Unit{
    public Civilian(Tile tile, Civilization civilization, UnitTemplate unitTemplate) {
        super(tile, civilization, unitTemplate);
    }

    public void destroy() {
        this.tile.setCivilian(null);
        this.civilization.removeUnit(this);
        this.civilization.updateDiscoveredTiles(this.tile, GameController.getGame().getTurnNumber());
    }
}
