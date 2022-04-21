package models.units;

import models.Civilization;
import models.Tile;
import models.enums.UnitTemplate;
import models.enums.UnitState;

public abstract class Unit {
    protected long id;
    protected Tile tile;
    protected Civilization civilization;
    protected UnitTemplate unitPattern;
    protected UnitState unitState;

    protected Tile moveTarget;
    protected int health;
}
