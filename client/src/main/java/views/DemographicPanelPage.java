package views;

import controllers.GameController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.civilization.Civilization;

import java.util.ArrayList;


public class DemographicPanelPage extends PageController {
    @FXML
    private VBox demographicPanelContainer;
    @FXML
    private Button backButton;

    public void initialize(){
        ArrayList<Civilization> civilizations = GameController.getGame().getCivilizations();
        for (Civilization civilization: civilizations) {
            demographicPanelContainer.getChildren().add(createDemographicItem(civilization));
        }
    }

    public HBox createDemographicItem(Civilization civilization){
        HBox demographicItem = new HBox(20);
        demographicItem.setAlignment(Pos.CENTER_LEFT);
        demographicItem.getStyleClass().add("demographicItem");

        Circle icon = new Circle(30);
        icon.setFill(new ImagePattern(civilization.getUser().getAvatar()));
        demographicItem.getChildren().add(icon);

        Text name = new Text("name  |  " + civilization.getCivilizationNames().getName());
        name.getStyleClass().add("demographic_name");
        demographicItem.getChildren().add(name);

        Text capitalName = new Text("capital name  |  "  + civilization.getCurrentCapital().getNAME());
        capitalName.getStyleClass().add("capital_name");
        demographicItem.getChildren().add(capitalName);

        Text happiness = new Text("hapiness  |  " + civilization.getHappiness());
        demographicItem.getChildren().add(happiness);

        return demographicItem;
    }

    public void back(MouseEvent mouseEvent) {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }
}
