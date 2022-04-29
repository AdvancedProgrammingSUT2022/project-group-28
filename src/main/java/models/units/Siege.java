package models.units;

import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitTemplate;

public class Siege extends Ranged{
    private boolean isPrepared;

    public Siege(Tile tile, Civilization civilization, UnitTemplate unitTemplate) {
        super(tile, civilization, unitTemplate);
        this.setPrepared(false);
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setPrepared(boolean isPrepared) {
        this.isPrepared = isPrepared;
    }
}
