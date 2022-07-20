package views;

import controllers.NetworkController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.ClientRequest;
import models.ServerResponse;
import models.User;
import sun.nio.ch.Net;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

public class LobbyPage extends PageController{
    private Thread pageUpdater;

    @FXML
    private VBox playersContainer;
    @FXML
    private TextField playerNickname;
    @FXML
    private Label playerMessage;

    @FXML
    private VBox friendsContainer;
    @FXML
    private TextField friendNickname;
    @FXML
    private Label friendMessage;

    @FXML
    private void initialize() {
        User user = App.getCurrentUser();
        createFriendsList(user);
        createUpdater();
    }

    @FXML
    private void sendFriendshipRequest() {
        ArrayList<String> data = new ArrayList<>();
        String friendNickname = this.friendNickname.getText();
        data.add(friendNickname);
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.REQUEST_FRIENDSHIP, data,
                                        NetworkController.getInstance().getUserToken());

        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
        switch (serverResponse.getResponse()) {
            case INVALID_TOKEN:
                this.friendMessage.setText("Failed to send");
                this.friendMessage.setManaged(true);
                break;
            case INVALID_NICKNAME:
                this.friendMessage.setText(friendNickname + " does not exists");
                this.friendMessage.setManaged(true);
                break;
            case ALREADY_FRIEND:
                this.friendMessage.setText(friendNickname + " is already your friend");
                this.friendMessage.setManaged(true);
                break;
            case IS_OFFLINE:
                this.friendMessage.setText(friendNickname + " is offline");
                this.friendMessage.setManaged(true);
                break;
            case SUCCESS:
                this.friendMessage.setText("Request sent");
                this.friendMessage.setManaged(true);
                break;
            default:
                break;
        }
    }


    @FXML
    private void back() {
        this.onExit();
        NetworkController.getInstance().setOnline(false);
        pageUpdater.interrupt();
        App.setRoot("mainPage");
    }

    private void createFriendsList(User user) {
        LobbyPage.this.friendsContainer.getChildren().clear();
        for (User friend : user.getFriends()) {
            LobbyPage.this.friendsContainer.getChildren().add(new Text(friend.getNickname()));
        }
    }

    private void createUpdater() {
        this.pageUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String token = NetworkController.getInstance().getUserToken();
                    ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.GET_USER_INFO,
                            new ArrayList<>(), token);

                    ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
                    if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
                        App.setCurrentUser(User.fromXML(serverResponse.getData().get(0)));
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                createFriendsList(App.getCurrentUser());
                            }
                        });
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        this.pageUpdater.start();
    }
}
