package views;

import controllers.NetworkController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.network.ClientRequest;
import models.network.ServerResponse;
import models.User;

import java.io.IOException;
import java.util.ArrayList;

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

            try {
                NetworkController.getInstance().startUpdateListener(NetworkController.getInstance().getIPAddress(), 8000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            App.setCurrentUser(User.fromXML(serverResponse.getData().get(1)));

            App.setRoot("mainPage");
        } else {
            String textOfError = "username or password is incorrect.";
            error.setStyle("-fx-text-fill: #ea540a");
            error.setText(textOfError);
            error.setLayoutX(410- textOfError.length() * 4.5);
        }
    }
}
