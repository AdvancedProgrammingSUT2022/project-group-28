package models.units;

import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitTemplate;

public class Ranged extends Military {
    private int range;
    private int rangeCombatStrength;

    public Ranged(Tile tile, Civilization civilization, UnitTemplate unitTemplate) {
        super(tile, civilization, unitTemplate);
        this.range = unitTemplate.getRange();
        this.rangeCombatStrength = unitTemplate.getRangedCombatStrength();
    }

    public int getRange() {
        return range;
    }
    public void setRange(int range) {
        this.range = range;
    }
    public int getRangeCombatStrength() {
        return rangeCombatStrength;
    }
    public void setRangeCombatStrength(int rangeCombatStrength) {
        this.rangeCombatStrength = rangeCombatStrength;
    }
    
}
