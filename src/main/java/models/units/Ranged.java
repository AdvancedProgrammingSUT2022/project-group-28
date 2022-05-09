package models.units;

import models.civilization.Civilization;
import models.tiles.Tile;
import models.units.enums.UnitTemplate;

public class Ranged extends Military {

    public Ranged(Tile tile, Civilization civilization, UnitTemplate unitTemplate) {
        super(tile, civilization, unitTemplate);
    }

    public int getRangedCombatStrength(){
        return unitTemplate.getRangedCombatStrength() - (int) (((10-health)/10) * unitTemplate.getRangedCombatStrength());
    }
    
}
