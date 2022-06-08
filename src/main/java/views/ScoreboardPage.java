package views;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.User;


public class ScoreboardPage extends PageController {

    @FXML
    private VBox rankings;

    @FXML
    public void initialize() {
        // TODO: add sort users
        for (User user : User.getAllUsers()) {
            rankings.getChildren().add(createUserInfoHBox(user));
        }
    }

    private HBox createUserInfoHBox(User user) {
        HBox hBox = new HBox();
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

        return hBox;
    }

    @FXML
    private void back() {
        this.onExit();
        App.setRoot("mainPage");
    }

}
