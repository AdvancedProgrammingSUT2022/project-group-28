package models.units;

import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitState;
import models.units.enums.UnitTemplate;

public class Settler extends Civilian {
    public Settler(Civilization civilization, Tile tile) {
        this.unitTemplate = UnitTemplate.SETTLER;
        this.movePoint = this.unitTemplate.getMovementPoint();
        this.unitState = UnitState.FREE;
        this.civilization = civilization;
        this.civilization.addUnit(this);
        this.tile = tile;
        tile.setCivilian(this);
    }

}
