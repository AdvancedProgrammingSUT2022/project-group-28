package views;

import controllers.NetworkController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.ClientRequest;
import models.ServerResponse;
import views.enums.Message;

import java.io.IOException;
import java.util.ArrayList;

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
        ArrayList<String> data = new ArrayList<>();
        data.add(username.getText());
        data.add(password.getText());
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.LOGIN, data);

        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            NetworkController.getInstance().setUserToken(serverResponse.getData().get(0));
            // TODO: add update socket
            // TODO: change loggedIn user policy
            RegisterMenuController.setLoggedInUser(username.getText());
            App.setRoot("mainPage");
            Menu.setCurrentMenu(MainMenu.getInstance());
        } else {
            String textOfError = "username or password is incorrect.";
            error.setStyle("-fx-text-fill: #ea540a");
            error.setText(textOfError);
            error.setLayoutX(410- textOfError.length() * 4.5);
        }
    }
}
