package views.components;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import models.tiles.TerrainOrTerrainFeature;
import models.tiles.Tile;
import models.tiles.enums.Terrain;
import models.tiles.enums.TerrainFeature;
import views.App;

import java.util.HashMap;

public class Hex extends Group {
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

    // Components
    private Polygon mainPolygon;
    private Text banner;

    public Hex(Tile tile, Double x, Double y) {
        this.tile = tile;
        this.setLayoutX(x);
        this.setLayoutY(y);

        this.mainPolygon = new Polygon();
        setPolygonPoints(this.mainPolygon);
        updateTerrainPicture();

        this.getChildren().add(this.mainPolygon);

        this.banner = new Text(tile.getCoordinates()[0] + "," + tile.getCoordinates()[1]);
        this.getChildren().add(this.banner);
    }

    private void setPolygonPoints(Polygon polygon) {
        Double[] points = new Double[12];

        for (int i = 0; i < 6; i++) {
            points[i * 2] = -Math.sin(Math.PI/3 * i) * this.RADIUS;
            points[i * 2 + 1] = Math.cos(Math.PI/3 * i) * this.RADIUS;
        }
        polygon.getPoints().addAll(points);
    }

    private void updateTerrainPicture() {
        if (this.tile.getTerrainFeature() != null) {
            this.mainPolygon.setFill(terrainImages.get(this.tile.getTerrainFeature()));
        } else this.mainPolygon.setFill(terrainImages.get(this.tile.getTerrain()));
    }

    public void move(Double x, Double y) {
//        this.center[0] = this.center[0] + x;
//        this.center[1] = this.center[1] + y;
//        for (int i = 0; i < 12; i++) {
//            Double temp = this.getPoints().get(i);
//            this.getPoints().remove(i);
//            if (i%2 == 0) this.getPoints().add(i, temp + x);
//            else this.getPoints().add(i, temp + y);
//        }
        this.setLayoutX(this.getLayoutX() + x);
        this.setLayoutY(this.getLayoutY() + y);

    }

}
