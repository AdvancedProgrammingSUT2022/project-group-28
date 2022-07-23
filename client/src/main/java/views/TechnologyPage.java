package views;

import controllers.TechnologyController;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import models.civilization.Civilization;
import models.civilization.Technology;
import models.civilization.enums.TechnologyTemplate;
import views.components.TechnologyInfo;

import java.util.ArrayList;

public class TechnologyPage extends PageController{
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button technologyTreePage;
    @FXML
    private Button backButton;
    @FXML
    private VBox technologiesContainer;

    private TechnologyInfo technologyInfo;


    public void initialize(){
        technologyInfo = new TechnologyInfo();

        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = TechnologyController.PossibleTechnology();
        Technology currentTechnology = TechnologyController.getGame().getCurrentPlayer().getCurrentStudyTechnology();
        ArrayList<Technology> userTechnologies = TechnologyController.getGame().getCurrentPlayer().getStudiedTechnologies();
        for (TechnologyTemplate technologyTemplate: possibleTechnologyTemplates) {
            technologiesContainer.getChildren().add(createTechnologyItem(technologyTemplate));
        }
    }
    public HBox createTechnologyItem(TechnologyTemplate technologyTemplate){
        HBox technologyItem = new HBox(15);
        technologyItem.setAlignment(Pos.CENTER_LEFT);
        technologyItem.getStyleClass().add("technology_item");

        Circle icon = new Circle(35);
        String address = App.class.getResource("../assets/image/technology/" + technologyTemplate.getFileName() + ".png").toExternalForm();
        icon.setFill(new ImagePattern(new Image(address)));
        technologyItem.getChildren().add(icon);

        String turns = "";
        Civilization userCivilization = TechnologyController.getGame().getCurrentPlayer();
        Technology technology = userCivilization.getTechnologyByTechnologyTemplate(technologyTemplate);
        if(technology == null){
            turns = ((int) Math.ceil((double)(technologyTemplate.getCost()) / TechnologyController.addEachTurnScienceBalance(userCivilization)) + " turns");

        }
        else{
            turns = ((int) Math.ceil((double)(technologyTemplate.getCost()-technology.getProgress()) / TechnologyController.addEachTurnScienceBalance(userCivilization)) + " turns");
        }

        Text name = new Text(technologyTemplate.getName() + "  |  " + turns);
        TechnologyTreePage.checkColorOfNode(technologyItem  , name , technologyTemplate);

        technologyItem.getChildren().add(name);

        technologyItem.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                technologyInfo.update(technologyTemplate);
                technologyInfo.setLayoutX(115);
                technologyInfo.setLayoutY(event.getSceneY());
                borderPane.getChildren().add(technologyInfo);
            }
        });
        technologyItem.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                borderPane.getChildren().remove(technologyInfo);
            }
        });

        technologyItem.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                technologiesContainer.getChildren().clear();
                GameMediator.getInstance().startStudyTechnology(technologyTemplate);
                GamePage.getInstance().updateGamePage();
                initialize();

            }
        });
        return technologyItem;

    }


    @FXML
    private void back() {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }

    public void goThechnologyTreePage(MouseEvent mouseEvent) {
        GameMediator.getInstance().openTechnologyTree();
    }
}
