package views;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import views.components.MessageBox;

public class HUDController {
    private static HUDController instance;
    public static HUDController getInstance() {
        if (instance == null) {
            instance = new HUDController();
        }
        return instance;
    }

    private Button nextTurnButton;
    private VBox messageBoxContainer;
    private ScrollPane messageScrollPane;

    public void createHUD(Pane HUD) {
        createNextTurnButton(HUD);
        createMessageBoxContainer(HUD);
    }

    private void createNextTurnButton(Pane HUD) {
        nextTurnButton = new Button("Next Turn");
        nextTurnButton.setMinWidth(350);
        nextTurnButton.setMinHeight(50);
        nextTurnButton.setLayoutX(1200);
        nextTurnButton.setLayoutY(700);
        nextTurnButton.getStyleClass().add("next_turn");
        HUD.getChildren().add(nextTurnButton);


        nextTurnButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                messageBoxContainer.getChildren().add(0, new MessageBox());
            }
        });


    }

    private void createMessageBoxContainer(Pane HUD) {
        messageBoxContainer = new VBox(5);
        messageBoxContainer.setAlignment(Pos.CENTER);
        messageBoxContainer.setPrefWidth(350);

        MessageBox messageBox = new MessageBox();
        messageBoxContainer.getChildren().add(messageBox);

        messageScrollPane = new ScrollPane(messageBoxContainer);
        messageScrollPane.getStyleClass().add("message_box");
        messageScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messageScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageScrollPane.setMinWidth(375);
        messageScrollPane.setMaxHeight(290);
        messageScrollPane.setLayoutX(1200);
        messageScrollPane.setLayoutY(400);
        HUD.getChildren().add(messageScrollPane);
    }
}
