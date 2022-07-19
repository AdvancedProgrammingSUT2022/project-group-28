package views;

import controllers.NetworkController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.ClientRequest;
import models.ServerResponse;

import java.io.IOException;
import java.util.ArrayList;

public class RegisterPage extends PageController {

    @FXML
    private TextField username;
    @FXML
    private TextField nickname;
    @FXML
    private TextField password;
    @FXML
    private Label error;


    @FXML
    private void checkDataOfUser() {
        ArrayList<String> data = new ArrayList<>();
        data.add(username.getText());
        data.add(password.getText());
        data.add(nickname.getText());

        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.REGISTER, data);

        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

        String textOfError = "";
        if(username.getText().length() != 0 && password.getText().length() != 0 && nickname.getText().length() != 0) {
            switch (serverResponse.getResponse()) {
                case SUCCESS:
                    textOfError = "user created successfully!";
                    error.setStyle("-fx-text-fill: green");
                    break;
                case USERNAME_EXISTS:
                    textOfError = "user with username " + username.getText() + " already exists";
                    error.setStyle("-fx-text-fill: #ea540a");
                    break;
                case NICKNAME_EXISTS:
                    textOfError = "user with nickname " + nickname.getText() + " already exists";
                    error.setStyle("-fx-text-fill: #ea540a");
                    break;
                default:
                    break;
            }
        }
        else {
            textOfError = "some field are empty!";
            error.setStyle("-fx-text-fill: #ea540a");
        }

        error.setText(textOfError);
        error.setLayoutX(340- textOfError.length()*4.5);
    }

    public void goLoginMenu(MouseEvent mouseEvent) throws IOException {
        App.setRoot("loginPage");
    }
}
