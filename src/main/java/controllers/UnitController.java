package controllers;

import models.Game;
import models.tiles.Tile;
import models.tiles.enums.Direction;
import models.units.Civilian;
import models.units.Military;
import models.units.Unit;
import models.units.enums.UnitState;
import views.enums.Message;

import java.util.HashMap;
import java.util.Set;

public class UnitController {
    public static Message selectCombatUnit(int i, int j) {
        Game game = GameMenuController.getGame();
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return Message.INVALID_POSITION;
        Tile tile = game.getMap()[i][j];
        if (tile.getMilitary() == null) return Message.NO_COMBAT_UNIT;
        game.setSelectedUnit(tile.getMilitary());
        return Message.SUCCESS;
    }

    public static Message selectNonCombatUnit(int i, int j) {
        Game game = GameMenuController.getGame();
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return Message.INVALID_POSITION;
        Tile tile = game.getMap()[i][j];
        if (tile.getCivilian() == null) return Message.NO_NONCOMBAT_UNIT;
        game.setSelectedUnit(tile.getMilitary());
        return Message.SUCCESS;
    }

    public static Message moveToTarget(int i, int j) {
        Game game = GameMenuController.getGame();
        if (i < 0 || i >= game.MAP_HEIGHT || j < 0 || j >= game.MAP_WIDTH)
            return Message.INVALID_POSITION;
        if (game.getSelectedUnit() == null) return Message.NO_SELECTED_UNIT;
        Unit unit = game.getSelectedUnit();
        Tile startTile = unit.getTile();
        Tile targetTile = game.getMap()[i][j];
        if (!unit.getCivilization().equals(game.getTurn())) return Message.NO_PERMISSION;
        if (startTile.getCoordinates()[0] == i && startTile.getCoordinates()[1] == j) return Message.SAME_TILE;
        if (!targetTile.isAccessible()) return Message.NOT_ACCESSIBLE_TILE;
        if (isFullTargetTile(unit, targetTile)) return Message.FULL_TILE;
        // TODO: Add enemy border check

        moveUnit(unit, startTile, targetTile);
        return Message.SUCCESS;
    }

    private static boolean isFullTargetTile(Unit unit, Tile targetTile) {
        if (unit instanceof Military && targetTile.getMilitary() != null) return true;
        if (unit instanceof Civilian && targetTile.getCivilian() != null) return true;
        return false;
    }

    private static void moveUnit(Unit unit, Tile startTile, Tile targetTile) {
        Game game = GameMenuController.getGame();
        MapPair[][] checkMap = new MapPair[game.MAP_HEIGHT][game.MAP_WIDTH];
        for (int i = 0; i < game.MAP_HEIGHT; i++) {
            for (int j = 0; j < game.MAP_WIDTH; j++) {
                checkMap[i][j] = new MapPair();
            }
        }

        int[] startCoordinates = startTile.getCoordinates();
        int startI = startCoordinates[0];
        int startJ = startCoordinates[1];
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
            int i = targetTile.getCoordinates()[0];
            int j = targetTile.getCoordinates()[1];
            directMove(i, j, checkMap);
        }

    }
    private static boolean isValidDirection(int i, int j, Direction direction, MapPair[][] checkMap) {
        Game game = GameMenuController.getGame();
        if (i + direction.i < game.MAP_HEIGHT && i + direction.i >= 0 &&
            j + direction.j < game.MAP_WIDTH && j + direction.j >= 0 &&
            !checkMap[i + direction.i][j + direction.j].isChecked()) {
            return true;
        }
        return false;
    }

    private static int getDirectionMovePoint(int i, int j, Direction direction, int currentMovePoint) {
        Tile[][] map = GameMenuController.getGame().getMap();
        Tile tile = map[i][j];
        Tile nextTile = map[i + direction.i][j + direction.j];
        if (tile.getRivers().contains(direction)) return currentMovePoint;
        int terrainMP = nextTile.getTerrain().getMovementCost();
        int terrainFeatureMP = nextTile.getTerrainFeature().getMovementCost();
        return Math.max(terrainMP, terrainFeatureMP);
    }

    private static void tagMapMovePoints(int i, int j, int movePoint, MapPair[][] checkMap) {
        if (movePoint < checkMap[i][j].getMovePoint()) checkMap[i][j].setMovePoint(movePoint);
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
        Game game = GameMenuController.getGame();
        HashMap<Integer[], Integer> result = new HashMap<>();
        int targetI = targetTile.getCoordinates()[0];
        int targetJ = targetTile.getCoordinates()[1];

        for (int i = 0; i < game.MAP_HEIGHT; i++) {
            for (int j = 0; j < game.MAP_WIDTH; j++) {
                if (checkMap[i][j].getMovePoint() < currentMovePoint) {
                    int r = (int)(Math.sqrt(Math.pow(i - targetI, 2) + Math.pow(j - targetJ, 2)) + 0.5);
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

    private static void directMove(int i, int j, MapPair[][] checkMap) {
        Tile[][] map = GameMenuController.getGame().getMap();
        Unit unit = GameMenuController.getGame().getSelectedUnit();
        Tile startTile = unit.getTile();
        Tile targetTile = map[i][j];

        int newMovePoint = checkMap[i][j].getMovePoint();
        unit.setMovePoint(newMovePoint);
        unit.setTile(targetTile);
        unit.setUnitState(UnitState.FREE);

        startTile.freeUnit(unit);
        targetTile.addUnit(unit);
    }
}





class MapPair {
    private boolean checked;
    private int movePoint;
    MapPair() {
        checked = false;
        movePoint = 100;
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
