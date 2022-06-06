package views;

import controllers.GameController;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import views.components.Hex;

public class GamePage extends PageController{
    @FXML
    private Pane pane;


    @FXML
    private void initialize() {
        Double[] center = new Double[]{
                300.0, 300.0,
        };
        Hex hex = new Hex(GameController.getGame().getMap()[30][30], center);

        pane.getChildren().add(hex);
    }
}
