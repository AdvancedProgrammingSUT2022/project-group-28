package views.components;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import models.tiles.TerrainOrTerrainFeature;
import models.tiles.Tile;
import models.tiles.enums.Terrain;
import models.tiles.enums.TerrainFeature;
import views.App;

import java.util.HashMap;

public class Hex extends Polygon {
    private static HashMap<TerrainOrTerrainFeature, ImagePattern> terrainImages = new HashMap<>();
    static {
        for (Terrain terrain : Terrain.values()) {
            Image image = new Image(App.class.getResource("../assets/image/terrain/" + terrain.getFilename() + ".png").toExternalForm());
            terrainImages.put(terrain, new ImagePattern(image));
        }
        for (TerrainFeature terrainFeature : TerrainFeature.values()) {
            Image image = new Image(App.class.getResource("../assets/image/terrain_feature/" + terrainFeature.getFilename() + ".png").toExternalForm());
            terrainImages.put(terrainFeature, new ImagePattern(image));
        }
    }


    private final Double RADIUS = 100d;

    private Tile tile;
    private Double[] center;
    public Hex(Tile tile, Double[] center) {
        this.tile = tile;
        this.center = center;
        setInitialPoints();
        setHexPicture();

    }

    private void setInitialPoints() {
        Double[] points = new Double[12];


        for (int i = 0; i < 6; i++) {
            points[i * 2] = this.center[0] - Math.sin(Math.PI/3 * i) * this.RADIUS;
            points[i * 2 + 1] = this.center[1] + Math.cos(Math.PI/3 * i) * this.RADIUS;
        }
        this.getPoints().addAll(points);
    }

    private void setHexPicture() {
        if (this.tile.getTerrainFeature() != null) {
            this.setFill(terrainImages.get(this.tile.getTerrainFeature()));
        } else this.setFill(terrainImages.get(this.tile.getTerrain()));
    }

}
