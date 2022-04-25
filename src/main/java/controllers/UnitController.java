package controllers;

import models.tiles.Tile;
import models.tiles.enums.Direction;
import models.units.Civilian;
import models.units.Military;
import models.units.Unit;
import models.units.enums.UnitState;
import views.enums.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class UnitController extends GameController {

    public static Message selectCombatUnit(int i, int j) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return Message.INVALID_POSITION;
        Tile tile = game.getMap()[i][j];
        if (tile.getMilitary() == null) return Message.NO_COMBAT_UNIT;
        game.setSelectedUnit(tile.getMilitary());
        return Message.SUCCESS;
    }

    public static Message selectNonCombatUnit(int i, int j) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return Message.INVALID_POSITION;
        Tile tile = game.getMap()[i][j];
        if (tile.getCivilian() == null) return Message.NO_NONCOMBAT_UNIT;
        game.setSelectedUnit(tile.getCivilian());
        return Message.SUCCESS;
    }

    // TODO: Reform move methods
    public static Message moveUnitToTarget(int i, int j) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return Message.INVALID_POSITION;
        if (game.getSelectedUnit() == null) return Message.NO_SELECTED_UNIT;
        Unit unit = game.getSelectedUnit();
        Tile startTile = unit.getTile();
        Tile targetTile = game.getMap()[i][j];
        if (!unit.getCivilization().equals(game.getCurrentPlayer())) return Message.NO_PERMISSION;
        if (startTile.getCoordinates()[0] == i && startTile.getCoordinates()[1] == j) return Message.SAME_TILE;
        if (!targetTile.isAccessible()) return Message.NOT_ACCESSIBLE_TILE;
        if (isFullTile(unit, targetTile)) return Message.FULL_TILE;
        // TODO: Add enemy border check

        moveUnit(unit, startTile, targetTile);
        return Message.SUCCESS;
    }

    public static void nextTurnUnitUpdates() {
        ArrayList<Unit> units = game.getCurrentPlayer().getUnits();
        for (Unit unit : units) {
            if (unit.getUnitState() == UnitState.MOVING &&
                unit.getMovePoint() == unit.getUnitTemplate().getMovementPoint()) {
                moveMovingUnit(unit);
            }
            unit.setMovePoint(unit.getUnitTemplate().getMovementPoint());
            // TODO: check all states
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
        checkMap[startI][startJ].setMovePoint(currentMovePoint);

        for (Direction direction: Direction.getDirections()) {
            if (isValidDirection(startI, startJ, direction, checkMap)) {
                int newMovePoint = currentMovePoint - getDirectionMovePoint(startI, startJ, direction, currentMovePoint);
                if (newMovePoint >= 0) {
                    tagMapMovePoints(startI + direction.i, startJ + direction.j, newMovePoint, checkMap);
                }
            }
        }

        HashMap<Integer[], Integer> distances = getDistances(targetTile, checkMap, currentMovePoint);

        if (isDirectMovePossible(distances)) {
            directMove(targetTile, checkMap);
        } else {
            Tile bestTile = getBestTile(distances);
            if (bestTile != null && !bestTile.equals(startTile)) indirectMove(bestTile, targetTile, checkMap);
            else {
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
        if (tile.getRivers().contains(direction)) return currentMovePoint;
        int terrainMP = nextTile.getTerrain().getMovementCost();
        int terrainFeatureMP = 0;
        if (nextTile.getTerrainFeature() != null) {
            terrainFeatureMP = nextTile.getTerrainFeature().getMovementCost();
        }
        return Math.max(terrainMP, terrainFeatureMP);
    }

    private static void tagMapMovePoints(int i, int j, int movePoint, MapPair[][] checkMap) {
        if (movePoint > checkMap[i][j].getMovePoint()) checkMap[i][j].setMovePoint(movePoint);
        checkMap[i][j].setChecked(true);

        if (movePoint == 0) {
            checkMap[i][j].setChecked(false);
            return;
        }

        for (Direction direction: Direction.getDirections()) {
            if (isValidDirection(i, j, direction, checkMap)) {
                int newMovePoint = movePoint - getDirectionMovePoint(i, j, direction, movePoint);
                if (newMovePoint >= 0) {
                    tagMapMovePoints(i + direction.i, j + direction.j, newMovePoint, checkMap);
                }
            }
        }
        checkMap[i][j].setChecked(false);
    }

    private static HashMap<Integer[],Integer> getDistances(Tile targetTile, MapPair[][] checkMap, int currentMovePoint) {
        HashMap<Integer[], Integer> result = new HashMap<>();
        int targetI = targetTile.getCoordinates()[0];
        int targetJ = targetTile.getCoordinates()[1];

        for (int i = 0; i < game.MAP_HEIGHT; i++) {
            for (int j = 0; j < game.MAP_WIDTH; j++) {
                if (checkMap[i][j].getMovePoint() >= 0) {
                    int r = (int)(Math.sqrt(Math.pow(i - targetI, 2) + Math.pow(j - targetJ, 2)) + 0.5);
                    result.put(new Integer[]{i, j}, r);
//                    System.out.println(i + "-" + j + ": " + r + " == " + checkMap[i][j].getMovePoint());
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
        Tile[][] map = game.getMap();
        Tile startTile = unit.getTile();

        startTile.freeUnit(unit);
        targetTile.addUnit(unit);

        int i = targetTile.getCoordinates()[0];
        int j = targetTile.getCoordinates()[1];

        int newMovePoint = checkMap[i][j].getMovePoint();
        unit.setMovePoint(newMovePoint);
        unit.setTile(targetTile);
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
        checkMap[startI][startJ].setMovePoint(currentMovePoint);
        for (Direction direction: Direction.getDirections()) {
            if (isValidDirection(startI, startJ, direction, checkMap)) {
                int newMovePoint = currentMovePoint - getDirectionMovePoint(startI, startJ, direction, currentMovePoint);
                if (newMovePoint >= 0) {
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
