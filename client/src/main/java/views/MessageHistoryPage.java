package views;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import views.components.MessageBox;

public class MessageHistoryPage extends PageController {

    @FXML
    private VBox messageContainer;
    
    @FXML
    private void initialize() {
        for (MessageBox messageBox : HUDController.getMessageHistory()) {
            messageBox.getRemove().setDisable(true);
            messageContainer.getChildren().add(messageBox);
        }
    }

    @FXML
    private void back() {
        for (MessageBox messageBox : HUDController.getMessageHistory()) {
            messageBox.getRemove().setDisable(false);
        }
        messageContainer.getScene().getWindow().hide();
    }

}
