package views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import views.enums.Message;

import java.io.IOException;

import controllers.RegisterMenuController;

public class LoginPage extends PageController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label error;

    public void goRegisterPage(MouseEvent mouseEvent) throws IOException {
        App.setRoot("registerPage");
    }

    public void goMainMenu(MouseEvent mouseEvent) {
        if (RegisterMenuController.checkUserLoginData(username.getText(), password.getText())==Message.SUCCESS) {
            RegisterMenuController.setLoggedInUser(username.getText());
            App.setRoot("mainPage");
        }
        else {
            String textOfError = "username or password is incorrect.";
            error.setStyle("-fx-text-fill: #ea540a");
            error.setText(textOfError);
            error.setLayoutX(340- textOfError.length()*4.5);
        }
    }
}
