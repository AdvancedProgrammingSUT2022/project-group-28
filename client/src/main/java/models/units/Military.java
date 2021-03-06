package models.units;

import controllers.GameController;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitTemplate;

public abstract class Military extends Unit {
    
    public Military(Tile tile, Civilization civilization, UnitTemplate unitTemplate) {
        super(tile, civilization, unitTemplate);
    }

    public void destroy() {
        this.tile.setMilitary(null);
        this.civilization.removeUnit(this);
        if(GameController.getGame().getSelectedUnit()==this)
            GameController.getGame().setSelectedUnit(null);
        this.civilization.updateDiscoveredTiles(this.tile, GameController.getGame().getTurnNumber());
    }
}
