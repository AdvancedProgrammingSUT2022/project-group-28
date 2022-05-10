package controllers;

import models.Game;
import models.civilization.City;
import models.tiles.Tile;
import models.units.Melee;
import models.units.Ranged;
import models.units.Siege;
import models.units.Unit;
import models.units.enums.UnitState;
import views.enums.CombatMessage;

public class CombatController {
    public static CombatMessage unitAttack(int i,int j){
        Game game = GameController.getGame();
        if(game.getSelectedUnit() == null) return CombatMessage.NO_UNIT_SELECTED;
        Unit unit = game.getSelectedUnit();
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return CombatMessage.INVALID_POSITION;
        Tile tile = game.getMap()[i][j];
        if(unit.getMovePoint()==0) return CombatMessage.NO_MOVE_POINT;
        if (!CivilizationController.isTileVisible(tile, game.getCurrentPlayer())) return CombatMessage.NOT_VISIBLE_TILE;
        int distance = TileController.getDistance(unit.getTile(), tile);
        if(((unit instanceof Melee)&&(distance>1)) ||
            (!(unit instanceof Melee) && distance > unit.getUnitTemplate().getRange())) 
            return CombatMessage.OUT_OF_RANGE;
        if(unit instanceof Melee){
            return meleeAttack(unit, tile);
        }else if( unit instanceof Siege){ 
            siegeAttack((Siege)unit, tile);
        }else if( unit instanceof Ranged){
            return rangedAttack((Ranged)unit, tile);
        }
        return CombatMessage.SUCCESS;
    }

    private static CombatMessage meleeAttack(Unit unit, Tile tile) {
        Game game = GameController.getGame();
        int strength = getCombatStrength(unit, false);
        if (tile.getCity()!=null){
            City city = tile.getCity();
            if(city.getCivilization()==game.getCurrentPlayer()) return CombatMessage.CANNOT_ATTACK_YOURSELF;
            city.setHitPoint(city.getHitPoint()-strength);
            unit.setMovePoint(0);
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
            unit.setMovePoint(0);
            if(target.getHealth()>0){
                unit.setHealth(unit.getHealth()- getCombatStrength(target,false));
            }else{
                unit.getTile().setMilitary(null);
                unit.setTile(target.getTile());
            }
        }else{
            return CombatMessage.NO_COMBATABLE;
        }
        return CombatMessage.SUCCESS;
    }

    private static CombatMessage rangedAttack(Ranged unit, Tile tile) {
        Game game = GameController.getGame();
        int strength = getCombatStrength(unit, true);
        if (tile.getCity()!=null){
            City city = tile.getCity();
            if(city.getCivilization()==game.getCurrentPlayer()) return CombatMessage.CANNOT_ATTACK_YOURSELF;
            if(city.getHitPoint()-strength<=0)return CombatMessage.NEEDS_MELEE_UNIT;
            city.setHitPoint(city.getHitPoint()-strength);
            unit.setMovePoint(0);
            // TODO: add city destruction and check to not destroy city by founder
            if(city.getHitPoint()>0 && CivilizationController.isTileVisible(unit.getTile(), city.getCivilization()) && isInCityRange(unit.getTile(), city)){
                unit.setHealth(unit.getHealth()-city.getCombatStrength());
            }
        }else if(tile.getMilitary()!=null || tile.getCivilian()!=null){
            Unit target = tile.getMilitary()!=null?tile.getMilitary():tile.getCivilian();
            if(target.getCivilization()==game.getCurrentPlayer()) return CombatMessage.CANNOT_ATTACK_YOURSELF;
            target.setHealth(target.getHealth()-strength);
            unit.setMovePoint(0);
            if(target.getHealth()>0 && 
              (target instanceof Ranged && 
              !(target instanceof Siege) && 
              TileController.getDistance(tile, unit.getTile()) <= target.getUnitTemplate().getRange())){
                unit.setHealth(unit.getHealth()-getCombatStrength(target, true));
            }
        }else{
            return CombatMessage.NO_COMBATABLE;
        }
        return CombatMessage.SUCCESS;
    }

    private static CombatMessage siegeAttack(Siege unit, Tile tile) {
        Game game = GameController.getGame();
        int strength = getCombatStrength(unit, true);
        if(unit.getUnitState()!=UnitState.PREPARED)return CombatMessage.NOT_PREPARED;
        if (tile.getCity()!=null){
            City city = tile.getCity();
            if(city.getCivilization()==game.getCurrentPlayer()) return CombatMessage.CANNOT_ATTACK_YOURSELF;
            if(city.getHitPoint()-strength<=0)return CombatMessage.NEEDS_MELEE_UNIT;
            city.setHitPoint(city.getHitPoint()-strength);
            unit.setUnitState(UnitState.FREE);
            unit.setMovePoint(0);
            // TODO: add city destruction and check to not destroy city by founder
            if(city.getHitPoint()>0 && CivilizationController.isTileVisible(unit.getTile(), city.getCivilization()) && isInCityRange(unit.getTile(), city)){
                unit.setHealth(unit.getHealth()-city.getCombatStrength());
            }
        }else if(tile.getMilitary()!=null || tile.getCivilian()!=null){
            Unit target = tile.getMilitary()!=null?tile.getMilitary():tile.getCivilian();
            if(target.getCivilization()==game.getCurrentPlayer()) return CombatMessage.CANNOT_ATTACK_YOURSELF;
            target.setHealth(target.getHealth()-strength);
            unit.setUnitState(UnitState.FREE);
            unit.setMovePoint(0);
            if(target.getHealth()>0 && 
              (target instanceof Ranged && 
              !(target instanceof Siege) && 
              TileController.getDistance(tile, unit.getTile()) <= target.getUnitTemplate().getRange())){
                unit.setHealth(unit.getHealth()-getCombatStrength(target, true));
            }
        }else{
            return CombatMessage.NO_COMBATABLE;
        }
        return CombatMessage.SUCCESS;
    }

    private static boolean isInCityRange(Tile tile, City city) {
        for (Tile cityTile : city.getTiles()) {
            if (TileController.getDistance(tile, cityTile) <= 2) {
                return true;
            }
        }
        return false;
    }

    public static int getCombatStrength(Unit unit, Boolean isRanged){
        int strength = 0;
        if(isRanged){
            strength = ((Ranged)unit).getRangedCombatStrength();
        }else{
            strength = unit.getCombatStrength();
        }
        
        strength =(int)Math.ceil (1 + unit.getTile().getTerrain().getCombatModifiers() +
                        ((unit.getTile().getTerrainFeature()==null)?0:unit.getTile().getTerrainFeature().getCombatModifier()));
        return strength;
    }
}
