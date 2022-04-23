package models.units;

import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitTemplate;
import models.units.enums.UnitState;

public abstract class Unit {
    protected long id;
    protected Tile tile;
    protected Civilization civilization;
    protected UnitTemplate unitPattern;
    protected UnitState unitState;

    protected Tile moveTarget;
    protected int health;

}
