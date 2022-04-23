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

    public Tile getTile() {
        return tile;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public UnitTemplate getUnitPattern() {
        return unitPattern;
    }

    public UnitState getUnitState() {
        return unitState;
    }

    public Tile getMoveTarget() {
        return moveTarget;
    }

    public int getHealth() {
        return health;
    }
}
