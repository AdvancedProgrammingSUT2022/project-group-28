package views;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;

public class ProfilePage extends PageController{
    @FXML
    private ImageView picture,right,left;

    private User currentUser;

    private int imagePointer;

    public void initialize(){
        currentUser = App.getCurrentUser();
        picture.setImage(currentUser.getAvatar());
        this.imagePointer = currentUser.getProfilePicNumber();
        if(imagePointer==7) left.setVisible(false);
        else if(imagePointer==1) right.setVisible(false);
    }

    public void back(){
        App.setRoot("mainPage");
    }

    public void chooseAvatar(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open avatar picture");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*jpg"));
        File file = fileChooser.showOpenDialog((Stage)picture.getScene().getWindow());
        if (file!=null){
            picture.setImage(new Image(file.toURI().toString()));
            imagePointer = -1;
            currentUser.setProfilePicNumber(-1);
            currentUser.setAvatar(picture.getImage());
        }
    }

    public void leftPic(){
        if(imagePointer == -1) imagePointer = 1;
        if (imagePointer==7)return;
        else {
            imagePointer++;
            if(imagePointer==7) left.setVisible(false);
            picture.setImage(new Image(App.resource.resolve("assets/image/profilePictures/" + imagePointer + ".png").toString()));
            currentUser.setProfilePicNumber(imagePointer);
            right.setVisible(true);
        }
    }

    public void rightPic(){
        if(imagePointer < 1) imagePointer = 1;
        if (imagePointer==1)return;
        else {
            imagePointer--;
            if(imagePointer==1) right.setVisible(false);
            picture.setImage(new Image(App.resource.resolve("assets/image/profilePictures/" + imagePointer + ".png").toString()));
            currentUser.setProfilePicNumber(imagePointer);
            left.setVisible(true);
        }
    }

    public void changePassword(){
        
    }

    public void changeNickname(){

    }
}

class ChangePassword{
    String current;
    String newPass;
    String confirm;
}
