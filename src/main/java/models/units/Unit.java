package models.units;

import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitTemplate;
import models.units.enums.UnitState;

public abstract class Unit {
    protected Tile tile;
    protected Civilization civilization;
    protected UnitTemplate unitTemplate;
    protected UnitState unitState;

    protected int movePoint;
    protected Tile moveTarget;

    protected int health;

    public Unit(Tile tile, Civilization civilization, UnitTemplate unitTemplate) {
        this.tile = tile;
        this.civilization = civilization;
        this.unitTemplate = unitTemplate;
        this.unitState = UnitState.FREE;
        this.movePoint = unitTemplate.getMovementPoint();
        this.moveTarget = null;
        this.health = 10;
    }

    public Tile getTile() {
        return tile;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public UnitTemplate getUnitTemplate() {
        return unitTemplate;
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

    public void setUnitTemplate(UnitTemplate unitTemplate) {
        this.unitTemplate = unitTemplate;
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
