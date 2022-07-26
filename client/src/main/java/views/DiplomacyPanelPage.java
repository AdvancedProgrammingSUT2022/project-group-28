package views;

import controllers.GameController;
import controllers.NetworkController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.civilization.Civilization;
import views.components.MessageBox;

import java.util.ArrayList;

public class DiplomacyPanelPage extends PageController {
    @FXML
    private VBox diplomacyPanelWindow;
    @FXML
    private TextField messageField;
    @FXML
    private VBox diplomacyPanelContainer;
    @FXML
    private Button backButton;

    public void initialize() {
        ArrayList<Civilization> civilizations = GameController.getGame().getCivilizations();
        for (Civilization civilization : civilizations) {
            if (civilization.getUser().getId() != App.getCurrentUser().getId()) {
                diplomacyPanelContainer.getChildren().add(createDiplomacyItem(civilization));
            }
        }
    }

    private HBox createDiplomacyItem(Civilization civilization) {
        HBox diplomacyItem = new HBox(25);
        diplomacyItem.setAlignment(Pos.CENTER_LEFT);
        diplomacyItem.getStyleClass().add("diplomacyItem");

        Circle icon = new Circle(40);
        icon.setFill(new ImagePattern(civilization.getUser().getAvatar()));
        diplomacyItem.getChildren().add(icon);

        Text name = new Text(civilization.getUser().getNickname() + "  |  " + civilization.getCivilizationNames().getName());
        name.getStyleClass().add("civilization_name");
        diplomacyItem.getChildren().add(name);

        if (civilization.getCurrentCapital() != null) {
            Text capitalName = new Text("capital name  |  " + civilization.getCurrentCapital().getNAME());
            capitalName.getStyleClass().add("capital_name");
            diplomacyItem.getChildren().add(capitalName);
        }

        Button trade = new Button("Trade");
        trade.getStyleClass().add("trade_button");
        trade.setOnMouseClicked(event -> {
            tradeButtonClicked(event, civilization);
        });
        diplomacyItem.getChildren().add(trade);

        Button sendMessage = new Button("send message");
        sendMessage.getStyleClass().add("send_message_button");
        sendMessage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                sendMessage(civilization.getUser().getNickname());
            }
        });
        diplomacyItem.getChildren().add(sendMessage);

        return diplomacyItem;
    }

    private void sendMessage(String receiverNickname) {
        NetworkController.getInstance().sendInGameMessage(messageField.getText(), receiverNickname, MessageBox.Type.INFO);
        messageField.setText("");
    }


    public void back(MouseEvent mouseEvent) {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }

    public void tradeButtonClicked(MouseEvent mouseEvent,Civilization civilization) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(backButton.getScene().getWindow());
        TradePage.setSeller(civilization);
        Scene scene =  new Scene(App.loadFXML("tradePage"));
        stage.setScene(scene);
        stage.show();

    }
}
