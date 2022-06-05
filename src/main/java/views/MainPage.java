package views;

import javafx.fxml.FXML;

public class MainPage extends PageController{
    @FXML
    private void newGame() {
        this.onExit();
        App.setRoot("startGamePage");
    }
    public void profile(){
        App.setRoot("profilePage");
    }
}
