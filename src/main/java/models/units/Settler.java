package models.units;

import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitTemplate;

public class Settler extends Civilian {
    public Settler(Civilization civilization, Tile tile) {
        super(tile, civilization, UnitTemplate.SETTLER);
    }

}
