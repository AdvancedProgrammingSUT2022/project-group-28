package models.units;

import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitTemplate;

public class Worker extends Civilian{
    public Worker(Civilization civilization, Tile tile) {
        super(tile, civilization, UnitTemplate.WORKER);
    }
}
