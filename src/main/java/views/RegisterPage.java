package views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegisterPage extends PageController {

    @FXML
    private TextField username;
    @FXML
    private TextField nickname;
    @FXML
    private TextField password;
    @FXML
    private Label error;
}
