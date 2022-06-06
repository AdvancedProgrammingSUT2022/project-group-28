package views;

import controllers.GameController;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import views.components.Hex;

public class GamePage extends PageController{
    @FXML
    private Pane pane;

    private int offsetI=0,offsetJ=0;


    @FXML
    private void initialize() {
        createMap(30, 30);
        
    }

    private void createMap(int baseI, int baseJ) {
        this.pane.getChildren().clear();
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < 100; i++) {
                //System.out.println(i + "," + j);
                int tileI = baseI + i - 50;
                int tileJ = baseJ + j - i/2 - 50;
                double hexX = tileJ * 173.2 + tileI * 86.6 - baseJ * 230 + offsetI;
                double hexY = tileI * 150 - baseI * 135 + offsetJ;
                //System.out.println(tileI + "," + tileJ);
                //System.out.println(hexX + "," + hexY);
                if(hexX<-100 || hexX>=1700 || hexY<-100 || hexY>=1000)
                    continue;
                Hex hex = new Hex(GameController.getGame().getMap()[tileI][tileJ], hexX, hexY);
                this.pane.getChildren().add(hex);
            }
        }
    }

    @FXML
    private void keyPressed(KeyEvent keyEvent){
        // TODO:cant move out of page
        if(keyEvent.getCode().getName().equals("Up")){
            offsetJ-=5;
        } else if(keyEvent.getCode().getName().equals("Down")){
            offsetJ+=5;
        } else if(keyEvent.getCode().getName().equals("Left")){
            offsetI-=5;
        } else if(keyEvent.getCode().getName().equals("Right")){
            offsetI+=5;
        }
        createMap(30, 30);
    }
}
