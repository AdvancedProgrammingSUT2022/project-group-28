package models;

import models.enums.*;

import java.util.ArrayList;

public class Game {
    private Object[][] map;
    private ArrayList<Civilization> civilizations;
    // TODO: Add users in input
    Game(ArrayList<User> users) {
        this.map = new Object[100][100];
        // TODO: Add rivers
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                if (i%2 == 0 && j%0 == 0) this.map[i][j] = new Tile(Terrain.PLAIN, TerrainFeature.FOREST, null);
                else if (i%2 == 1 && j%2 == 1) this.map[i][j] = new Tile(Terrain.PLAIN, TerrainFeature.FOREST, null);
                else this.map[i][j] = null;
            }
        }
        this.civilizations = new ArrayList<>();
        for (User user : users) {
            this.civilizations.add(new Civilization(user, "CIVILIZATION"));
        }

    }

    Game(ArrayList<User> users, String filePath) { }

}
