package views;

import controllers.ProfileMenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

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
            String textOfError;
            switch(ProfileMenuController.changePassword(current.getText(), newPassword.getText())){
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
