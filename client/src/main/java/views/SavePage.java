package views;

import controllers.GameController;
import controllers.GsonHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SavePage {
    @FXML
    private TextField filename;
    @FXML
    private Label errorLabel;

    @FXML
    private void save() {
        String name = filename.getText();
        if (name.equals("")) {
            errorLabel.setText("Please enter a filename");
            return;
        }else if (!isValidName(name)){
            errorLabel.setText("Please enter a valid filename");
            return;
        }else{
            errorLabel.setText("game saved");
            GsonHandler.saveGame(GameController.getGame(), name);
        }
    }

    @FXML
    private void back() {
        filename.getScene().getWindow().hide();
    }

    private static boolean isValidName(String text){
        return text.matches("[-_. A-Za-z0-9]+");
    }
}
