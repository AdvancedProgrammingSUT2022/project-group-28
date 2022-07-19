package views;

import java.util.ArrayList;

import controllers.GameController;
import controllers.TileController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.tiles.enums.ImprovementTemplate;
import models.units.Worker;


public class BuildPage extends PageController {
    @FXML
    private VBox buildContainer;
    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        Worker worker = (Worker)GameController.getGame().getSelectedUnit();
        ArrayList <ImprovementTemplate> possibleImprovements = TileController.getTilePossibleImprovements(worker.getTile());
        for (ImprovementTemplate improvementTemplate : possibleImprovements) {
            buildContainer.getChildren().add(createBuildItem(improvementTemplate));
        }
    }

    @FXML
    private void back() {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }

    private HBox createBuildItem(ImprovementTemplate improvementTemplate) {
        HBox unitItem = new HBox(15);
        unitItem.setAlignment(Pos.CENTER_LEFT);

        Text name = new Text(improvementTemplate.getName());
        name.getStyleClass().add("normal_text");
        unitItem.getChildren().add(name);

        Text cost = new Text("Turn Cost: " + improvementTemplate.getTurnCost());
        cost.getStyleClass().add("normal_text");
        unitItem.getChildren().add(cost);

        Button button = new Button();

        button.setText("Build");
        button.getStyleClass().add("valid_buy_button");

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().build(improvementTemplate);
                BuildPage.this.back();
            }
        });

        unitItem.getChildren().add(button);

        return unitItem;
    }
}
