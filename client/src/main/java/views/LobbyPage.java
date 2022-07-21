package views;

import controllers.NetworkController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.WaitingGame;
import models.network.ClientRequest;
import models.network.ServerResponse;
import models.User;

import java.util.ArrayList;

public class LobbyPage extends PageController{
    private static LobbyPage instance;

    private Thread pageUpdater;

    @FXML
    private VBox gamesContainer;
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
    private Button leaveGameButton;
    @FXML
    private Button cancelGameButton;
    @FXML
    private Button sendInviteButton;
    @FXML
    private Button createGameButton;
    @FXML
    private Button startGameButton;
    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        instance = this;

        User user = App.getCurrentUser();
        createFriendsList(user);
        createWaitingGamesList();
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
            case OWN_FRIENDSHIP:
                this.friendMessage.setText("You can not send request to yourself");
                this.friendMessage.setManaged(true);
                break;
            case FRIENDSHIP_REQUEST_WAITING:
                this.friendMessage.setText("You have been sent request to this user");
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
    private void createGame() {
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.CREATE_GAME, new ArrayList<>(),
                                    NetworkController.getInstance().getUserToken());
        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            backButton.setDisable(true);
            createGameButton.setDisable(true);
            sendInviteButton.setDisable(false);
            cancelGameButton.setDisable(false);

            // TODO: handle start game button

            createWaitingGamesList();
        }
    }

    @FXML
    private void cancelGame() {
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.CANCEL_GAME, new ArrayList<>(),
                                    NetworkController.getInstance().getUserToken());
        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            sendInviteButton.setDisable(true);
            cancelGameButton.setDisable(true);
            backButton.setDisable(false);
            createGameButton.setDisable(false);

            createWaitingGamesList();
        }
    }

    @FXML
    private void leaveGame() {
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.LEAVE_GAME, new ArrayList<>(),
                                    NetworkController.getInstance().getUserToken());
        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            sendInviteButton.setDisable(true);
            leaveGameButton.setDisable(true);
            backButton.setDisable(false);
            createGameButton.setDisable(false);

            createWaitingGamesList();
        }
    }

    @FXML
    private void back() {
        this.onExit();
        NetworkController.getInstance().setOnline(false);
        pageUpdater.interrupt();
        App.setRoot("mainPage");
    }

    private void attendGame(WaitingGame waitingGame) {
        ArrayList<String> data = new ArrayList<>();
        data.add(waitingGame.toXML());
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.ATTEND_GAME, data,
                                    NetworkController.getInstance().getUserToken());
        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            backButton.setDisable(true);
            createGameButton.setDisable(true);
            leaveGameButton.setDisable(false);

            createWaitingGamesList();
        } else if (serverResponse.getResponse() == ServerResponse.Response.NOT_FRIEND) {
            playerMessage.setText("Game admin is not your friend");
            playerMessage.setManaged(true);
        }
    }

    private void createFriendsList(User user) {
        String token = NetworkController.getInstance().getUserToken();
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.GET_USER_INFO,
                new ArrayList<>(), token);

        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            App.setCurrentUser(User.fromXML(serverResponse.getData().get(0)));
        }

        this.friendsContainer.getChildren().clear();
        for (User friend : user.getFriends()) {
            this.friendsContainer.getChildren().add(new Text(friend.getNickname()));
        }
    }

    public void createWaitingGamesList() {
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.GET_WAITING_GAMES, new ArrayList<>());
        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
        ArrayList<WaitingGame> waitingGames = WaitingGame.waitingGamesFromXML(serverResponse.getData().get(0));

        User user = App.getCurrentUser();

        this.gamesContainer.getChildren().clear();
        for (WaitingGame waitingGame : waitingGames) {
            VBox vBox = new VBox();

            Text description = new Text("Admin: " + waitingGame.getAdmin().getNickname());
            vBox.getChildren().add(description);

            Text size = new Text("Players: " + (waitingGame.getOtherPlayers().size() + 1));
            vBox.getChildren().add(size);

            if (!WaitingGame.isAdmin(user, waitingGames)) {
                if (waitingGame.isOtherPlayer(user)) {
                    Button button = new Button("Leave");
                    button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            leaveGame();
                        }
                    });
                    vBox.getChildren().add(button);
                } else if (!WaitingGame.isInAnyGame(user, waitingGames)) {
                    Button button = new Button("Attend");
                    button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            attendGame(waitingGame);
                        }
                    });
                    vBox.getChildren().add(button);
                } else if (waitingGame.isWaitingForAccept(user)){
                    Text text = new Text("Waiting for accept");
                    vBox.getChildren().add(text);
                }
            }
            this.gamesContainer.getChildren().add(vBox);
        }
    }

    private void createUpdater() {
        this.pageUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                createFriendsList(App.getCurrentUser());
                                createWaitingGamesList();
                            }
                        });

                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });

        this.pageUpdater.start();
    }

    public static LobbyPage getInstance() { return instance; }
}
