package views;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginPage extends PageController {
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    public void goRegisterPage(MouseEvent mouseEvent) throws IOException {
        App.setRoot("registerPage");
    }

    public void goMainMenu(MouseEvent mouseEvent) {
    }
}
