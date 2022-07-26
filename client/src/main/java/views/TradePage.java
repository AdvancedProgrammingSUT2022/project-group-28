package views;

import controllers.CivilizationController;
import controllers.GameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.civilization.Civilization;
import models.tiles.enums.ResourceTemplate;
import views.components.Hex;

public class TradePage extends PageController{

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
        CivilizationController.updateResources(source);
        CivilizationController.updateResources(target);
        for(ResourceTemplate template:source.getResources().keySet()){
            if (source.getResources().get(template) <= 0) continue;
            Label lbl = new Label(template.getName());
            ImageView img = new ImageView(Hex.getResourceimages().get(template).getImage());
            img.setFitHeight(36);
            img.setFitWidth(36);
            lbl.setGraphic(img);
            sourceOptions.add(lbl);
        }
        for(ResourceTemplate template:target.getResources().keySet()){
            if (source.getResources().get(template) <= 0) continue;
            Label lbl = new Label(template.getName());
            ImageView img = new ImageView(Hex.getResourceimages().get(template).getImage());
            lbl.setGraphic(img);
            targetOptions.add(lbl);
        }

        Label lbl = new Label("Coin");
        ImageView img = new ImageView(new Image(App.class.getResource("../images/coin.png").toString()));
        img.setFitHeight(36);
        img.setFitWidth(36);
        lbl.setGraphic(img);
        sourceOptions.add(lbl);
        targetOptions.add(lbl);

        tradeCombo.setItems(sourceOptions);
        withCombo.setItems(targetOptions);
    }

    public static void setTarget(Civilization targetCivilization) {
        target = targetCivilization;
    }

    @FXML
    private void back(){
        this.onExit();
        tradeCombo.getScene().getWindow().hide();
    }

    @FXML
    private void trade(){

    }
}
