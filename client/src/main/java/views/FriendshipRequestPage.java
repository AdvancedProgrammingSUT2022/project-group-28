package views;

import controllers.NetworkController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import models.network.ClientRequest;
import models.network.ServerResponse;

import java.util.ArrayList;

public class FriendshipRequestPage extends PageController {
    // TODO: find a better solution
    private static String requesterNickname;

    @FXML
    private Text message;


    @FXML
    private void initialize() {
        this.message.setText(requesterNickname + " has sent you friendship request");
    }


    @FXML
    private void accept() {
        ArrayList<String> data = new ArrayList<>();
        data.add(requesterNickname);
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.ACCEPT_FRIENDSHIP, data,
                                    NetworkController.getInstance().getUserToken());

        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

        exitPage();
    }

    @FXML
    private void reject() {
        exitPage();
    }


    private void exitPage() {
        this.onExit();
        this.message.getParent().getScene().getWindow().hide();
    }

    public static String getRequesterNickname() { return requesterNickname; }

    public static void setRequesterNickname(String requesterNickname) {
        FriendshipRequestPage.requesterNickname = requesterNickname;
    }
}
