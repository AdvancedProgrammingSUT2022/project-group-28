package views;

import controllers.TechnologyController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.Game;
import models.civilization.Civilization;
import models.civilization.Technology;
import models.civilization.enums.TechnologyTemplate;

import java.util.ArrayList;
import java.util.HashMap;

public class TechnologyTreePage {
    @FXML
    private AnchorPane anchorPane;
    public static final HashMap<TechnologyTemplate, ImagePattern> technologies = new HashMap<>();

    static {
        for (TechnologyTemplate technologyTemplate: TechnologyTemplate.values()) {
            Image image = new Image(App.class.getResource("../assets/image/technology/" + technologyTemplate.getFileName() + ".png").toExternalForm());
            technologies.put(technologyTemplate , new ImagePattern(image));
        }
    }

    public void initialize(){

        for (TechnologyTemplate technologyTemplate:TechnologyTemplate.values()) {
            HBox node = new HBox(25);
            node.setAlignment(Pos.CENTER_LEFT);
            node.setPrefWidth(250);
            node.setLayoutX(technologyTemplate.getX());
            node.setLayoutY(technologyTemplate.getY());
            checkColorOfNode(node , technologyTemplate);
            node.getStyleClass().add("node");
            Circle picture = new Circle(35);
            picture.setFill(technologies.get(technologyTemplate));
            Text technologyName = new Text(technologyTemplate.getName());
            node.getChildren().addAll(picture , technologyName);
            anchorPane.getChildren().add(node);
        }

    }

    private void checkColorOfNode(HBox node, TechnologyTemplate technologyTemplate) {
        ArrayList<TechnologyTemplate> userFullTechnologyTemplates = TechnologyController.extractFullProgressTechnology();
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = TechnologyController.PossibleTechnology();
        Technology currentTechnology = TechnologyController.getGame().getCurrentPlayer().getCurrentStudyTechnology();
        if(currentTechnology != null && TechnologyController.getGame().getCurrentPlayer().getCurrentStudyTechnology().getTechnologyTemplate().equals(technologyTemplate)){
            node.getStyleClass().add("currently_researching");
        }
        else if(userFullTechnologyTemplates.contains(technologyTemplate)){
            node.getStyleClass().add("researched_node");
        }
        else if(possibleTechnologyTemplates.contains(technologyTemplate)){
            node.getStyleClass().add("available_node");
        }
        else{
            node.getStyleClass().add("not_available_node");
        }
    }

}
