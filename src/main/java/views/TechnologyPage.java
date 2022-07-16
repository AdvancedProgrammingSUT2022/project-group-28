package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class TechnologyPage extends PageController{
    @FXML
    private Button backButton;
    @FXML
    private VBox technologiesContainer;

    @FXML
    private void back() {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }
}
