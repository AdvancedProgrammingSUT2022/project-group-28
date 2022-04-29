package models.units;

import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitTemplate;

public abstract class Civilian extends Unit{
    public Civilian(Tile tile, Civilization civilization, UnitTemplate unitTemplate) {
        super(tile, civilization, unitTemplate);
    }
}
