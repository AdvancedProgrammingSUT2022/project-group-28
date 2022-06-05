package views;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
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


public class StartGamePage extends PageController {
    @FXML
    private VBox playersContainer;
    @FXML
    private TextField playerUsername;
    @FXML
    private Label message;

    @FXML
    private void initialize() {
        User user = new User("asdf", "asdf", "dasf");

        this.playersContainer.getChildren().add(createUserHBox(user));
    }

    private HBox createUserHBox(User user) {
        HBox hBox = new HBox();
        hBox.setId(user.getUsername());
        hBox.setSpacing(30);
        hBox.setPrefWidth(700);
        hBox.getStyleClass().add("player_box");

        // TODO: change profile image
        ImagePattern avatarPattern = new ImagePattern(new Image(App.class.getResource("../assets/image/background/start_background.png").toExternalForm()));
        Circle avatar = new Circle(40);
        avatar.setFill(avatarPattern);
        hBox.getChildren().add(avatar);

        VBox usernameVBox = new VBox();
        usernameVBox.setPrefWidth(300);
        Text username = new Text(user.getUsername());
        usernameVBox.getChildren().add(username);
        hBox.getChildren().add(usernameVBox);

        // TODO: add logged in user condition
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
                        return;
                    }
                }
            }
        });

        remove.setFill(removePattern);
        hBox.getChildren().add(remove);

        return hBox;
    }

    @FXML
    private void addPlayer() {
        if (this.playersContainer.getChildren().size() == 5) {
            this.message.setText("Maximum players is 5");
            this.message.setManaged(true);
            this.message.setVisible(true);
            return;
        }

        String usernameValue = this.playerUsername.getText();

        for (Node child : this.playersContainer.getChildren()) {
            if (child.getId().equals(usernameValue)) {
                this.message.setText("Request has been sent to \"" + usernameValue + "\"");
                this.message.setManaged(true);
                this.message.setVisible(true);
                return;
            }
        }

        // TODO: send request while adding
        // TODO: change to real users
        User user = new User(usernameValue, usernameValue, usernameValue);

        playersContainer.getChildren().add(createUserHBox(user));

        this.message.setManaged(false);
        this.message.setVisible(false);
    }

    @FXML
    private void back() {
        this.onExit();
        App.setRoot("mainPage");
    }

    // TODO: add accept request tasks and change the user condition

}
