package controllers.units;

import controllers.CombatController;
import controllers.GameController;
import controllers.TileController;
import models.Game;
import models.civilization.Civilization;
import models.tiles.Tile;
import models.tiles.enums.Direction;
import models.tiles.enums.Terrain;
import models.tiles.enums.TerrainFeature;
import models.units.Civilian;
import models.units.Melee;
import models.units.Military;
import models.units.Settler;
import models.units.Siege;
import models.units.Unit;
import models.units.Worker;
import models.units.enums.UnitState;
import views.components.UnitInfo.UnitAction;
import views.enums.UnitMessage;
import views.notifications.CivilizationNotification;
import views.notifications.GameNotification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class UnitController extends GameController {
    public static UnitMessage selectCombatUnit(int i, int j, boolean cheatMode) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return UnitMessage.INVALID_POSITION;
        Tile tile = game.getMap()[i][j];
        if (tile.getMilitary() == null) return UnitMessage.NOT_COMBAT_UNIT;
        if (!tile.getMilitary().getCivilization().equals(game.getCurrentPlayer()) && !cheatMode) return UnitMessage.NO_PERMISSION;
        game.setSelectedUnit(tile.getMilitary());
        return UnitMessage.SUCCESS;
    }

    public static UnitMessage selectNonCombatUnit(int i, int j, boolean cheatMode) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return UnitMessage.INVALID_POSITION;
        Tile tile = game.getMap()[i][j];
        if (tile.getCivilian() == null) return UnitMessage.NOT_NONCOMBAT_UNIT;
        if (!tile.getCivilian().getCivilization().equals(game.getCurrentPlayer()) && !cheatMode) return UnitMessage.NO_PERMISSION;
        game.setSelectedUnit(tile.getCivilian());
        return UnitMessage.SUCCESS;
    }

    public static UnitMessage sleepUnit(Unit unit) {
        if (unit.getUnitState() == UnitState.SLEPT) return UnitMessage.IS_SLEPT;
        unit.setUnitState(UnitState.SLEPT);
        return UnitMessage.SUCCESS;
    }

    public static UnitMessage alertUnit(Unit unit) {
        if (unit instanceof Civilian) return UnitMessage.NOT_COMBAT_UNIT;
        if (unit.getUnitState() == UnitState.ALERT) return UnitMessage.IS_ALERT;
        unit.setUnitState(UnitState.ALERT);
        return UnitMessage.SUCCESS;
    }

    public static UnitMessage wakeUnit(Unit unit) {
        if (unit.getUnitState() != UnitState.ALERT && unit.getUnitState() != UnitState.SLEPT)
            return UnitMessage.IS_AWAKE;
        unit.setUnitState(UnitState.FREE);
        return UnitMessage.SUCCESS;
    }

    public static UnitMessage moveUnitToTarget(int i, int j) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return UnitMessage.INVALID_POSITION;
        if (game.getSelectedUnit() == null) return UnitMessage.NO_SELECTED_UNIT;
        Unit unit = game.getSelectedUnit();
        Tile startTile = unit.getTile();
        Tile targetTile = game.getMap()[i][j];
        if (!unit.getCivilization().equals(game.getCurrentPlayer())) return UnitMessage.NOT_PLAYERS_TURN;
        if (startTile.getCoordinates()[0] == i && startTile.getCoordinates()[1] == j) return UnitMessage.SAME_TARGET_TILE;
        if (!targetTile.isAccessible()) return UnitMessage.NOT_ACCESSIBLE_TILE;
        if (isFullTile(unit, targetTile)) return UnitMessage.FULL_TARGET_TILE;
        // TODO: Add enemy border check

        moveUnit(unit, startTile, targetTile);
        return UnitMessage.SUCCESS;
    }

    public static GameNotification checkUnitsForNextTurn(ArrayList<Unit> units) {
        for (Unit unit : units) {
            if (unit.getUnitState() == UnitState.FREE && unit.getMovePoint() > 0) {
                Integer i = unit.getTile().getCoordinates()[0];
                Integer j = unit.getTile().getCoordinates()[1];
                ArrayList<String> data = new ArrayList<>(Arrays.asList(i.toString(), j.toString()));
                return new GameNotification(CivilizationNotification.FREE_UNIT, data, 0);
            }
        }
        return new GameNotification(CivilizationNotification.SUCCESS, new ArrayList<>(), 0);
    }

    public static Unit findFreeUnit() {
        ArrayList<Unit> units = game.getCurrentPlayer().getUnits();
        for (Unit unit : units) {
            if (unit.getUnitState() == UnitState.FREE && unit.getMovePoint() > 0) return unit;
        }
        return null;
    }

    public static void nextTurnUnitUpdates(ArrayList<Unit> units) {
        for (Unit unit : units) {
            switch (unit.getUnitState()) {
                case MOVING:
                    if (unit.getMovePoint() == unit.getUnitTemplate().getMovementPoint()) moveMovingUnit(unit);
                    break;
                case WORKING:
                    WorkerController.nextTurnWorkerUpdates((Worker) unit);
                    break;
                case FORTIFYING:
                    UnitController.healFortifyingUnit(unit);
                case HEALING:
                    UnitController.healHealingUnit(unit);
                    break;
                default:
                    break;
            }
            unit.setMovePoint(unit.getUnitTemplate().getMovementPoint());
        }
    }

    private static boolean isFullTile(Unit unit, Tile targetTile) {
        if (unit instanceof Military && targetTile.getMilitary() != null) return true;
        if (unit instanceof Civilian && targetTile.getCivilian() != null) return true;
        return false;
    }

    private static void moveUnit(Unit unit, Tile startTile, Tile targetTile) {
        MapPair[][] checkMap = new MapPair[game.MAP_HEIGHT][game.MAP_WIDTH];
        for (int i = 0; i < game.MAP_HEIGHT; i++) {
            for (int j = 0; j < game.MAP_WIDTH; j++) {
                checkMap[i][j] = new MapPair();
            }
        }

        int startI = startTile.getCoordinates()[0];
        int startJ = startTile.getCoordinates()[1];

        int currentMovePoint = unit.getMovePoint();

        checkMap[startI][startJ].setChecked(true);
        checkMap[startI][startJ].setMovePoint(currentMovePoint * 3);

        for (Direction direction: Direction.getDirections()) {
            if (isValidDirection(startI, startJ, direction, checkMap)) {
                int newMovePoint = currentMovePoint * 3 - getDirectionMovePoint(startI, startJ, direction, 3 * currentMovePoint);
                if (newMovePoint >= 0 || (currentMovePoint >= 1 && currentMovePoint <= 3)) {
                    tagMapMovePoints(startI + direction.i, startJ + direction.j, newMovePoint, checkMap);
                }
            }
        }

        HashMap<Integer[], Integer> distances = getDistances(targetTile, checkMap, currentMovePoint);

        if (isDirectMovePossible(distances)) {
            directMove(targetTile, checkMap);
        } else if (currentMovePoint == 0){
            unit.setMoveTarget(targetTile);
            unit.setUnitState(UnitState.MOVING);
        } else {
            Tile bestTile = getBestTile(distances);
            if (bestTile != null && !bestTile.equals(startTile)) indirectMove(bestTile, targetTile, checkMap);
            else{
                unit.setMoveTarget(null);
                unit.setUnitState(UnitState.FREE);
            }
        }
    }
    
    private static boolean isValidDirection(int i, int j, Direction direction, MapPair[][] checkMap) {
        if (i + direction.i < game.MAP_HEIGHT && i + direction.i >= 0 &&
            j + direction.j < game.MAP_WIDTH && j + direction.j >= 0 &&
            !checkMap[i + direction.i][j + direction.j].isChecked() &&
            game.getMap()[i + direction.i][j + direction.j].isAccessible()) {
            return true;
        }
        return false;
    }

    private static int getDirectionMovePoint(int i, int j, Direction direction, int currentMovePoint) {
        Tile[][] map = game.getMap();
        Tile tile = map[i][j];
        Tile nextTile = map[i + direction.i][j + direction.j];
        if (nextTile.getCity() != null) return 1;
        if (nextTile.isRailRoadConstructed()) return 1;
        if (nextTile.isRoadConstructed()) return 2;
        if (tile.getRivers().contains(direction)) return currentMovePoint;
        int terrainMP = nextTile.getTerrain().getMovementCost();
        int terrainFeatureMP = 0;
        if (nextTile.getTerrainFeature() != null) {
            terrainFeatureMP = nextTile.getTerrainFeature().getMovementCost();
        }
        return 3 * Math.max(terrainMP, terrainFeatureMP);
    }

    private static void tagMapMovePoints(int i, int j, int movePoint, MapPair[][] checkMap) {
        if (movePoint > checkMap[i][j].getMovePoint()) checkMap[i][j].setMovePoint(movePoint);
        if (movePoint < 0) checkMap[i][j].setMovePoint(0);
        checkMap[i][j].setChecked(true);

        if (movePoint <= 0) {
            checkMap[i][j].setChecked(false);
            return;
        }

        for (Direction direction: Direction.getDirections()) {
            if (isValidDirection(i, j, direction, checkMap)) {
                int newMovePoint = movePoint - getDirectionMovePoint(i, j, direction, movePoint);
                if (newMovePoint >= 0 || (movePoint >= 1 && movePoint <= 3)) {
                    tagMapMovePoints(i + direction.i, j + direction.j, newMovePoint, checkMap);
                }
            }
        }
        checkMap[i][j].setChecked(false);
    }

    private static HashMap<Integer[],Integer> getDistances(Tile targetTile, MapPair[][] checkMap, int currentMovePoint) {
        HashMap<Integer[], Integer> result = new HashMap<>();

        for (int i = 0; i < game.MAP_HEIGHT; i++) {
            for (int j = 0; j < game.MAP_WIDTH; j++) {
                if (checkMap[i][j].getMovePoint() >= 0) {
                    Tile tile = game.getMap()[i][j];
                    int r = TileController.getDistance(targetTile, tile);
                    result.put(new Integer[]{i, j}, r);
                }
            }
        }
        return result;
    }

    private static boolean isDirectMovePossible(HashMap<Integer[], Integer> distances) {
        Set<Integer[]> coordinates = distances.keySet();

        for (Integer[] coordinate : coordinates) {
            if (distances.get(coordinate) == 0) return true;
        }
        return false;
    }

    private static void directMove(Tile targetTile, MapPair[][] checkMap) {
        Unit unit = game.getSelectedUnit();
        moveToTile(unit, targetTile, checkMap);

        unit.setMoveTarget(null);
        unit.setUnitState(UnitState.FREE);
    }

    private static void moveToTile(Unit unit, Tile targetTile, MapPair[][] checkMap) {
        Tile startTile = unit.getTile();

        startTile.freeUnit(unit);
        targetTile.addUnit(unit);

        int i = targetTile.getCoordinates()[0];
        int j = targetTile.getCoordinates()[1];

        int newMovePoint = checkMap[i][j].getMovePoint();
        unit.setMovePoint(newMovePoint / 3);
        unit.setTile(targetTile);
        // capture unit
        if (unit instanceof Military && targetTile.getCivilian() != null) {
            Civilian civilian = targetTile.getCivilian();
            Civilization civilianCivilization = civilian.getCivilization();
            civilianCivilization.removeUnit((Unit) civilian);
            civilian.setCivilization(unit.getCivilization());
            Civilization militaryCivilization = unit.getCivilization();
            militaryCivilization.addUnit((Unit) civilian);
        }

    }

    private static void indirectMove(Tile bestTile, Tile targetTile, MapPair[][] checkMap) {
        Unit unit = game.getSelectedUnit();
        moveToTile(unit, bestTile, checkMap);

        unit.setMoveTarget(targetTile);
        unit.setUnitState(UnitState.MOVING);
    }

    private static Tile getBestTile(HashMap<Integer[], Integer> distances) {
        int movePoint = 1000;
        Set<Integer[]> coordinates = distances.keySet();
        Integer[] bestCoordinates = new Integer[] {};
        Tile[][] map = game.getMap();
        for (Integer[] coordinate : coordinates) {
            if (distances.get(coordinate) < movePoint &&
                !isFullTile(game.getSelectedUnit(), map[coordinate[0]][coordinate[1]])) {
                movePoint = distances.get(coordinate);
                bestCoordinates = coordinate;
            }
        }
        Tile bestTile = null;
        if (bestCoordinates.length != 0) {
            int i = bestCoordinates[0];
            int j = bestCoordinates[1];
            bestTile = game.getMap()[i][j];
        }
        return bestTile;
    }

    private static void moveMovingUnit(Unit unit) {
        Tile targetTile = unit.getMoveTarget();
        Tile startTile = unit.getTile();

        MapPair[][] checkMap = new MapPair[game.MAP_HEIGHT][game.MAP_WIDTH];
        for (int i = 0; i < game.MAP_HEIGHT; i++) {
            for (int j = 0; j < game.MAP_WIDTH; j++) {
                checkMap[i][j] = new MapPair();
            }
        }

        int startI = startTile.getCoordinates()[0];
        int startJ = startTile.getCoordinates()[1];
        int currentMovePoint = unit.getMovePoint();

        checkMap[startI][startJ].setChecked(true);
        checkMap[startI][startJ].setMovePoint(currentMovePoint * 3);
        for (Direction direction: Direction.getDirections()) {
            if (isValidDirection(startI, startJ, direction, checkMap)) {
                int newMovePoint = currentMovePoint * 3 - getDirectionMovePoint(startI, startJ, direction, 3 * currentMovePoint);
                if (newMovePoint >= 0 || (currentMovePoint >= 1 && currentMovePoint <= 3)) {
                    tagMapMovePoints(startI + direction.i, startJ + direction.j, newMovePoint, checkMap);
                }
            }
        }

        HashMap<Integer[], Integer> distances = getDistances(targetTile, checkMap, currentMovePoint);
        if (isDirectMovePossible(distances)) {
            if (!isFullTile(unit, targetTile)) {
                moveToTile(unit, targetTile, checkMap);
            }
            unit.setUnitState(UnitState.FREE);
            unit.setMoveTarget(null);
        } else {
            Tile bestTile = getBestTile(distances);
            if (bestTile != null && !bestTile.equals(startTile)) {
                indirectMoveMovingUnit(unit, bestTile, targetTile, checkMap);
            } else {
                unit.setUnitState(UnitState.FREE);
                unit.setMoveTarget(null);
            }
        }
    }

    private static void indirectMoveMovingUnit(Unit unit, Tile bestTile, Tile targetTile, MapPair[][] checkMap) {
        moveToTile(unit, bestTile, checkMap);
        unit.setMoveTarget(targetTile);
        unit.setUnitState(UnitState.MOVING);
    }

    public static UnitMessage prepareUnit(){
        Unit unit = game.getSelectedUnit();
        if(unit == null) return UnitMessage.NO_SELECTED_UNIT;
        else if(!(unit instanceof Siege)) return UnitMessage.NO_SIEGE_UNIT;
        ((Siege)unit).setUnitState(UnitState.PREPARED);
        unit.setMovePoint(0);
        return UnitMessage.SUCCESS;
    }

    public static void checkAlertUnit(Game game, Unit unit) {
        if (unit.getUnitState() == UnitState.ALERT) {
            ArrayList<Tile> scopeOfVision = getUnitScopeOfVision(game, unit);
            for (Tile tile : scopeOfVision) {
                if (tile.getMilitary() != null) {
                    Military military = tile.getMilitary();
                    if (!military.getCivilization().equals(unit.getCivilization())) {
                        Integer unitI = unit.getTile().getCoordinates()[0];
                        Integer unitJ = unit.getTile().getCoordinates()[1];
                        Integer enemyI = military.getTile().getCoordinates()[0];
                        Integer enemyJ = military.getTile().getCoordinates()[1];
                        ArrayList<String> data = new ArrayList<>(Arrays.asList(unitI.toString(), unitJ.toString(),
                                                                 enemyI.toString(), enemyJ.toString()));
                        GameNotification unitAlert = new GameNotification(CivilizationNotification.NEAR_ENEMY_UNIT_ALERT,
                                                    data, game.getTurnNumber());
                        unit.getCivilization().addGameNotification(unitAlert);
                        unit.setUnitState(UnitState.FREE);
                        return;
                    }
                }
            }
        }
    }

    public static UnitMessage fortifyUnit(Unit unit) {
        if (unit instanceof Civilian) return UnitMessage.NOT_COMBAT_UNIT;
        if (unit.getUnitState() == UnitState.FORTIFYING) return UnitMessage.IS_FORTIFYING;
        unit.setUnitState(UnitState.FORTIFYING);
        return UnitMessage.SUCCESS;
    }

    public static UnitMessage healUnit(Unit unit) {
        if (unit.getHealth() == 10) return UnitMessage.FULL_HEALTH;
        unit.setUnitState(UnitState.HEALING);
        return UnitMessage.SUCCESS;
    }

    private static void healFortifyingUnit(Unit unit) {
        Tile tile = unit.getTile();
        if (unit.getHealth() < 10) {
            if (unit.getCivilization().equals(tile.getCivilization()))
                unit.setHealth(Math.min(unit.getHealth() + 2, 10));
            else unit.setHealth(unit.getHealth() + 1);
        }
    }

    private static void healHealingUnit(Unit unit) {
        Tile tile = unit.getTile();
        if (unit.getHealth() < 10) {
            if (unit.getCivilization().equals(tile.getCivilization()))
                unit.setHealth(Math.min(unit.getHealth() + 2, 10));
            else unit.setHealth(unit.getHealth() + 1);
        }
        if (unit.getHealth() == 10) unit.setUnitState(UnitState.FREE);
    }
    
    public static ArrayList<Tile> getUnitScopeOfVision(Game game, Unit unit) {
        ArrayList<Tile> scopeOfVision = new ArrayList<>();
        Tile unitTile = unit.getTile();
        scopeOfVision.add(unitTile);
        Tile[][] map = game.getMap();
        for (Direction direction : Direction.values()) {
            int i = unitTile.getCoordinates()[0] + direction.i;
            int j = unitTile.getCoordinates()[1] + direction.j;
            if (i >= 0 && i < game.MAP_HEIGHT && j >= 0 && j < game.MAP_WIDTH) {
                Tile tile = map[i][j];
                scopeOfVision.add(tile);
                if (tile.getTerrain() != Terrain.MOUNTAIN && tile.getTerrain() != Terrain.HILL &&
                    tile.getTerrainFeature() != TerrainFeature.JUNGLE && tile.getTerrainFeature() != TerrainFeature.FOREST) {
                    for (Direction directionPrime : Direction.values()) {
                        int iPrime = tile.getCoordinates()[0] + directionPrime.i;
                        int jPrime = tile.getCoordinates()[1] + directionPrime.j;
                        if (iPrime >= 0 && iPrime < game.MAP_HEIGHT && jPrime >= 0 && jPrime < game.MAP_WIDTH) {
                            if (!scopeOfVision.contains(map[iPrime][jPrime])) {
                                scopeOfVision.add(map[iPrime][jPrime]);
                            }
                        }
                    }
                }
            }
        }
        return scopeOfVision;
    }

    public static String getMoveUnitState(int currentMovePoint, int movementPoint) {
        if(currentMovePoint == movementPoint){
            return "green";
        }
        else if(currentMovePoint == 0 ){
            return "empty";
        }
        else {
            return "yellow";
        }
    }

    public static boolean isTileAccessible(Tile tile, Unit unit) {
        if (tile.getTerrain() == Terrain.MOUNTAIN || 
            tile.getTerrainFeature() == TerrainFeature.ICE ||
            tile.getTerrain() == Terrain.OCEAN ) {
            return false;
        }
        if (tile.getMilitary() != null && unit instanceof Military ||
            tile.getCivilian() != null && unit instanceof Civilian) {
                return false;
        }
        if (unit.getCivilization().getDiscoveredTiles().containsKey(tile) && unit.getCivilization().getDiscoveredTiles().get(tile) == game.getTurnNumber())
            return false;
        return true;
    }

    public static boolean isTileAttackable(Tile tile, Unit unit) {
        if (unit instanceof Civilian) return false;
        if (unit instanceof Siege && ((Siege)unit).getUnitState() != UnitState.PREPARED) return false;
        if (tile.getMilitary() == null && tile.getCivilian() == null && tile.getCity() == null) return false;
        if (unit.getCivilization().getDiscoveredTiles().containsKey(tile) && unit.getCivilization().getDiscoveredTiles().get(tile) == game.getTurnNumber())
            return false;
        if (tile.getMilitary() != null && tile.getMilitary().getCivilization().equals(unit.getCivilization())) return false;
        if (tile.getCivilian() != null && tile.getCivilian().getCivilization().equals(unit.getCivilization())) return false;
        if (tile.getCity() != null && tile.getCity().getCivilization().equals(unit.getCivilization())) return false;
        if (unit.getMovePoint() == 0) return false;
        int distance = TileController.getDistance(unit.getTile(), tile);
        if(((unit instanceof Melee)&&(distance>1)) ||
            (!(unit instanceof Melee) && distance > unit.getUnitTemplate().getRange())) return false;
        if(!(unit instanceof Melee) && tile.getCity() != null) {
            int strength = CombatController.getCombatStrength(unit, true);
            if (tile.getCity().getHitPoint()-strength <= 0) return false;
        }
                                            
        return true;
    }

    public static ArrayList<UnitAction> getPossibleActions(Unit unit){
        ArrayList<UnitAction> possibleActions = new ArrayList<>();
        if (unit instanceof Settler && SettlerController.checkTileToFoundCity((Settler)unit) == UnitMessage.SUCCESS) 
            possibleActions.add(UnitAction.FOUND_CITY);
        if (unit instanceof Military && unit.getUnitState() != UnitState.FORTIFYING) 
            possibleActions.add(UnitAction.FORTIFY);
        if (unit instanceof Siege && unit.getUnitState() != UnitState.PREPARED) 
            possibleActions.add(UnitAction.PREPARE);
        if (unit.getUnitState() != UnitState.SLEPT)
            possibleActions.add(UnitAction.SLEEP);
        if (unit.getUnitState() == UnitState.ALERT || unit.getUnitState() == UnitState.SLEPT)
            possibleActions.add(UnitAction.WAKE);
        if (unit.getUnitState() != UnitState.ALERT)
            possibleActions.add(UnitAction.ALERT);
        if (unit.getUnitState() != UnitState.HEALING && unit.getHealth() < 10)
            possibleActions.add(UnitAction.HEAL);
        if (unit instanceof Military &&
            unit.getMovePoint() > 0 &&
            unit.getTile().getImprovement() != null &&
            unit.getTile().getCivilization() != unit.getCivilization())
            possibleActions.add(UnitAction.PILLAGE);
        if (unit.getUnitState() != UnitState.FREE)
            possibleActions.add(UnitAction.FREE);
        possibleActions.add(UnitAction.INFO);
        possibleActions.add(UnitAction.DELETE);
        return possibleActions;
    }
}

class MapPair {
    private boolean checked;
    private int movePoint;
    MapPair() {
        checked = false;
        movePoint = -1;
    }

    public boolean isChecked() {
        return checked;
    }

    public int getMovePoint() {
        return movePoint;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setMovePoint(int movePoint) {
        this.movePoint = movePoint;
    }
}
