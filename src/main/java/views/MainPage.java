package views;

import controllers.GameMenuController;
import controllers.GsonHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import models.Game;

public class MainPage extends PageController{
    @FXML
    private void newGame() {
        this.onExit();
        App.setRoot("startGamePage");
    }

    @FXML
    private void loadGame() {
        Game game = GsonHandler.importGame();
        if (game == null) {
            // TODO: add message
            System.out.println("No game to load");
            return;
        }
        this.onExit();
        GameMenuController.setGame(game);
        App.setRoot("gamePage");
        Menu.setCurrentMenu(GameMenu.getInstance());
    }

    @FXML
    private void profile(){
        App.setRoot("profilePage");
        Menu.setCurrentMenu(ProfileMenu.getInstance());
    }
    @FXML
    private void scoreboard() {
        App.setRoot("scoreboardPage");
    }

    @FXML
    private void logout() {
        App.setCurrentUser(null);
        App.setRoot("loginPage");
        Menu.setCurrentMenu(RegisterMenu.getInstance());
    }

}
