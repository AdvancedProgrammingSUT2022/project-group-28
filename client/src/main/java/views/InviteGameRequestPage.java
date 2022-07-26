package views;

import controllers.NetworkController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import models.User;
import models.network.ClientRequest;
import models.network.ServerResponse;

import java.util.ArrayList;

public class InviteGameRequestPage extends PageController {
    private static User sender;

    @FXML
    private Text message;

    @FXML
    private void initialize() {
        message.setText(sender.getNickname() + " invites you to a game.");
    }

    @FXML
    private void accept() {
        ArrayList<String> data = new ArrayList<>();
        data.add(sender.toXML());
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.ACCEPT_INVITE_GAME, data,
                                    NetworkController.getInstance().getUserToken());
        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            App.setRoot("lobbyPage");
            LobbyPage.getInstance().getBackButton().setDisable(true);
            LobbyPage.getInstance().getCreateGameButton().setDisable(true);
            LobbyPage.getInstance().getLeaveGameButton().setDisable(false);
        }

        exit();
    }

    @FXML
    private void reject() {
        exit();
    }

    private void exit() {
        message.getScene().getWindow().hide();
    }

    public static void setSender(User sender) { InviteGameRequestPage.sender = sender; }
}
