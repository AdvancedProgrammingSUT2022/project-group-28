package views.components;

import controllers.NetworkController;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

    public enum Type {
        INFO("message_box_info"),
        ALERT("message_box_alert"),
        WARNING("message_box_warning");
        private String styleClass;
        Type(String styleClass) {this.styleClass = styleClass;}
    }

    private Rectangle remove;
    private Button firstChoice;
    private Button secondChoice;

    public MessageBox(String message, Type type) {
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add(type.styleClass);
        this.setLayoutX(0);
        this.setLayoutY(0);
        this.setSpacing(10);
        this.setMinWidth(350);
        this.setHeight(80);

        Text messageText = new Text(message);
        this.getChildren().add(messageText);

        remove = new Rectangle(30, 30);
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

    public MessageBox(String message, Type type, String firstChoice, String secondChoice) {
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add(type.styleClass);
        this.setLayoutX(0);
        this.setLayoutY(0);
        this.setMinWidth(350);
        this.setHeight(80);

        Text messageText = new Text(message);
        this.getChildren().add(messageText);

        this.firstChoice = new Button(firstChoice);
        this.firstChoice.getStyleClass().add("message_box_button");
        this.getChildren().add(this.firstChoice);
        this.secondChoice = new Button(secondChoice);
        this.secondChoice.getStyleClass().add("message_box_button");
        this.getChildren().add(this.secondChoice);

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
                NetworkController.getInstance().sendTradeResult(message, "N");
                MessageBox messageBox = (MessageBox) remove.getParent();
                VBox messageBoxContainer = (VBox) messageBox.getParent();
                messageBoxContainer.getChildren().remove(messageBox);
            }
                
        });

        this.firstChoice.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                NetworkController.getInstance().sendTradeResult(message, "Y");
                MessageBox messageBox = (MessageBox) remove.getParent();
                VBox messageBoxContainer = (VBox) messageBox.getParent();
                messageBoxContainer.getChildren().remove(messageBox);
            }
        });

        this.secondChoice.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                NetworkController.getInstance().sendTradeResult(message, "N");
                MessageBox messageBox = (MessageBox) remove.getParent();
                VBox messageBoxContainer = (VBox) messageBox.getParent();
                messageBoxContainer.getChildren().remove(messageBox);
            }
        });

        this.getChildren().add(remove);
    }

    public Rectangle getRemove() { return remove; }

    public Button getFirstChoice() {
        return firstChoice;
    }

    public Button getSecondChoice() {
        return secondChoice;
    }
}
