package models.units;

import models.Tile;
import models.enums.UnitPattern;
import models.enums.UnitState;

public abstract class Unit {
    protected long id;
    protected Tile tile;
    protected Tile moveTarget;
    // TODO: add civilization
    protected int health;
    protected UnitPattern unitPattern;
    protected UnitState unitState;
}
