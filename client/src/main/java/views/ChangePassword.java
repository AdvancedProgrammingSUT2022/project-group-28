package views;

import controllers.NetworkController;
import controllers.ProfileMenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import models.network.ClientRequest;
import models.network.ServerResponse;

import java.util.ArrayList;

public class ChangePassword {
    @FXML 
    private Label errorLabel;
    @FXML
    private PasswordField current,newPassword,confirmPassword;
    public void change(){
        if(current.getText().equals("")||newPassword.getText().equals("")||confirmPassword.getText().equals("")){
            errorLabel.setText("Please fill all the fields");
        }
        else if(!newPassword.getText().equals(confirmPassword.getText())){
            errorLabel.setText("Passwords do not match");
        }
        else{
            ArrayList<String> data = new ArrayList<>();
            data.add(current.getText());
            data.add(newPassword.getText());

            ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.CHANGE_PASSWORD, data,
                                          NetworkController.getInstance().getUserToken());

            ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

            String textOfError;
            switch(serverResponse.getResponse()){
                case SUCCESS:
                    textOfError = "Password changed successfully.";
                    errorLabel.setStyle("-fx-text-fill: #ea540a");
                    errorLabel.setText(textOfError);
                    errorLabel.setLayoutX(340- textOfError.length()*4.5);
                    break;
                case INCORRECT_PASSWORD:
                    textOfError = "Wrong password.";
                    errorLabel.setStyle("-fx-text-fill: #ea540a");
                    errorLabel.setText(textOfError);
                    errorLabel.setLayoutX(340- textOfError.length()*4.5);
                    break;
                case REPETITIOUS_PASSWORD:
                    textOfError = "New password is the same as the old one.";
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
