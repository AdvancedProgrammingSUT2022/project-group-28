package views;

import controllers.TechnologyController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
        ArrayList<TechnologyTemplate> userFullTechnologyTemplates = TechnologyController.extractFullProgressTechnology();
        ArrayList<TechnologyTemplate> possibleTechnologyTemplates = TechnologyController.PossibleTechnology();


    }

}
