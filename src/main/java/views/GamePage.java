package views;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import views.components.Hex;

public class GamePage extends PageController{
    @FXML
    private Pane pane;


    @FXML
    private void initialize() {

        createMap(30, 30);
    }

    private void createMap(int baseI, int baseJ) {
        this.pane.getChildren().clear();
        for (int j = 0; j < 14; j++) {
            for (int i = 0; i < 7; i++) {
                System.out.println(i + "," + j);
                int tileI = baseI + i - 3;
                int tileJ = baseJ + j - i/2 - 7;
                double hexX = tileJ * 173.2 + tileI * 86.6 - baseJ * 230;
                double hexY = tileI * 150 - baseI * 135;
                System.out.println(tileI + "," + tileJ);
                System.out.println(hexX + "," + hexY);
                Hex hex = new Hex(GameController.getGame().getMap()[tileI][tileJ], hexX, hexY);
                this.pane.getChildren().add(hex);
            }
        }
    }
}
