package views;

import controllers.TechnologyController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.civilization.Civilization;
import models.civilization.Technology;
import models.civilization.enums.TechnologyTemplate;

import java.util.ArrayList;

public class TechnologyPage extends PageController{
    @FXML
    private Button backButton;
    @FXML
    private VBox technologiesContainer;


    public void initialize(){
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = TechnologyController.PossibleTechnology();
        Technology currentTechnology = TechnologyController.getGame().getCurrentPlayer().getCurrentStudyTechnology();
        ArrayList<Technology> userTechnologies = TechnologyController.getGame().getCurrentPlayer().getStudiedTechnologies();
        for (TechnologyTemplate technologyTemplate: possibleTechnologyTemplates) {
            technologiesContainer.getChildren().add(createTechnologyItem(technologyTemplate));
        }
        //if(currentTechnology != null && currentTechnology.getTechnologyTemplate().equals(technologyTemplate)){
        //    node.getStyleClass().add("currently_researching");
        //    text.getStyleClass().add("current_technology_name");
        //}
    }
    public HBox createTechnologyItem(TechnologyTemplate technologyTemplate){
        HBox technologyItem = new HBox(15);
        technologyItem.setAlignment(Pos.CENTER_LEFT);

        Circle icon = new Circle(30);
        String address = App.class.getResource("../assets/image/technology/" + technologyTemplate.getFileName() + ".png").toExternalForm();
        icon.setFill(new ImagePattern(new Image(address)));
        technologyItem.getChildren().add(icon);

        Text name = new Text(technologyTemplate.getName());
        name.getStyleClass().add("normal_text");
        technologyItem.getChildren().add(name);

        Text turns = new Text();
        Civilization userCivilization = TechnologyController.getGame().getCurrentPlayer();
        Technology technology = userCivilization.getTechnologyByTechnologyTemplate(technologyTemplate);
        if(technology == null){
            turns.setText((int) Math.ceil((double)(technologyTemplate.getCost()) / TechnologyController.addEachTurnScienceBalance(userCivilization)) + " turns\n");

        }
        else{
            turns.setText((int) Math.ceil((double)(technologyTemplate.getCost()-technology.getProgress()) / TechnologyController.addEachTurnScienceBalance(userCivilization)) + " turns\n");
        }
        turns.getStyleClass().add("normal_text");
        technologyItem.getChildren().add(turns);

        return technologyItem;

    }






    @FXML
    private void back() {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }
}
