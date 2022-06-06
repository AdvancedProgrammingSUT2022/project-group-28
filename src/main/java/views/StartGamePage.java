package views;

import controllers.GameMenuController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.User;

import java.util.ArrayList;


public class StartGamePage extends PageController {
    @FXML
    private VBox playersContainer;
    @FXML
    private TextField playerUsername;
    @FXML
    private Label message;
    @FXML
    private Button startGameButton;

    @FXML
    private void initialize() {
        this.playersContainer.getChildren().add(createUserHBox(App.getCurrentUser()));
    }

    private HBox createUserHBox(User user) {
        HBox hBox = new HBox();
        hBox.setId(user.getUsername());
        hBox.setSpacing(30);
        hBox.setPrefWidth(700);
        hBox.getStyleClass().add("player_box");

        Circle avatar = new Circle(40);
        avatar.setFill(new ImagePattern(user.getAvatar()));
        hBox.getChildren().add(avatar);

        VBox usernameVBox = new VBox();
        usernameVBox.setPrefWidth(300);
        Text username = new Text(user.getUsername());
        usernameVBox.getChildren().add(username);
        hBox.getChildren().add(usernameVBox);

        if (!user.equals(App.getCurrentUser())) {
            ImagePattern removePattern = new ImagePattern(new Image(App.class.getResource("../assets/image/ui_icon/cross.png").toExternalForm()));
            ImagePattern redRemovePattern = new ImagePattern(new Image(App.class.getResource("../assets/image/ui_icon/red_cross.png").toExternalForm()));
            Rectangle remove = new Rectangle(50, 50);
            remove.getStyleClass().add("remove_button");
            remove.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    remove.setFill(redRemovePattern);
                }
            });
            remove.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    remove.setFill(removePattern);
                }
            });

            remove.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    for (Node child : StartGamePage.this.playersContainer.getChildren()) {
                        if (child.getId().equals(user.getUsername())) {
                            StartGamePage.this.playersContainer.getChildren().remove(child);
                            StartGamePage.this.message.setManaged(false);
                            StartGamePage.this.message.setVisible(false);

                            checkStartGamePossibility();
                            return;
                        }
                    }
                }
            });
            remove.setFill(removePattern);
            hBox.getChildren().add(remove);
        }


        return hBox;
    }

    @FXML
    private void addPlayer() {
        if (this.playersContainer.getChildren().size() == 5) {
            this.message.setText("Maximum players is 5");
            this.message.setManaged(true);
            return;
        }

        String usernameValue = this.playerUsername.getText();

        for (Node child : this.playersContainer.getChildren()) {
            if (child.getId().equals(usernameValue)) {
                this.message.setText("Request has been sent to \"" + usernameValue + "\"");
                this.message.setManaged(true);
                return;
            }
        }

        User user = User.getUserByUsername(usernameValue);

        if (user == null) {
            this.message.setText("\"" + usernameValue + "\" does not exists");
            this.message.setManaged(true);
            return;
        }

        // TODO: send request while adding
        playersContainer.getChildren().add(createUserHBox(user));

        checkStartGamePossibility();

        this.message.setManaged(false);

    }

    private void checkStartGamePossibility() {
        if (this.playersContainer.getChildren().size() == 1) {
            startGameButton.setDisable(true);
        } else startGameButton.setDisable(false);
    }

    @FXML
    private void newGame() {
        GameMenuController.startNewGame(getPlayers(), 100);
        this.onExit();
        App.setRoot("gamePage");
    }

    private ArrayList<User> getPlayers() {
        ArrayList<User> players = new ArrayList<>();
        for (Node child : this.playersContainer.getChildren()) {
            players.add(User.getUserByUsername(child.getId()));
        }
        return players;
    }

    @FXML
    private void back() {
        this.onExit();
        App.setRoot("mainPage");
    }

    // TODO: add accept request tasks and change the user condition

}
