package views;

import controllers.GameController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import models.tiles.Tile;
import models.tiles.enums.Terrain;
import views.components.Hex;

public class GamePage extends PageController{
    @FXML
    private Pane gameContent;

    private boolean rightKey = false, leftKey = false, topKey = false, bottomKey = false;
    private boolean rightMouse = false, leftMouse = false, topMouse = false, bottomMouse = false;

    private int offsetI=0, offsetJ=0;


    @FXML
    private void initialize() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    private void createMap(int baseI, int baseJ) {
        this.gameContent.getChildren().clear();
        // TODO: add river to map
        for (int j = 0; j < 200; j++) {
            for (int i = 0; i < 200; i++) {
                int tileI = baseI + i - 50;
                int tileJ = baseJ + j - i/2 - 50;
                double hexX = tileJ * 259.8076 + tileI * 129.9038 - baseJ * 360 + offsetI;
                double hexY = tileI * 225 - baseI * 210 + offsetJ;

                if (hexX < -200 || hexX >= 1800 || hexY <- 200 || hexY >= 1100)
                    continue;

                if (tileI >= 0 && tileI < 100 && tileJ >= 0 && tileJ < 100) {
                    Tile tile = GameController.getGame().getMap()[tileI][tileJ];
                    int discoveryTurn = 0; // TODO: change it to real number
                    Hex hex = new Hex(tile, discoveryTurn, hexX, hexY);
                    hex.setMouseTransparent(true);
                    this.gameContent.getChildren().add(hex);
                }
            }
        }
    }

    @FXML
    private void keyPressed(KeyEvent keyEvent){
        // TODO:cant move out of page
        if(keyEvent.getCode().getName().equals("Up")){
            topKey = true;
        } else if(keyEvent.getCode().getName().equals("Down")){
            bottomKey = true;
        } else if(keyEvent.getCode().getName().equals("Left")){
            leftKey = true;
        } else if(keyEvent.getCode().getName().equals("Right")){
            rightKey = true;
        }
    }

    @FXML
    private void keyReleased(KeyEvent keyEvent){
        if(keyEvent.getCode().getName().equals("Up")){
            topKey = false;
        } else if(keyEvent.getCode().getName().equals("Down")){
            bottomKey = false;
        } else if(keyEvent.getCode().getName().equals("Left")){
            leftKey = false;
        } else if(keyEvent.getCode().getName().equals("Right")){
            rightKey = false;
        }
    }

    @FXML
    private void mouseMoved(MouseEvent mouseEvent){
        leftMouse = (mouseEvent.getX()>20)?false:true;
        topMouse = (mouseEvent.getY()>20)?false:true;
        bottomMouse = (mouseEvent.getY()<880)?false:true;
        rightMouse = (mouseEvent.getX()<1580)?false:true;
    }

    private void update() {
        if(rightKey || rightMouse) {
            if (offsetI * 1.72 + 20395 > offsetJ) {
                offsetI -= 5;
            }
        }
        if(leftKey || leftMouse){
            if (offsetI * 1.72 - 7715 < offsetJ) {
                offsetI+=5;
            }
        }
        if(topKey || topMouse) {
            if (offsetJ < 4250) {
                offsetJ += 5;
            }
        }
        if(bottomKey || bottomMouse){
            if (offsetJ > -10350) {
                offsetJ-=5;
            }
        }
        createMap(30,30);
    }
}
