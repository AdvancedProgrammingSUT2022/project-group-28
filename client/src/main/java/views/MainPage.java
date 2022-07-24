package views;

import controllers.GameController;
import controllers.NetworkController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.network.ClientRequest;

import java.util.ArrayList;

public class MainPage extends PageController{

    @FXML
    private Pane pane;

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
        
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(pane.getScene().getWindow());
        Scene scene = new Scene(App.loadFXML("loadPage"));
        stage.setScene(scene);

        stage.setOnHidden(value -> {
            if (GameController.getGame() != null) {
                this.onExit();
                App.setRoot("gamePage");
            }
        });

        stage.show();  

        
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
    private void chat() { App.setRoot("chatPage"); }

    @FXML
    private void logout() {
        ArrayList<String> data = new ArrayList<>();
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.LOGOUT, data,
                                    NetworkController.getInstance().getUserToken());

        NetworkController.getInstance().sendRequest(clientRequest);

        App.setCurrentUser(null);
        App.setRoot("loginPage");
    }

}
