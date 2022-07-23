package views;

import controllers.NetworkController;
import controllers.ProfileMenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.network.ClientRequest;
import models.network.ServerResponse;

import java.util.ArrayList;
import java.util.Date;

public class ChangeNickname {
    @FXML 
    private Label errorLabel;
    @FXML
    private TextField newNickname;

    public void change(){
        if (newNickname.getText().equals("")){
            errorLabel.setText("Please fill all the fields");
        }
        else{
            ArrayList<String> data = new ArrayList<>();
            data.add(newNickname.getText());
            ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.CHANGE_NICKNAME, data,
                                        NetworkController.getInstance().getUserToken());

            ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

            String textOfError;
            switch(serverResponse.getResponse()){
                case SUCCESS:
                    textOfError = "Nickname changed successfully.";
                    errorLabel.setStyle("-fx-text-fill: #ea540a");
                    errorLabel.setText(textOfError);
                    errorLabel.setLayoutX(340- textOfError.length()*4.5);
                    break;
                case INVALID_NICKNAME:
                    textOfError = "Nickname already exists.";
                    errorLabel.setStyle("-fx-text-fill: #ea540a");
                    errorLabel.setText(textOfError);
                    errorLabel.setLayoutX(340- textOfError.length()*4.5);
                    break;
                default:
                    break;                
            }
        }
    }

    public void back(){
        errorLabel.getScene().getWindow().hide();
    }
}
