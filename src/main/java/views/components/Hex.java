package views.components;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import models.tiles.TerrainOrTerrainFeature;
import models.tiles.Tile;
import models.tiles.enums.*;
import views.App;

import java.util.HashMap;

public class Hex extends Group {
    private static final HashMap<TerrainOrTerrainFeature, ImagePattern> terrainImages = new HashMap<>();
    private static final HashMap<ImprovementTemplate, Image> improvementImages = new HashMap<>();
    private static final HashMap<ResourceTemplate, ImagePattern> resourceImages = new HashMap<>();
    private static final Image fog = new Image(App.class.getResource("../assets/image/terrain/fog.png").toExternalForm());

    static {
        for (Terrain terrain : Terrain.values()) {
            Image image = new Image(App.class.getResource("../assets/image/terrain/" + terrain.getFilename() + ".png").toExternalForm());
            terrainImages.put(terrain, new ImagePattern(image));
        }
        for (TerrainFeature terrainFeature : TerrainFeature.values()) {
            Image image = new Image(App.class.getResource("../assets/image/terrain_feature/" + terrainFeature.getFilename() + ".png").toExternalForm());
            terrainImages.put(terrainFeature, new ImagePattern(image));
        }

        for (ImprovementTemplate improvementTemplate : ImprovementTemplate.values()) {
            if (improvementTemplate.getFilename() != null) {
                Image image = new Image(App.class.getResource("../assets/image/improvement/" + improvementTemplate.getFilename() + ".png").toExternalForm());
                improvementImages.put(improvementTemplate, image);
            }
        }

        for (ResourceTemplate resourceTemplate : ResourceTemplate.values()) {
            Image image = new Image(App.class.getResource("../assets/image/resource/" + resourceTemplate.getFilename() + ".png").toExternalForm());
            resourceImages.put(resourceTemplate, new ImagePattern(image));
        }
    }

    private final Double RADIUS = 150d;

    private Tile tile;

    // Components
    private Polygon mainPolygon;
    private Polygon shadow;
    private Shape hoverBorder;
    private Text banner;
    private CityBanner cityBanner;
    private UnitIcon civilian;
    private UnitIcon military;

    public Hex(Tile tile, int discoveryTurn, Double x, Double y) {
        this.tile = tile;

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


        // TODO: reform these hover borders
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Hex.this.hoverBorder = createHoverBorder();
                Hex.this.getChildren().add(Hex.this.hoverBorder);
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Hex.this.getChildren().contains(Hex.this.hoverBorder))
                    Hex.this.getChildren().remove(Hex.this.hoverBorder);
            }
        });

        this.banner = new Text(tile.getCoordinates()[0] + "," + tile.getCoordinates()[1]);
        this.getChildren().add(this.banner);

        addRiver();

        if (this.tile.getCity() != null) {
            this.cityBanner = new CityBanner(this.tile.getCity());
            this.cityBanner.setLayoutX(-100);
            this.cityBanner.setLayoutY(-100);
            this.getChildren().add(this.cityBanner);
        }

        if (this.tile.getCivilian() != null) {
            this.civilian = new UnitIcon(this.tile.getCivilian());
            this.civilian.setLayoutX(-95);
            this.civilian.setLayoutY(0);
            this.getChildren().add(this.civilian);
        }

        if (this.tile.getMilitary() != null) {
            this.military = new UnitIcon(this.tile.getMilitary());
            this.military.setLayoutX(-95);
            this.military.setLayoutY(55);
            this.getChildren().add(this.military);
        }


        if (this.tile.getImprovement() != null) {
            Image image = improvementImages.get(this.tile.getImprovement());
            ImageView improvement = new ImageView(image);
            improvement.setLayoutX(-image.getWidth()/2);
            improvement.setLayoutY(35);
            this.getChildren().add(improvement);
        }
        // TODO: add project icon

        if (this.tile.getResource() != null) {
            Circle resource = new Circle(35);
            resource.setLayoutX(90);
            resource.setLayoutY(30);
            resource.setFill(resourceImages.get(this.tile.getResource().getResourceTemplate()));
            this.getChildren().add(resource);
        }

    }

    public Hex(Double x, Double y,int i,int j) {
        this.setLayoutX(x);
        this.setLayoutY(y);

        this.mainPolygon = new Polygon();
        setPolygonPoints(this.mainPolygon);
        updateTerrainPicture();
        this.getChildren().add(this.mainPolygon);

        this.banner = new Text(i + "," + j);
        this.banner.setFill(Color.color(1,1,1));
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

    private Shape createHoverBorder() {
        Polygon out = new Polygon();
        Polygon in = new Polygon();

        Double[] outPoints = new Double[12];
        Double[] inPoints = new Double[12];

        for (int i = 0; i < 6; i++) {
            outPoints[i * 2] = -Math.sin(Math.PI/3 * i) * (this.RADIUS - 1);
            outPoints[i * 2 + 1] = Math.cos(Math.PI/3 * i) * (this.RADIUS - 1);
            inPoints[i * 2] = -Math.sin(Math.PI/3 * i) * (this.RADIUS - 6);
            inPoints[i * 2 + 1] = Math.cos(Math.PI/3 * i) * (this.RADIUS - 6);
        }

        out.getPoints().addAll(outPoints);
        in.getPoints().addAll(inPoints);

        return Shape.subtract(out, in);
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
        if(this.tile == null){
            this.mainPolygon.setFill(new ImagePattern(fog));
            return;
        }
        if (this.tile.getTerrainFeature() != null) {
            this.mainPolygon.setFill(terrainImages.get(this.tile.getTerrainFeature()));
        } else this.mainPolygon.setFill(terrainImages.get(this.tile.getTerrain()));
    }

    public UnitIcon getCivilian() {
        return civilian;
    }

    public UnitIcon getMilitary() {
        return military;
    }

    public void setCivilian(UnitIcon civilian) {
        this.civilian = civilian;
    }

    public void setMilitary(UnitIcon military) {
        this.military = military;
    }
}
