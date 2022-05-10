package controllers.units;

import controllers.GameController;
import controllers.TileController;
import models.tiles.Tile;
import models.tiles.enums.Direction;
import models.units.Civilian;
import models.units.Military;
import models.units.Unit;
import models.units.Worker;
import models.units.enums.UnitState;
import views.enums.CivilizationMessage;
import views.enums.UnitMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class UnitController extends GameController {

    public static UnitMessage selectCombatUnit(int i, int j) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return UnitMessage.INVALID_POSITION;
        Tile tile = game.getMap()[i][j];
        if (tile.getMilitary() == null) return UnitMessage.NOT_COMBAT_UNIT;
        if (!tile.getMilitary().getCivilization().equals(game.getCurrentPlayer())) return UnitMessage.NO_PERMISSION;
        game.setSelectedUnit(tile.getMilitary());
        return UnitMessage.SUCCESS;
    }

    public static UnitMessage selectNonCombatUnit(int i, int j) {
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return UnitMessage.INVALID_POSITION;
        Tile tile = game.getMap()[i][j];
        if (tile.getCivilian() == null) return UnitMessage.NOT_NONCOMBAT_UNIT;
        if (!tile.getCivilian().getCivilization().equals(game.getCurrentPlayer())) return UnitMessage.NO_PERMISSION;
        game.setSelectedUnit(tile.getCivilian());
        return UnitMessage.SUCCESS;
    }

    // TODO: Reform move methods
    // TODO: check methods to work properly
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

    public static CivilizationMessage checkUnitsForNextTurn(ArrayList<Unit> units) {
        for (Unit unit : units) {
            if (unit.getUnitState() == UnitState.FREE && unit.getMovePoint() > 0) return CivilizationMessage.FREE_UNITS;
            // TODO: check all stuff
        }
        return CivilizationMessage.SUCCESS;
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
                    // TODO: change the conditions
                    if (unit.getMovePoint() == unit.getUnitTemplate().getMovementPoint()) moveMovingUnit(unit);
                    break;
                case WORKING:
                    WorkerController.nextTurnWorkerUpdates((Worker) unit);
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

    // TODO: SUPER CHECK MOVE
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
        checkMap[startI][startJ].setMovePoint(currentMovePoint * 5);

        for (Direction direction: Direction.getDirections()) {
            if (isValidDirection(startI, startJ, direction, checkMap)) {
                int newMovePoint = currentMovePoint * 5 - getDirectionMovePoint(startI, startJ, direction, 5 * currentMovePoint);
                if (newMovePoint >= 0 || (currentMovePoint >= 1 && currentMovePoint <= 5)) {
                    tagMapMovePoints(startI + direction.i, startJ + direction.j, newMovePoint, checkMap);
                }
            }
        }

        HashMap<Integer[], Integer> distances = getDistances(targetTile, checkMap, currentMovePoint);

        for (int i = -4; i < 5; i++) {
            for (int j = -4; j < 5; j++) {
                System.out.printf("%2d ", checkMap[i + startI][j + startJ].getMovePoint());
            }
            System.out.println();
        }

//        for (Integer[] integers : distances.keySet()) {
//            System.out.println(integers[0] + " " + integers[1] + ": " + distances.get(integers));
//        }

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
        // TODO: check next tile is in border
        Tile nextTile = map[i + direction.i][j + direction.j];
        if (nextTile.getCity() != null || nextTile.isRoadConstructed()) return 1;
        if (tile.getRivers().contains(direction)) return currentMovePoint;
        int terrainMP = nextTile.getTerrain().getMovementCost();
        int terrainFeatureMP = 0;
        if (nextTile.getTerrainFeature() != null) {
            terrainFeatureMP = nextTile.getTerrainFeature().getMovementCost();
        }
        return 5 * Math.max(terrainMP, terrainFeatureMP);
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
                if (newMovePoint >= 0 || (movePoint >= 1 && movePoint <= 5)) {
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
        unit.setMovePoint(newMovePoint / 5);
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
        checkMap[startI][startJ].setMovePoint(currentMovePoint * 5);
        for (Direction direction: Direction.getDirections()) {
            if (isValidDirection(startI, startJ, direction, checkMap)) {
                int newMovePoint = currentMovePoint * 5 - getDirectionMovePoint(startI, startJ, direction, 5 * currentMovePoint);
                if (newMovePoint >= 0 || (currentMovePoint >= 1 && currentMovePoint <= 5)) {
                    tagMapMovePoints(startI + direction.i, startJ + direction.j, newMovePoint, checkMap);
                }
            }
        }

        for (int i = -4; i < 5; i++) {
            for (int j = -4; j < 5; j++) {
                System.out.printf("%2d ", checkMap[i + startI][j + startJ].getMovePoint());
            }
            System.out.println();
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
