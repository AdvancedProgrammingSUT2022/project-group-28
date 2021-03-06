package views;

import controllers.GameController;
import controllers.GameMenuController;
import controllers.NetworkController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.Game;
import models.OnlineGame;
import models.WaitingGame;
import models.network.ClientRequest;
import models.network.ServerResponse;
import models.User;

import java.util.ArrayList;
import java.util.zip.Deflater;

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
            startGameButton.setDisable(false);
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
            startGameButton.setDisable(true);
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
    private void startGame() {
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.START_GAME, new ArrayList<>(),
                NetworkController.getInstance().getUserToken());
        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

        if (serverResponse.getResponse() == ServerResponse.Response.SUCCESS) {
            OnlineGame onlineGame = OnlineGame.fromXML(serverResponse.getData().get(0));

            ArrayList<User> players = (ArrayList<User>) onlineGame.getOtherPlayers().clone();
            players.add(onlineGame.getAdmin());

            // TODO: set random seed

            Game game = new Game(players, 1000);
            GameMenuController.setGame(game);

            ArrayList<String> data = new ArrayList<>();
            data.add(game.encode());

            ClientRequest clientRequest1 = new ClientRequest(ClientRequest.Request.SET_INITIAL_GAME, data,
                                         NetworkController.getInstance().getUserToken());

            ServerResponse serverResponse1 = NetworkController.getInstance().sendRequest(clientRequest1);

            if (serverResponse1.getResponse() == ServerResponse.Response.SUCCESS) {
                App.setRoot("gamePage");
            }
        }
    }

    @FXML
    private void sendInvitation() {
        String nickname = playerNickname.getText();

        if (nickname.length() == 0) {
            playerMessage.setText("Please enter a nickname");
            playerMessage.setManaged(true);
        }

        ArrayList<String> data = new ArrayList<>();
        data.add(nickname);

        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.INVITE_GAME, data,
                                    NetworkController.getInstance().getUserToken());

        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
        switch (serverResponse.getResponse()) {
            case INVALID_NICKNAME:
                playerMessage.setText(nickname + " does not exists");
                playerMessage.setManaged(true);
                break;
            case NOT_FRIEND:
                playerMessage.setText(nickname + " is not your friend");
                playerMessage.setManaged(true);
                break;
            case IS_OFFLINE:
                playerMessage.setText(nickname + " is offline");
                playerMessage.setManaged(true);
                break;
            case SUCCESS:
                playerMessage.setText("Invitation sent");
                playerMessage.setManaged(true);
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
            HBox hBox = new HBox(10);
            hBox.getStyleClass().add("user_info");
            hBox.setAlignment(Pos.CENTER);

            Circle avatar = new Circle(35);
            avatar.setFill(new ImagePattern(friend.getAvatar()));
            hBox.getChildren().add(avatar);

            Text nickname = new Text(friend.getNickname());
            nickname.getStyleClass().add("normal_text");
            hBox.getChildren().add(nickname);

            this.friendsContainer.getChildren().add(hBox);
        }
    }

    public void createWaitingGamesList() {
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.GET_WAITING_GAMES, new ArrayList<>());
        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);
        ArrayList<WaitingGame> waitingGames = WaitingGame.waitingGamesFromXML(serverResponse.getData().get(0));

        User user = App.getCurrentUser();

        this.gamesContainer.getChildren().clear();
        for (WaitingGame waitingGame : waitingGames) {
            VBox vBox = new VBox(10);
            vBox.setAlignment(Pos.CENTER);
            vBox.getStyleClass().add("game_box");

            Text description = new Text("Admin: " + waitingGame.getAdmin().getNickname());
            description.getStyleClass().add("normal_text");
            vBox.getChildren().add(description);

            Text size = new Text("Players: " + (waitingGame.getOtherPlayers().size() + 1));
            size.getStyleClass().add("normal_text");
            vBox.getChildren().add(size);

            if (!WaitingGame.isAdmin(user, waitingGames)) {
                if (waitingGame.isOtherPlayer(user)) {
                    Button button = new Button("Leave");
                    button.getStyleClass().add("game_button");
                    button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            leaveGame();
                        }
                    });
                    vBox.getChildren().add(button);
                } else if (!WaitingGame.isInAnyGame(user, waitingGames)) {
                    Button button = new Button("Attend");
                    button.getStyleClass().add("game_button");
                    button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            attendGame(waitingGame);
                        }
                    });
                    vBox.getChildren().add(button);
                } else if (waitingGame.isWaitingForAccept(user)){
                    Text text = new Text("Waiting for accept");
                    text.getStyleClass().add("normal_text");
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

                        Thread.sleep(3000);
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

    public Button getLeaveGameButton() { return leaveGameButton; }

    public Button getCancelGameButton() { return cancelGameButton; }

    public Button getSendInviteButton() { return sendInviteButton; }

    public Button getCreateGameButton() { return createGameButton; }

    public Button getStartGameButton() { return startGameButton; }

    public Button getBackButton() { return backButton; }
}
