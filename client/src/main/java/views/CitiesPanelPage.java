package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class CitiesPanelPage extends PageController {
    @FXML
    private Button backButton;
    @FXML
    private VBox citiesPanelContainer;

    public void back(MouseEvent mouseEvent) {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }
}
