package views;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import views.components.*;
import views.components.console.ConsoleView;

import java.util.ArrayList;

public class HUDController {
    private static HUDController instance;
    public static HUDController getInstance() {
        if (instance == null) {
            instance = new HUDController();
        }
        return instance;
    }

    private static ArrayList<MessageBox> messageHistory = new ArrayList<>();

    private Button nextTurnButton;
    private Button consoleButton;
    private Button saveButton;
    private Button messageHistoryButton;
    private VBox messageBoxContainer;
    private ScrollPane messageScrollPane;
    private MiniMap miniMap;
    private InfoBar infoBar;
    private UnitInfo unitInfo;

    private CurrentTechnologyInfo currentTechnologyInfo;

    public void createHUD(Group HUD) {
        createNextTurnButton(HUD);
        createConsoleButton(HUD);
        createMessageHistoryButton(HUD);
        createMessageBoxContainer(HUD);
        createMiniMap(HUD);
        createInfoBar(HUD);
        createUnitInfo(HUD);
        createCurrentTechnologyPanel(HUD);
        createSaveButton(HUD);
    }

    public void addMessage(String message, MessageBox.Type type) {
        MessageBox messageBox = new MessageBox(message, type);
        this.messageBoxContainer.getChildren().add(0, messageBox);

        messageHistory.add(0,messageBox);
    }

    public MessageBox addMessage(String message, MessageBox.Type type, String firstChoice, String secondChoice) {
        MessageBox messageBox = new MessageBox(message, type, firstChoice, secondChoice);
        this.messageBoxContainer.getChildren().add(messageBox);

        messageHistory.add(0,messageBox);
        return messageBox;
    }

    private void createNextTurnButton(Group HUD) {
        nextTurnButton = new Button("Next Turn");
        nextTurnButton.setMinWidth(350);
        nextTurnButton.setMinHeight(50);
        nextTurnButton.setLayoutX(1200);
        nextTurnButton.setLayoutY(595);
        nextTurnButton.getStyleClass().add("next_turn");
        HUD.getChildren().add(nextTurnButton);


        nextTurnButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().nextTurn();
            }
        });

    }

    private void createConsoleButton(Group HUD) {
        consoleButton = new Button("Console");
        HUD.getChildren().add(consoleButton);
        consoleButton.setLayoutX(50);
        consoleButton.setLayoutY(140);
        consoleButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(nextTurnButton.getScene().getWindow());
                Scene scene =  ConsoleView.getInstance();
                scene.getStylesheets().add(App.class.getResource("../css/console.css").toExternalForm());
                stage.setTitle("Console");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();     
            }
        });
    }

    private void createMessageHistoryButton(Group HUD) {
        messageHistoryButton = new Button("Message History");
        messageHistoryButton.setLayoutX(50);
        messageHistoryButton.setLayoutY(200);
        messageHistoryButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().openMessageHistoryPage();
            }
        });
        HUD.getChildren().add(messageHistoryButton);
    }

    private void createMessageBoxContainer(Group HUD) {
        messageBoxContainer = new VBox(5);
        messageBoxContainer.setAlignment(Pos.CENTER);
        messageBoxContainer.setPrefWidth(350);

        messageScrollPane = new ScrollPane(messageBoxContainer);
        messageScrollPane.getStyleClass().add("message_box");
        messageScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messageScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageScrollPane.setMinWidth(375);
        messageScrollPane.setMaxHeight(290);
        messageScrollPane.setLayoutX(1200);
        messageScrollPane.setLayoutY(295);
        HUD.getChildren().add(messageScrollPane);
    }

    private void createMiniMap(Group HUD) {
        miniMap = new MiniMap(GameController.getGame().getCurrentPlayer());
        miniMap.setLayoutX(1200);
        miniMap.setLayoutY(650);
        HUD.getChildren().add(miniMap);
    }

    private void createInfoBar(Group HUD){
        infoBar = new InfoBar();
        infoBar.setLayoutX(0);
        infoBar.setLayoutY(0);
        HUD.getChildren().add(infoBar);
    }

    private void createUnitInfo(Group HUD) {
        unitInfo = new UnitInfo();
        unitInfo.setLayoutX(50);
        unitInfo.setLayoutY(650);
        HUD.getChildren().add(unitInfo);
    }

    private void createCurrentTechnologyPanel(Group HUD) {
        currentTechnologyInfo = new CurrentTechnologyInfo();
        HUD.getChildren().add(currentTechnologyInfo);
    }

    private void createSaveButton(Group HUD) {
        saveButton = new Button("Save");
        saveButton.setLayoutX(50);
        saveButton.setLayoutY(170);
        HUD.getChildren().add(saveButton);

        saveButton.setOnMouseClicked(value -> {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initOwner(nextTurnButton.getScene().getWindow());
            Scene scene =  new Scene(App.loadFXML("savePage"));
            stage.setScene(scene);
            stage.show();    
        });
    }

    public static ArrayList<MessageBox> getMessageHistory() { return messageHistory; }

    public UnitInfo getUnitInfo() {
        return unitInfo;
    }

    public MiniMap getMiniMap() { return miniMap; }

    public CurrentTechnologyInfo getCurrentTechnologyInfo() {
        return currentTechnologyInfo;
    }

    public InfoBar getInfoBar() {
        return infoBar;
    }
}
