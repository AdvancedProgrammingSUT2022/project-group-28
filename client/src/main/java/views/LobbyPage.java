package views;

import javafx.fxml.FXML;

public class LobbyPage extends PageController{

    @FXML
    private void back() {
        this.onExit();
        App.setRoot("mainPage");
    }
}
