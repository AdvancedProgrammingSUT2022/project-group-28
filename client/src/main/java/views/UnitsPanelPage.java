package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class UnitsPanelPage extends PageController {
    @FXML
    private VBox unitsPanelContainer;
    @FXML
    private Button backButton;








    @FXML
    private void back() {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }
}
