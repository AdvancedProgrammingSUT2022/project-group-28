package views;

import controllers.CombatController;
import controllers.GameController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.units.Military;
import models.units.Ranged;
import models.units.Unit;

public class UnitPage extends PageController{
    @FXML
    private VBox unitInfo;
    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        Unit unit = GameController.getGame().getSelectedUnit();
        Text unitName = new Text("Name: " + unit.getUnitTemplate().getName());
        unitName.getStyleClass().add("normal_text");
        Text type = new Text("Type: " + unit.getUnitTemplate().getUnitType());
        type.getStyleClass().add("normal_text");
        Text movePoint = new Text("Move Point: " + unit.getMovePoint());
        movePoint.getStyleClass().add("normal_text");
        Text health = new Text("Health: " + unit.getHealth());
        health.getStyleClass().add("normal_text");

        unitInfo.getChildren().add(unitName);
        unitInfo.getChildren().add(type);
        unitInfo.getChildren().add(movePoint);
        unitInfo.getChildren().add(health);

        if (unit.getMoveTarget() != null) {
            Text moveTarget = new Text("Move Target: " + unit.getMoveTarget());
            moveTarget.getStyleClass().add("normal_text");
            unitInfo.getChildren().add(moveTarget);
        }
        
        if(unit instanceof Military){
            Text rawCombatStrength = new Text("Raw Combat Strength: " + unit.getCombatStrength());
            rawCombatStrength.getStyleClass().add("normal_text");
            Text combatStrength = new Text("Combat Strength: " + unit.getCombatStrength());
            combatStrength.getStyleClass().add("normal_text");
            unitInfo.getChildren().add(rawCombatStrength);
            unitInfo.getChildren().add(combatStrength);
        }
        if(unit instanceof Ranged){
            Text range = new Text("Range: " + ((Ranged) unit).getUnitTemplate().getRange());
            range.getStyleClass().add("normal_text");
            Text rawRangedCombatStrength = new Text("Raw Ranged Combat Strength: " + ((Ranged) unit).getRangedCombatStrength());
            rawRangedCombatStrength.getStyleClass().add("normal_text");
            Text rangedCombatStrength = new Text("Ranged Combat Strength: " + CombatController.getCombatStrength((Ranged) unit, true));
            rangedCombatStrength.getStyleClass().add("normal_text");
            unitInfo.getChildren().addAll(range, rawRangedCombatStrength, rangedCombatStrength);
        }
    }

    @FXML
    private void back() {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }
}
