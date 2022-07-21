package views;

import controllers.NetworkController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import models.User;
import models.network.ClientRequest;
import java.util.ArrayList;

public class AttendGameRequestPage extends PageController {
    private static User sender;

    @FXML
    private Text message;

    @FXML
    private void initialize() {
        message.setText(sender.getNickname() + " wants to attend your game");
    }

    @FXML
    private void accept() {
        ArrayList<String> data = new ArrayList<>();
        data.add(sender.toXML());
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.ACCEPT_ATTEND_GAME, data,
                                    NetworkController.getInstance().getUserToken());
        NetworkController.getInstance().sendRequest(clientRequest);
        LobbyPage.getInstance().createWaitingGamesList();
        exit();
    }

    @FXML
    private void reject() {
        ArrayList<String> data = new ArrayList<>();
        data.add(sender.toXML());
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.REJECT_ATTEND_GAME, data,
                                    NetworkController.getInstance().getUserToken());
        NetworkController.getInstance().sendRequest(clientRequest);
        LobbyPage.getInstance().createWaitingGamesList();
        exit();
    }

    private void exit() {
        message.getScene().getWindow().hide();
    }

    public static void setSender(User sender) { AttendGameRequestPage.sender = sender; }
}
