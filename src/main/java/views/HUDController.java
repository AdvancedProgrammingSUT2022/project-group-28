package views;

import controllers.GameController;
import controllers.TechnologyController;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.civilization.Technology;
import views.components.MessageBox;
import views.components.MiniMap;
import views.components.UnitInfo;

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
    private MiniMap miniMap;
    private UnitInfo unitInfo;

    public void createHUD(Group HUD) {

        createNextTurnButton(HUD);
        createMessageBoxContainer(HUD);
        createMiniMap(HUD);
        createUnitInfo(HUD);
        createCurrentTechnologyPanel(HUD);
        createTechnologyTreeButton(HUD);
        this.addMessage("hit", MessageBox.Type.INFO, "shit", "fuck");
    }

    public void addMessage(String message, MessageBox.Type type) {
        this.messageBoxContainer.getChildren().add(0, new MessageBox(message, type));
    }

    public MessageBox addMessage(String message, MessageBox.Type type, String firstChoice, String secondChoice) {
        MessageBox messageBox = new MessageBox(message, type, firstChoice, secondChoice);
        this.messageBoxContainer.getChildren().add(messageBox);
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

    private void createUnitInfo(Group HUD) {
        unitInfo = new UnitInfo();
        unitInfo.setLayoutX(50);
        unitInfo.setLayoutY(650);
        HUD.getChildren().add(unitInfo);
    }

    private void createCurrentTechnologyPanel(Group HUD) {
        HBox currentTechnologyPanel = new HBox(25);
        currentTechnologyPanel.getStyleClass().add("current_technology_panel");
        currentTechnologyPanel.setAlignment(Pos.CENTER_LEFT);
        currentTechnologyPanel.setPrefWidth(350);
        currentTechnologyPanel.setLayoutX(30);
        currentTechnologyPanel.setLayoutY(20);
        //ProgressIndicator currentTechnologyProgress = new ProgressIndicator();
        Circle currentTechnologyPicture = new Circle(50);
        Text currentTechnologyName = new Text() ;
        currentTechnologyName.getStyleClass().add("current_technology_name");

        String imageFileName = "";
        String address = "";
        Technology currentStudyTechnology = TechnologyController.getGame().getCurrentPlayer().getCurrentStudyTechnology();
        if(currentStudyTechnology != null){
            imageFileName = TechnologyController.getGame().getCurrentPlayer().getCurrentStudyTechnology().getTechnologyTemplate().getFileName();
            address = App.class.getResource("../assets/image/technology/" + imageFileName + ".png").toExternalForm();
            //currentTechnologyProgress.setProgress((double)currentStudyTechnology.getProgress() / currentStudyTechnology.getTechnologyTemplate().getCost());
            currentTechnologyName.setText(currentStudyTechnology.getTechnologyTemplate().getName());
        }
        else {
            address = App.class.getResource("../assets/image/technology/agriculture.png").toExternalForm();
            currentTechnologyName.setText("Agriculture");
        }
        ImagePattern picture = new ImagePattern(new Image(address));
        currentTechnologyPicture.setFill(picture);




        currentTechnologyPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().openTechnologyTree();
            }
        });
        currentTechnologyPanel.getChildren().addAll(currentTechnologyPicture , currentTechnologyName);
        HUD.getChildren().add(currentTechnologyPanel);
    }
    private void createTechnologyTreeButton(Group HUD) {
        Button technologyPanel = new Button("open Technology tree");
        technologyPanel.getStyleClass().add("technology_tree_button");
        technologyPanel.setLayoutX(50);
        technologyPanel.setLayoutY(200);
        technologyPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().openTechnologyTree();
            }
        });
        HUD.getChildren().add(technologyPanel);
    }

    public UnitInfo getUnitInfo() {
        return unitInfo;
    }

    public MiniMap getMiniMap() { return miniMap; }
}
