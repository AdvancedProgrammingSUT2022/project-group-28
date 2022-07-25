package views.components;

import controllers.CivilizationController;
import controllers.GameController;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import models.civilization.Civilization;
import models.tiles.Tile;
import views.App;
import views.GamePage;

public class MiniMap extends Group {
    private final double RADIUS;

    private Civilization civilization;
    private Shape cameraFocus;
    private Group map;
    public MiniMap() {
        this.civilization = App.getCurrentUserCivilization();

        this.RADIUS = calculateRadius();

        Rectangle background = createBackground();
        this.getChildren().add(background);

        cameraFocus = createCameraFocus();
        this.getChildren().add(cameraFocus);

        map = new Group();
        updateMap();
        this.getChildren().add(map);

        Rectangle clickArea = createClickArea();
        this.getChildren().add(clickArea);
    }


    public void updateMap() {
        map.getChildren().clear();
        CivilizationController.updateDiscoveredTiles();
        this.civilization = App.getCurrentUserCivilization();
        for (Tile tile : civilization.getDiscoveredTiles().keySet()) {
            Polygon polygon = new Polygon();
            setPolygonPoints(polygon);

            switch (tile.getTerrain()) {
                case DESERT:
                    polygon.setFill(Color.YELLOW);
                    break;
                case GRASSLAND:
                    polygon.setFill(Color.GREEN);
                    break;
                case HILL:
                    polygon.setFill(Color.YELLOWGREEN);
                    break;
                case MOUNTAIN:
                    polygon.setFill(Color.GRAY);
                    break;
                case OCEAN:
                    polygon.setFill(Color.BLUE);
                    break;
                case PLAIN:
                    polygon.setFill(Color.DARKSEAGREEN);
                    break;
                case SNOW:
                    polygon.setFill(Color.WHITE);
                    break;
                case TUNDRA:
                    polygon.setFill(Color.WHEAT);
                    break;
                default:
                    break;
            }

            int tileI = tile.getCoordinates()[0];
            int tileJ = tile.getCoordinates()[1];

            polygon.setLayoutY(tileI * 3 * MiniMap.this.RADIUS / 4);
            polygon.setLayoutX(tileJ * Math.sqrt(3) * MiniMap.this.RADIUS / 2 + polygon.getLayoutY()/Math.sqrt(3));
            map.getChildren().add(polygon);
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


    private Rectangle createBackground() {
        Rectangle blackBackground = new Rectangle(350, 200);
        blackBackground.setFill(Color.BLACK);

        return blackBackground;
    }

    private double calculateRadius() {
        return 800 / (3 * GameController.getGame().MAP_HEIGHT + 1);
    }

    private Rectangle createClickArea() {
        Rectangle clickArea = new Rectangle(350, 200);
        clickArea.setFill(Color.TRANSPARENT);

        clickArea.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int tileI = (int)((event.getY()) * 2 / (3 * (MiniMap.this.RADIUS/2))) -1;
                int tileJ = (int)((event.getX()- (int)(event.getY()/Math.sqrt(3))) / (Math.sqrt(3) * (MiniMap.this.RADIUS/2))) -1 ;
                GamePage.getInstance().setBaseI(tileI);
                GamePage.getInstance().setBaseJ(tileJ);
                GamePage.getInstance().setOffsetI(0);
                GamePage.getInstance().setOffsetJ(0);
                GamePage.getInstance().createMap(true);
            }
        });

        return clickArea;
    }

    private Shape createCameraFocus() {
        Rectangle innerRectangle = new Rectangle(2.5, 2.5, 80, 45);
        Rectangle border = new Rectangle(0, 0, 85, 50);
        Shape cameraFocus = Shape.subtract(border, innerRectangle);
        cameraFocus.setFill(Color.WHITE);

        // TODO: find a better way
        AnimationTimer cameraFocusUpdater = new AnimationTimer() {
            @Override
            public void handle(long now) {
                int baseI = GamePage.getInstance().getBaseI();
                int baseJ = GamePage.getInstance().getBaseJ();
                int offsetI = GamePage.getInstance().getOffsetI();
                int offsetJ = GamePage.getInstance().getOffsetJ();


                cameraFocus.setLayoutY(baseI * 3 * MiniMap.this.RADIUS / 4  - offsetJ * 0.00682 - 22.5);
                cameraFocus.setLayoutX(baseJ * Math.sqrt(3) * MiniMap.this.RADIUS / 2 + cameraFocus.getLayoutY()/Math.sqrt(3) - offsetI * 0.006797 - 40);

            }
        };
        cameraFocusUpdater.start();

        return cameraFocus;
    }

}
