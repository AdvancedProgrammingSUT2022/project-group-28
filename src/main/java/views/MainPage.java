package views;

import javafx.fxml.FXML;

public class MainPage extends PageController{
    @FXML
    private void newGame() {
        this.onExit();
        App.setRoot("startGamePage");
    }
    @FXML
    private void profile(){
        App.setRoot("profile");
    }
    @FXML
    private void scoreboard() {
        App.setRoot("scoreboardPage");
    }

}
