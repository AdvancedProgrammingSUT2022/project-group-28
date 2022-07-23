package views;

import controllers.NetworkController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.User;
import models.network.ClientRequest;
import models.network.ServerResponse;

import java.util.ArrayList;


public class ScoreboardPage extends PageController {

    private Thread pageUpdater;

    @FXML
    private VBox rankings;

    @FXML
    public void initialize() {
        updateScoreBoard();
        createPageUpdater();
    }

    private HBox createUserInfoHBox(User user) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(30);
        hBox.setPrefWidth(700);
        hBox.getStyleClass().add("user_ranking_info");

        Circle avatar = new Circle(40);
        avatar.setFill(new ImagePattern(user.getAvatar()));
        hBox.getChildren().add(avatar);

        VBox nicknameVBox = new VBox();
        nicknameVBox.setPrefWidth(300);
        Text nickname = new Text(user.getNickname());
        nicknameVBox.getChildren().add(nickname);
        hBox.getChildren().add(nicknameVBox);

        VBox scoreVBox = new VBox();
        scoreVBox.setPrefWidth(200);
        Text score = new Text(String.valueOf(user.getScore()));
        scoreVBox.getChildren().add(score);
        hBox.getChildren().add(scoreVBox);

        VBox onlineBox = new VBox();
        onlineBox.setPrefWidth(100);
        if (user.isOnline()) {
            Circle circle = new Circle(20);
            circle.setFill(Color.GREEN);
            onlineBox.getChildren().add(circle);
        } else {
            Text lastOnline = new Text("Last Online: " + user.getLastOnline().toString());
            onlineBox.getChildren().add(lastOnline);
        }
        hBox.getChildren().add(onlineBox);


        return hBox;
    }

    private void updateScoreBoard() {
        ClientRequest clientRequest = new ClientRequest(ClientRequest.Request.GET_ALL_USERS, new ArrayList<>());
        ServerResponse serverResponse = NetworkController.getInstance().sendRequest(clientRequest);

        ArrayList<User> users = User.usersFromXML(serverResponse.getData().get(0));
        // TODO: sort users

        rankings.getChildren().clear();

        for (User user : users) {
            rankings.getChildren().add(createUserInfoHBox(user));
        }
    }

    private void createPageUpdater() {
        pageUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                updateScoreBoard();
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        pageUpdater.start();
    }

    @FXML
    private void back() {
        pageUpdater.interrupt();
        this.onExit();
        App.setRoot("mainPage");
    }

}
