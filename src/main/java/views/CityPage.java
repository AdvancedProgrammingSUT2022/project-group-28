package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class CityPage extends PageController {
    @FXML
    private Button backButton;

    @FXML
    private void back() {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }

}
