package views.components;

import controllers.CityController;
import controllers.CombatController;
import controllers.GameController;
import controllers.units.UnitController;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;
import models.civilization.City;
import models.civilization.Civilization;
import models.tiles.TerrainOrTerrainFeature;
import models.tiles.Tile;
import models.tiles.enums.*;
import models.units.Unit;
import views.App;
import views.GameMediator;
import views.GamePage;
import views.HUDController;
import views.GamePage.MapState;

import java.util.HashMap;

public class Hex extends Group {
    private static final HashMap<TerrainOrTerrainFeature, ImagePattern> terrainImages = new HashMap<>();
    private static final HashMap<ImprovementTemplate, Image> improvementImages = new HashMap<>();
    private static final HashMap<ImprovementTemplate, Image> projectImages= new HashMap<>();
    private static final HashMap<ResourceTemplate, ImagePattern> resourceImages = new HashMap<>();
    
    private static final Image fog = new Image(App.class.getResource("../assets/image/terrain/fog.png").toExternalForm());

    private TileInfo tileInfo = new TileInfo();
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
                Image improvement = new Image(App.class.getResource("../assets/image/improvement/" + improvementTemplate.getFilename() + ".png").toExternalForm());
                improvementImages.put(improvementTemplate, improvement);
            }

            if (improvementTemplate.getIconName() != null) {
                Image project = new Image(App.class.getResource("../assets/image/project/" + improvementTemplate.getIconName() + ".png").toExternalForm());
                projectImages.put(improvementTemplate, project);
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
    private Shape border;
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
            this.shadow = createShadow(Color.color(0,0, 0, 0.5));
            this.getChildren().add(this.shadow);
        }
        
        if (tile.getCivilization() != null) {
            this.border = createBorder(5);
            this.border.setFill(tile.getCivilization().getCivilizationNames().getColor());
            this.getChildren().add(this.border);
        }
        
        if (tile.getCity() != null) {
            this.border = createBorder(5);
            this.border.setFill(tile.getCity().getCivilization().getCivilizationNames().getColor());
            this.getChildren().add(this.border);
        }

        //shows tile information when you hover on tile
        Animation delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            tileInfo.update(this);
            GamePage.getInstance().createTileInfo(tileInfo);
        });
        //this.addEventHandler(MouseEvent.MOUSE_ENTERED , e -> {
        //    tileInfo.setLayoutX(e.getSceneX());
        //    tileInfo.setLayoutY(e.getSceneY());
        //    delay.playFromStart();
        //});
//
        //this.addEventHandler(MouseEvent.MOUSE_EXITED , e -> {
        //    GamePage.getInstance().deleteTileInfo(tileInfo);
        //    delay.stop();
        //});


        // TODO: reform these hover borders
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Hex.this.hoverBorder = createBorder(8);
                Hex.this.getChildren().add(Hex.this.hoverBorder);
                tileInfo.setLayoutX(event.getSceneX());
                tileInfo.setLayoutY(event.getSceneY());
                delay.playFromStart();
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Hex.this.getChildren().contains(Hex.this.hoverBorder)){
                    Hex.this.getChildren().remove(Hex.this.hoverBorder);
                    GamePage.getInstance().deleteTileInfo(tileInfo);
                    delay.stop();
                }
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
            
            this.civilian.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Civilization civilization = GameController.getGame().getCurrentPlayer();
                    if (Hex.this.tile.getCivilian().getCivilization().equals(civilization)) {
                        GameController.getGame().setSelectedUnit(Hex.this.tile.getCivilian());
                        HUDController.getInstance().getUnitInfo().update();
                        GamePage.getInstance().setMapState(MapState.UNIT_SELECTED);
                        GamePage.getInstance().createMap(true);
                    }
                }
            });

            this.getChildren().add(this.civilian);
        }

        if (this.tile.getMilitary() != null) {
            this.military = new UnitIcon(this.tile.getMilitary());
            this.military.setLayoutX(-95);
            this.military.setLayoutY(55);

            this.military.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Civilization civilization = GameController.getGame().getCurrentPlayer();
                    if (Hex.this.tile.getMilitary().getCivilization().equals(civilization)) {
                        GameController.getGame().setSelectedUnit(Hex.this.tile.getMilitary());
                        HUDController.getInstance().getUnitInfo().update();
                        GamePage.getInstance().setMapState(MapState.UNIT_SELECTED);
                        GamePage.getInstance().createMap(true);
                    }
                }
            });

            this.getChildren().add(this.military);
        }

        if (this.tile.getProject() != null) {
            Image image = projectImages.get(this.tile.getProject().getImprovement());
            Circle project = new Circle(30);
            project.setFill(new ImagePattern(image));
            project.setLayoutY(35);
            this.getChildren().add(project);

            double progress = (double)  this.tile.getProject().getSpentTurns() / this.tile.getProject().getImprovement().getTurnCost();
            ProgressBar progressBar = new ProgressBar(progress);
            progressBar.setLayoutX(8);
            progressBar.setLayoutY(25);
            progressBar.getStyleClass().add("project_bar");
            progressBar.setMaxWidth(50);
            progressBar.setRotate(-90);
            this.getChildren().add(progressBar);
        }
        
        if (this.tile.getImprovement() != null) {
            Image image = improvementImages.get(this.tile.getImprovement());
            ImageView improvement = new ImageView(image);
            improvement.setLayoutX(-image.getWidth()/2);
            improvement.setLayoutY(35);
            this.getChildren().add(improvement);
        }
        
        if (this.tile.getResource() != null) {
            Circle resource = new Circle(35);
            resource.setLayoutX(90);
            resource.setLayoutY(30);
            resource.setFill(resourceImages.get(this.tile.getResource().getResourceTemplate()));
            this.getChildren().add(resource);
        }

        GamePage.MapState mapState = GamePage.getInstance().getMapState();
        City city = GameController.getGame().getSelectedCity();
        Unit unit = GameController.getGame().getSelectedUnit();

        if (mapState == GamePage.MapState.ASSIGN_CITIZEN && city != null) {
            if (city.getTiles().contains(this.tile)) {
                Circle citizen = new Circle(30);
                int i = this.tile.getCoordinates()[0];
                int j = this.tile.getCoordinates()[1];
                if (this.tile.isWorking()) {
                    citizen.getStyleClass().add("working_citizen");
                    citizen.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            GameMediator.getInstance().tryFreeCitizen(i, j);
                        }
                    });
                } else {
                    citizen.getStyleClass().add("not_working_citizen");
                    citizen.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            GameMediator.getInstance().tryAssignCitizen(i, j);
                        }
                    });
                }

                this.getChildren().add(citizen);
            }
        }

        if (mapState == GamePage.MapState.BUY_TILE && city != null) {
            if (CityController.isAvailableToBuy(city, this.tile)) {
                Group group = new Group();
                
                group.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        GameMediator.getInstance().tryBuyTile(city, Hex.this.tile);
                    }
                });

                Circle toBuy = new Circle(30);
                toBuy.getStyleClass().add("to_buy_tile");
                group.getChildren().add(toBuy);

                Text cost = new Text(String.valueOf(CityController.getTileValue(city, this.tile)));
                cost.setLayoutX(-10);
                cost.setLayoutY(10);
                cost.getStyleClass().add("tile_cost");
                group.getChildren().add(cost);
                
                this.getChildren().add(group);
            }
        }
        
        if (mapState == GamePage.MapState.CITY_ATTACK && city != null) {
            if (CombatController.isCityAttackPossible(tile)) {
                this.shadow = createShadow(Color.color(.60, .0, .0, .5));
                this.getChildren().add(this.shadow);
                this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(!event.getButton().equals(MouseButton.PRIMARY))
                        return;
                        
                        GameMediator.getInstance().cityAttack(Hex.this.tile);
                        GamePage.getInstance().updateGamePage();
                    }
                });
            }
        }
        
        if (mapState == GamePage.MapState.UNIT_SELECTED && unit != null) {
            if (UnitController.isTileAccessible(this.tile,unit)) {

                this.shadow = createShadow(Color.color(.0, .0, .50, .3));
                this.getChildren().add(this.shadow);
                this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(!event.getButton().equals(MouseButton.PRIMARY))
                            return;

                        GameMediator.getInstance().moveUnit(unit, Hex.this.tile);
                        GamePage.getInstance().updateGamePage();
                    }
                });
            }
            
            if (UnitController.isTileAttackable(this.tile,unit)) {

                this.shadow = createShadow(Color.color(.60, .0, .0, .5));
                this.getChildren().add(this.shadow);
                this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(!event.getButton().equals(MouseButton.PRIMARY))
                        return;
                        
                        GameMediator.getInstance().attack(unit, Hex.this.tile);
                        GamePage.getInstance().updateGamePage();
                    }
                });
            }
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

    private Polygon createShadow(Color color) {
        Polygon shadow = new Polygon();

        Double[] points = new Double[12];
        for (int i = 0; i < 6; i++) {
            points[i * 2] = -Math.sin(Math.PI/3 * i) * this.RADIUS;
            points[i * 2 + 1] = Math.cos(Math.PI/3 * i) * this.RADIUS;
        }

        shadow.getPoints().addAll(points);
        
        shadow.setFill(color);

        return shadow;
    }

    private Shape createBorder(int width) {
        Polygon out = new Polygon();
        Polygon in = new Polygon();
        
        Double[] outPoints = new Double[12];
        Double[] inPoints = new Double[12];
        
        for (int i = 0; i < 6; i++) {
            outPoints[i * 2] = -Math.sin(Math.PI/3 * i) * (this.RADIUS - 1);
            outPoints[i * 2 + 1] = Math.cos(Math.PI/3 * i) * (this.RADIUS - 1);
            inPoints[i * 2] = -Math.sin(Math.PI/3 * i) * (this.RADIUS - 1 - width);
            inPoints[i * 2 + 1] = Math.cos(Math.PI/3 * i) * (this.RADIUS - 1 - width);
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

    public Tile getTile() {
        return tile;
    }

    public static HashMap<ResourceTemplate, ImagePattern> getResourceimages() {
        return resourceImages;
    }
    
}
