package views;

import controllers.ProfileMenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
            String textOfError;
            switch(ProfileMenuController.changeNickname(newNickname.getText())){
                case SUCCESS:
                    textOfError = "Nickname changed successfully.";
                    errorLabel.setStyle("-fx-text-fill: #ea540a");
                    errorLabel.setText(textOfError);
                    errorLabel.setLayoutX(340- textOfError.length()*4.5);
                    break;
                case CHANGE_NICKNAME_ERROR:
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
