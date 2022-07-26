package views;

import controllers.CivilizationController;
import controllers.GameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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

    private static Civilization customer;

    private Civilization seller;

    public void initialize() {
        seller = GameController.getGame().getCurrentPlayer();
        ObservableList<Label> sellerOptions = FXCollections.observableArrayList();
        ObservableList<Label> customerOptions = FXCollections.observableArrayList();
        tradeCombo.setButtonCell(new ListCell<Label>() {
            @Override
            protected void updateItem(Label item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getText());
                    setGraphic(item.getGraphic());
                }
            }
        });

        withCombo.setButtonCell(new ListCell<Label>() {
            @Override
            protected void updateItem(Label item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getText());
                    setGraphic(item.getGraphic());
                }
            }
        });
        CivilizationController.updateResources(seller);
        CivilizationController.updateResources(customer);
        for(ResourceTemplate template:seller.getResources().keySet()){
            if (seller.getResources().get(template) <= 0) continue;
            Label lbl = new Label(template.getName());
            ImageView img = new ImageView(Hex.getResourceimages().get(template).getImage());
            img.setFitHeight(36);
            img.setFitWidth(36);
            lbl.setGraphic(img);
            sellerOptions.add(lbl);
        }
        for(ResourceTemplate template:customer.getResources().keySet()){
            if (customer.getResources().get(template) <= 0) continue;
            Label lbl = new Label(template.getName());
            ImageView img = new ImageView(Hex.getResourceimages().get(template).getImage());
            img.setFitHeight(36);
            img.setFitWidth(36);
            lbl.setGraphic(img);
            customerOptions.add(lbl);
        }

        Label lbl = new Label("Coin");
        ImageView img = new ImageView(new Image(App.class.getResource("../images/coin.png").toString()));
        img.setFitHeight(36);
        img.setFitWidth(36);
        lbl.setGraphic(img);
        sellerOptions.add(lbl);
        customerOptions.add(lbl);

        tradeCombo.setItems(sellerOptions);
        withCombo.setItems(customerOptions);
    }

    public static void setTarget(Civilization targetCivilization) {
        customer = targetCivilization;
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
