package views;

import controllers.GameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import models.civilization.Civilization;
import models.tiles.enums.ResourceTemplate;
import views.components.Hex;

public class TradePage extends PageController{
    @FXML
    private Button backButton, tradeButton;

    @FXML
    private Label label;

    @FXML
    private ComboBox<Label> tradeCombo, withCombo;

    private static Civilization target;

    private Civilization source;

    public void initialize() {
        source = GameController.getGame().getCurrentPlayer();
        ObservableList<Label> sourceOptions = FXCollections.observableArrayList();
        ObservableList<Label> targetOptions = FXCollections.observableArrayList();
        for(ResourceTemplate template:source.getResources().keySet()){
            Label lbl = new Label(template.getName());
            ImageView img = new ImageView(Hex.getResourceimages().get(template).getImage());
            lbl.setGraphic(img);
        }
        for(ResourceTemplate template:target.getResources().keySet()){
            Label lbl = new Label(template.getName());
            ImageView img = new ImageView(Hex.getResourceimages().get(template).getImage());
            lbl.setGraphic(img);
        }
        tradeCombo.setItems(sourceOptions);
        withCombo.setItems(targetOptions);

    }

    public static void setTarget(Civilization targetCivilization) {
        target = targetCivilization;
    }

    @FXML
    private void back(){
        this.onExit();
        backButton.getScene().getWindow().hide();
    }

    @FXML
    private void trade(){

    }
}
