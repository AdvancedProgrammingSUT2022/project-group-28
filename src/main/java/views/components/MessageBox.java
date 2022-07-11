package views.components;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import views.App;


public class MessageBox extends HBox {
    // TODO: add Notification object to constructor
    private static final ImagePattern removePattern = new ImagePattern(new Image(App.class.getResource("../assets/image/ui_icon/cross.png").toExternalForm()));
    private static final ImagePattern redRemovePattern = new ImagePattern(new Image(App.class.getResource("../assets/image/ui_icon/red_cross.png").toExternalForm()));

    private final Text message;

    public MessageBox(String message) {
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("message_box_warning");
        this.setLayoutX(0);
        this.setLayoutY(0);
        this.setMinWidth(350);
        this.setHeight(80);

        this.message = new Text(message);
        this.getChildren().add(this.message);

        Rectangle remove = new Rectangle(30, 30);
        remove.setFill(removePattern);
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
                MessageBox messageBox = (MessageBox) remove.getParent();
                VBox messageBoxContainer = (VBox) messageBox.getParent();
                messageBoxContainer.getChildren().remove(messageBox);
            }
        });

        this.getChildren().add(remove);

    }
}
