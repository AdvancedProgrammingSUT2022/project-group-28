package controllers;

import models.Game;
import models.civilization.City;
import models.tiles.Tile;
import models.units.Unit;
import views.enums.CombatMessage;

public class CombatController {
    public static CombatMessage unitAttack(int i,int j){
        Game game = GameController.getGame();
        if(game.getSelectedUnit() == null) return CombatMessage.NO_UNIT_SELECTED;
        Unit unit = game.getSelectedUnit();
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return CombatMessage.INVALID_POSITION;
        Tile tile = game.getMap()[i][j];
        if (!CivilizationController.isTileVisible(tile, game.getCurrentPlayer())) return CombatMessage.NOT_VISIBLE_TILE;
        int distance = TileController.getDistance(unit.getTile(), tile);
        if((unit.getUnitTemplate().getRange()==0)&&(distance!=1) ||
            distance > unit.getUnitTemplate().getRange()) 
            return CombatMessage.OUT_OF_RANGE;
        if(unit.getUnitTemplate().getRange()==0){
            return meleeAttack(unit, tile);
        }
        return CombatMessage.SUCCESS;
    }

    private static CombatMessage meleeAttack(Unit unit, Tile tile) {
        Game game = GameController.getGame();
        int strength = (int)Math.ceil(unit.getCombatStrength() *
                                     (unit.getTile().getTerrain().getCombatModifiers() +
                                      unit.getTile().getTerrainFeature().getCombatModifier()));
        if (tile.getCity()!=null){
            City city = tile.getCity();
            if(city.getCivilization()==game.getCurrentPlayer()) return CombatMessage.CANNOT_ATTACK_YOURSELF;
            city.setHitPoint(city.getHitPoint()-strength);
            // TODO: add city destruction and check to not destroy city by founder
            if(city.getHitPoint()>0){
                unit.setHealth(unit.getHealth()-city.getCombatStrength());
            }else{
                unit.getTile().setMilitary(null);
                unit.setTile(city.getTile());
            }
        }else if(tile.getMilitary()!=null || tile.getCivilian()!=null){
            Unit target = tile.getMilitary()!=null?tile.getMilitary():tile.getCivilian();
            if(target.getCivilization()==game.getCurrentPlayer()) return CombatMessage.CANNOT_ATTACK_YOURSELF;
            target.setHealth(target.getHealth()-strength);
            if(target.getHealth()>0){
                unit.setHealth(unit.getHealth()-target.getCombatStrength());
            }else{
                unit.getTile().setMilitary(null);
                unit.setTile(target.getTile());
            }
        }else{
            return CombatMessage.NO_COMBATABLE;
        }
        return CombatMessage.SUCCESS;
    }
}
