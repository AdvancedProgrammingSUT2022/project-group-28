package views;

import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;


public class StartPage extends PageController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox textContainer;

    @FXML
    public void initialize() {
        textContainer.getChildren().add(createStartText());

        setupEventHandlers();
    }

    private void setupEventHandlers() {
        borderPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                StartPage.this.onExit();
                try {
                    App.setRoot("loginPage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // TODO: go to next page
            }
        });

        borderPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                StartPage.this.onExit();
                try {
                    App.setRoot("loginPage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // TODO: go to next page
            }
        });
    }

    public Text createStartText() {
        Text text = new Text("Press any key to start.");
        text.getStyleClass().add("start");

        Transition opacityUpdater = new Transition() {
            {
                setCycleCount(-1);
                setCycleDuration(Duration.millis(2000));
                setAutoReverse(true);
            }
            @Override
            protected void interpolate(double frac) {
                text.setOpacity(frac + 0.2);
            }
        };

        opacityUpdater.play();

        this.transitions.add(opacityUpdater);

        return text;
    }

}
