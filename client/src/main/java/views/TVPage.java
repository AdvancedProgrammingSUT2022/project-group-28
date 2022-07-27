package views;

import controllers.GameMenuController;
import controllers.NetworkController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.Game;
import models.OnlineGame;
import models.User;
import models.network.ClientRequest;
import models.network.ServerResponse;

import java.util.ArrayList;

public class TVPage extends PageController {

    @FXML
    private VBox gameContainer;

    @FXML
    private void initialize() {
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.GET_ALL_ONLINE_GAMES, new ArrayList<>());
        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
        for (String datum : serverResponse.getData()) {
            OnlineGame onlineGame = OnlineGame.fromXML(datum);

            VBox vBox = new VBox(20);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefWidth(500);
            vBox.getStyleClass().add("online_game_info");

            Text game = new Text("Game");
            game.getStyleClass().add("normal_text");
            vBox.getChildren().add(game);

            Rectangle line = new Rectangle(250, 7);
            line.getStyleClass().add("line");
            vBox.getChildren().add(line);

            for (User player : onlineGame.getOtherPlayers()) {
                Text playerText = new Text(player.getNickname());
                playerText.getStyleClass().add("normal_text");
                vBox.getChildren().add(playerText);
            }

            Rectangle line2 = new Rectangle(250, 7);
            line2.getStyleClass().add("line");

            vBox.getChildren().add(line2);

            Button watch = new Button("Watch");
            watch.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    watch(onlineGame);
                }
            });
            watch.getStyleClass().add("normal_button");
            vBox.getChildren().add(watch);

            gameContainer.getChildren().add(vBox);
        }
    }

    private void watch(OnlineGame onlineGame) {
        TVShowPage.setFirstUserID(onlineGame.getOtherPlayers().get(0).getId());

        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(onlineGame.getOtherPlayers().get(0).getId()));

        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.GET_ONLINE_GAME, data,
                NetworkController.getInstance().getUserToken());

        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            Game game = Game.decode(serverResponse.getData().get(0));
            GameMenuController.setGame(game);
            App.setRoot("tvShowPage");
        }
    }

    @FXML
    private void back() {
        App.setRoot("mainPage");
    }
}
