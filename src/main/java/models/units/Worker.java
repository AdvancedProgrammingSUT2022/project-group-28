package models.units;

import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitTemplate;

public class Worker extends Civilian{
    public Worker(Tile tile,Civilization civilization) {
        super(tile, civilization, UnitTemplate.WORKER);
    }
}
