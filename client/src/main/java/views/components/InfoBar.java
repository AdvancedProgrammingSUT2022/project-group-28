package views.components;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.civilization.Civilization;
import views.App;
import views.GameMediator;
import views.components.console.ConsoleView;


public class InfoBar extends Group {
    private BorderPane infoBar;
    private HBox data;
    private HBox otherData;
    private Circle scienceIcon;
    private Text science;
    private Circle goldIcon;
    private Text gold;
    private Circle happinessIcon;
    private Text happiness;
    private Circle saveButton;
    private Circle consoleButton;
    private Circle messageBoxButton;
    private Circle unitsPanel;
    private Button unitsPanelButton;
    private Circle citiesPanel;
    private Button citiesPanelButton;
    private Circle demographicPanel;
    private Button demographicPanelButton;
    private Text turn;
    private Civilization civilization;



    public InfoBar(){
        civilization = GameController.getGame().getCurrentPlayer();

        infoBar = new BorderPane();
        infoBar.getStyleClass().add("info_bar");

        data = new HBox(15);
        data.setAlignment(Pos.CENTER);
        data.getStyleClass().add("data_section");

        otherData = new HBox(15);
        otherData.setAlignment(Pos.CENTER);
        otherData.getStyleClass().add("other_data_section");

        scienceIcon = new Circle(10);
        scienceIcon.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/science_icon.png").toExternalForm())));

        science = new Text("+" + civilization.getScienceBalance());
        science.getStyleClass().add("science_text");

        goldIcon = new Circle(10);
        goldIcon.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/gold_icon.png").toExternalForm())));

        gold = new Text(civilization.getGold() + " ( " + makeSign() + civilization.getGoldBalance() + " )");
        gold.getStyleClass().add("gold_text");

        happinessIcon = new Circle(10);
        happinessIcon.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/happiness_icon.png").toExternalForm())));

        happiness = new Text(Integer.toString(civilization.getHappiness()));
        happiness.getStyleClass().add("happiness_text");

        saveButton = new Circle(15);
        saveButton.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/save_icon.png").toExternalForm())));
        saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.initOwner(infoBar.getScene().getWindow());
                Scene scene =  new Scene(App.loadFXML("savePage"));
                stage.setScene(scene);
                stage.show();
            }
        });

        consoleButton = new Circle(15);
        consoleButton.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/console_icon.png").toExternalForm())));
        consoleButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(infoBar.getScene().getWindow());
                Scene scene =  ConsoleView.getInstance();
                scene.getStylesheets().add(App.class.getResource("../css/console.css").toExternalForm());
                stage.setTitle("Console");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        });

        messageBoxButton = new Circle(15);
        messageBoxButton.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/message_box_icon.png").toExternalForm())));
        messageBoxButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().openMessageHistoryPage();
            }
        });

        unitsPanel = new Circle(15);
        unitsPanel.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/units_panel_icon.png").toExternalForm())));
        unitsPanelButton = new Button("units panel");
        unitsPanelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().openUnitsPanel();
            }
        });
        unitsPanelButton.getStyleClass().add("panel_button");

        citiesPanel = new Circle(15);
        citiesPanel.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/cities_panel_icon.png").toExternalForm())));
        citiesPanelButton = new Button("cities panel");
        citiesPanelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().openCitiesPanel();
            }
        });
        citiesPanelButton.getStyleClass().add("panel_button");

        demographicPanel = new Circle(15);
        demographicPanel.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/demographic_panel_icon.png").toExternalForm())));
        demographicPanelButton = new Button("demographic panel");
        demographicPanelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().openDemographicPanel();
            }
        });
        demographicPanelButton.getStyleClass().add("panel_button");

        turn = new Text("Turn : 0");
        turn.getStyleClass().add("turn_number");

        data.getChildren().addAll(scienceIcon,science,goldIcon,gold,happinessIcon,happiness,saveButton,consoleButton,messageBoxButton);

        otherData.getChildren().addAll(unitsPanel,unitsPanelButton,citiesPanel,citiesPanelButton ,demographicPanel,demographicPanelButton,turn);

        infoBar.setLeft(data);
        infoBar.setRight(otherData);
        this.getChildren().add(infoBar);
    }


    public void update(){
        civilization = GameController.getGame().getCurrentPlayer();
        science.setText("+" + civilization.getScienceBalance());
        gold.setText(civilization.getGold() + " ( " + makeSign() + civilization.getGoldBalance() + " )");
        happiness.setText(Integer.toString(civilization.getHappiness()));
        turn.setText("Turn : " + GameController.getGame().getTurnNumber());
    }

    private String makeSign(){
        if(civilization.getGoldBalance() > 0){
            return "+";
        }
       return "";
    }
}
