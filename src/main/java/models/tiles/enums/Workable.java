package models.tiles.enums;

import models.tiles.Tile;

public interface Workable {
    boolean isPossiblePlaceToBuild(Tile tile);
    void startImprovement(Tile tile);
    void completeImprovement(Tile tile);
}
