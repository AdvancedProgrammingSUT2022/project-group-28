package views.components;

import controllers.GameController;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.tiles.TerrainOrTerrainFeature;
import models.tiles.Tile;
import models.tiles.enums.Direction;
import models.tiles.enums.Terrain;
import models.tiles.enums.TerrainFeature;
import models.units.Melee;
import models.units.Military;
import models.units.enums.UnitTemplate;
import views.App;

import java.util.Arrays;
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

    private final Double RADIUS = 150d;

    private Tile tile;

    // Components
    private Polygon mainPolygon;
    private Polygon shadow;
    private Text banner;

    public Hex(Tile tile, int discoveryTurn, Double x, Double y) {
        this.tile = tile;
        if (tile == null) {
            return;
        }
        this.setLayoutX(x);
        this.setLayoutY(y);

        this.mainPolygon = new Polygon();
        setPolygonPoints(this.mainPolygon);
        updateTerrainPicture();
        this.getChildren().add(this.mainPolygon);

        if (discoveryTurn != GameController.getGame().getTurnNumber()) {
            this.shadow = createShadow();
            this.getChildren().add(this.shadow);
        }

        this.banner = new Text(tile.getCoordinates()[0] + "," + tile.getCoordinates()[1]);
        this.getChildren().add(this.banner);

        addRiver();

        if (this.tile.getCivilian() != null) {
            UnitIcon civilian = new UnitIcon(this.tile.getCivilian());
            civilian.setLayoutX(-95);
            civilian.setLayoutY(0);
            this.getChildren().add(civilian);
            this.tile.setMilitary(new Melee(this.tile, this.tile.getCivilian().getCivilization(), UnitTemplate.ANTI_TANK));
        }

        if (this.tile.getMilitary() != null) {
            UnitIcon military = new UnitIcon(this.tile.getMilitary());
            military.setLayoutX(-95);
            military.setLayoutY(55);
            this.getChildren().add(military);
        }

    }

    private void setPolygonPoints(Polygon polygon) {
        Double[] points = new Double[12];

        for (int i = 0; i < 6; i++) {
            points[i * 2] = -Math.sin(Math.PI/3 * i) * this.RADIUS;
            points[i * 2 + 1] = Math.cos(Math.PI/3 * i) * this.RADIUS;
        }
        polygon.getPoints().addAll(points);
    }

    private Polygon createShadow() {
        Polygon shadow = new Polygon();

        Double[] points = new Double[12];
        for (int i = 0; i < 6; i++) {
            points[i * 2] = -Math.sin(Math.PI/3 * i) * this.RADIUS;
            points[i * 2 + 1] = Math.cos(Math.PI/3 * i) * this.RADIUS;
        }

        shadow.getPoints().addAll(points);

        shadow.setFill(Color.color(0,0, 0, 0.5));

        return shadow;
    }

    private void addRiver() {
        if (this.tile.getRivers().contains(Direction.LEFT)) {
            Rectangle river = new Rectangle(15, 150);
            river.setLayoutX(-135);
            river.setLayoutY(-75);
            river.setFill(Color.color(0.317, 0.721, 0.941, .9));
            this.getChildren().add(river);
        }

        if (this.tile.getRivers().contains(Direction.UP)) {
            Rectangle river = new Rectangle(15, 150);
            river.setRotate(60);
            river.setLayoutX(-70.5);
            river.setLayoutY(-184.5);
            river.setFill(Color.color(0.317, 0.721, 0.941, .9));
            this.getChildren().add(river);
        }

        if (this.tile.getRivers().contains(Direction.UP_RIGHT)) {
            Rectangle river = new Rectangle(15, 150);
            river.setRotate(120);
            river.setLayoutX(57);
            river.setLayoutY(-187.5);
            river.setFill(Color.color(0.317, 0.721, 0.941, .9));
            this.getChildren().add(river);
        }
    }

    private void updateTerrainPicture() {
        if (this.tile.getTerrainFeature() != null) {
            this.mainPolygon.setFill(terrainImages.get(this.tile.getTerrainFeature()));
        } else this.mainPolygon.setFill(terrainImages.get(this.tile.getTerrain()));
    }


}
