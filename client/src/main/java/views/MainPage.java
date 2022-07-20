package views;

import controllers.GameMenuController;
import controllers.GsonHandler;
import controllers.NetworkController;
import javafx.fxml.FXML;
import models.Game;

public class MainPage extends PageController{
    @FXML
    private void newGame() {
        this.onExit();
        App.setRoot("startGamePage");
    }

    @FXML
    private void lobby() {
        NetworkController.getInstance().setOnline(true);
        App.setRoot("lobbyPage");
    }

    @FXML
    private void loadGame() {
        Game game = GsonHandler.importGame("gameInformation");
        if (game == null) {
            // TODO: add message
            System.out.println("No game to load");
            return;
        }
        this.onExit();
        GameMenuController.setGame(game);
        App.setRoot("gamePage");
    }

    @FXML
    private void profile(){
        App.setRoot("profilePage");
    }
    @FXML
    private void scoreboard() {
        App.setRoot("scoreboardPage");
    }

    @FXML
    private void logout() {
        // TODO: server logout

        App.setCurrentUser(null);
        App.setRoot("loginPage");
    }

}
