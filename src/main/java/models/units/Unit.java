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

    protected int movePoint;
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

    public int getMovePoint() {
        return movePoint;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setCivilization(Civilization civilization) {
        this.civilization = civilization;
    }

    public void setUnitPattern(UnitTemplate unitPattern) {
        this.unitPattern = unitPattern;
    }

    public void setUnitState(UnitState unitState) {
        this.unitState = unitState;
    }

    public void setMoveTarget(Tile moveTarget) {
        this.moveTarget = moveTarget;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMovePoint(int movePoint) {
        this.movePoint = movePoint;
    }
}
