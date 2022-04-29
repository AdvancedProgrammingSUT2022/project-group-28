package models.units;

import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitTemplate;

public abstract class Military extends Unit {
    private int combatStrength;
    
    public Military(Tile tile, Civilization civilization, UnitTemplate unitTemplate) {
        super(tile, civilization, unitTemplate);
        this.combatStrength = unitTemplate.getCombatStrength();
    }

    public int getCombatStrength() {
        return combatStrength;
    }
}
