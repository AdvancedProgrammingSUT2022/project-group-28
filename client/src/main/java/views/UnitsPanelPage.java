package views;

import controllers.GameController;
import controllers.units.UnitController;
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
import models.units.Unit;

public class UnitsPanelPage extends PageController {
    @FXML
    private VBox unitsPanelContainer;
    @FXML
    private Button backButton;

    public void initialize(){
        Civilization civilization = GameController.getGame().getCurrentPlayer();
        Text information = new Text("                  |     name     |      state      |         coordinate      |    health   |   move state");
        unitsPanelContainer.getChildren().add(information);
        for (Unit unit: civilization.getUnits()) {
            unitsPanelContainer.getChildren().add(createUnitItem(unit));
        }
    }

    private HBox createUnitItem(Unit unit){
        HBox unitItem = new HBox(35);
        unitItem.setAlignment(Pos.CENTER_LEFT);
        unitItem.getStyleClass().add("unit_item");

        Circle icon = new Circle(30);
        String address = App.class.getResource("../assets/image/unit/" + unit.getUnitTemplate().getFilename() + ".png").toExternalForm();
        icon.setFill(new ImagePattern(new Image(address)));
        unitItem.getChildren().add(icon);

        Text unitName = new Text(unit.getUnitTemplate().getName());
        unitName.getStyleClass().add("unit_name");
        unitItem.getChildren().add(unitName);


        Text unitState = new Text(unit.getUnitState().toString());
        unitState.getStyleClass().add("unit_state");
        unitItem.getChildren().add(unitState);


        Text unitCoordinates = new Text("i : " + unit.getTile().getCoordinates()[0] + "  |  j : " + unit.getTile().getCoordinates()[1]);
        unitItem.getChildren().add(unitCoordinates);

        Text unitHealth = new Text(Integer.toString(unit.getHealth()));
        unitItem.getChildren().add(unitHealth);

        Circle moveUnitState = new Circle(15);
        String address2 = App.class.getResource(chooseMoveUnitStateImage(unit)).toExternalForm();
        moveUnitState.setFill(new ImagePattern(new Image(address2)));
        unitItem.getChildren().add(moveUnitState);

        return unitItem;
    }

    private String chooseMoveUnitStateImage(Unit unit){
        String stateColor = UnitController.getMoveUnitState(unit.getMovePoint(),unit.getUnitTemplate().getMovementPoint());
        String address = "";
        switch (stateColor){
            case "green":
                address =  "../assets/image/green_circle.png";
                break;
            case "yellow" :
                address = "../assets/image/yellow_circle.png";
                break;
            case "empty":
                address =  "../assets/image/white_circle.png";
                break;
        }
        return address;
    }

    @FXML
    private void back() {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }
}
