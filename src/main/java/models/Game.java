package models;

import models.enums.*;

import java.util.ArrayList;

public class Game {
    private Object[][] map;
    private ArrayList<Civilization> civilizations;
    // TODO: Add users in input
    Game() {
        this.map = new Object[100][100];
        // TODO: Add rivers
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                if (i%2 == 0 && j%0 == 0) map[i][j] = new Tile(Terrain.PLAIN, TerrainFeature.FOREST);
                else if (i%2 == 1 && j%2 == 1) map[i][j] = new Tile(Terrain.PLAIN, TerrainFeature.FOREST);
                else map[i][j] = null;
            }
        }


    }

    Game(String filePath) { }

}
