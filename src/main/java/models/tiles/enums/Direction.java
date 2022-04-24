package models.tiles.enums;

import java.util.ArrayList;
import java.util.Arrays;

public enum Direction {
    UP(-1, 0),
    UP_RIGHT(-1, 1),
    RIGHT(0, 1),
    DOWN(1, 0),
    DOWN_LEFT(1, -1),
    LEFT(0, -1);

    public final int i;
    public final int j;

    Direction(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public static ArrayList<Direction> getDirections() {
        return new ArrayList<>(Arrays.asList(Direction.values()));
    }
}
