package views;

import controllers.GameController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import views.components.Hex;

public class GamePage extends PageController{
    @FXML
    private AnchorPane pane;
    @FXML
    private Rectangle right, left, top, bottom;

    private boolean rightKey = false, leftKey = false, topKey = false, bottomKey = false;
    private boolean rightMouse = false, leftMouse = false, topMouse = false, bottomMouse = false;

    private int offsetI=0,offsetJ=0;


    @FXML
    private void initialize() {
        //createMap(30, 30);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    private void createMap(int baseI, int baseJ) {
        this.pane.getChildren().clear();
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < 100; i++) {
                int tileI = baseI + i - 50;
                int tileJ = baseJ + j - i/2 - 50;
                double hexX = tileJ * 173.2 + tileI * 86.6 - baseJ * 230 + offsetI;
                double hexY = tileI * 150 - baseI * 135 + offsetJ;
                if(hexX<-100 || hexX>=1700 || hexY<-100 || hexY>=1000)
                    continue;
                Hex hex = new Hex(GameController.getGame().getMap()[tileI][tileJ], hexX, hexY);
                hex.setMouseTransparent(true);
                this.pane.getChildren().add(hex);
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
    private void rightEntered(MouseEvent mouseEvent){
        rightMouse = true;
    }   

    @FXML
    private void mouseMoved(MouseEvent mouseEvent){
        leftMouse = (mouseEvent.getX()>20)?false:true;
        topMouse = (mouseEvent.getY()>20)?false:true;
        bottomMouse = (mouseEvent.getY()<880)?false:true;
        rightMouse = (mouseEvent.getX()<1580)?false:true;
    }

    private void update(){
        if(rightKey || rightMouse){
            offsetI-=5;
        }
        if(leftKey || leftMouse){
            offsetI+=5;
        }
        if(topKey || topMouse){
            offsetJ+=5;
        }
        if(bottomKey || bottomMouse){
            offsetJ-=5;
        }
        createMap(30,30);
    }
}
