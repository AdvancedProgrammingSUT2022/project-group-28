package views;

import controllers.CivilizationController;
import controllers.GameController;
import controllers.NetworkController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Trade;
import models.civilization.Civilization;
import models.tiles.enums.ResourceTemplate;

public class TradePage extends PageController{

    @FXML
    private Label errorLabel;

    @FXML
    private ComboBox<String> tradeCombo, withCombo;

    @FXML
    private TextField tradeNumber,withNumber;

    private static Civilization seller;

    private Civilization customer;

    public void initialize() {
        customer = GameController.getGame().getCurrentPlayer();
        ObservableList<String> sellerOptions = FXCollections.observableArrayList();
        ObservableList<String> customerOptions = FXCollections.observableArrayList();

        CivilizationController.updateResources(seller);
        CivilizationController.updateResources(customer);
        for(ResourceTemplate template:seller.getResources().keySet()){
            if (seller.getResources().get(template) <= 0) continue;
            sellerOptions.add(template.getName()  + ": " + seller.getResources().get(template));
        }
        for(ResourceTemplate template:customer.getResources().keySet()){
            if (customer.getResources().get(template) <= 0) continue;
            customerOptions.add(template.getName() + ": " + customer.getResources().get(template));
        }

        sellerOptions.add("Coin: " + seller.getGold());
        customerOptions.add("Coin: " + customer.getGold());

        tradeCombo.setItems(customerOptions);
        withCombo.setItems(sellerOptions);
    }

    public static void setSeller(Civilization targetCivilization) {
        seller = targetCivilization;
    }

    @FXML
    private void back(){
        this.onExit();
        tradeCombo.getScene().getWindow().hide();
    }

    @FXML
    private void trade(){
        int sellerCount;
        int customerCount;
        Trade trade = null;

        if (tradeCombo.getValue() == null || withCombo.getValue() == null){
            errorLabel.setText("Please select a resource to trade");
        } 
        try{
            customerCount = Integer.parseInt(tradeNumber.getText());
            sellerCount = Integer.parseInt(withNumber.getText());
        }catch(NumberFormatException e){
            errorLabel.setText("Please enter a valid number");
            return;
        }
        if (customerCount <= 0 || sellerCount <= 0){
            errorLabel.setText("Please enter a valid number");
            return;
        }
        if (tradeCombo.getValue() == null || withCombo.getValue() == null){
            errorLabel.setText("Please select a resource to trade");
            return;
        }
        if (tradeCombo.getValue().split(":")[0].equals(withCombo.getValue().split(":")[0])){
            errorLabel.setText("Please select different resources");
            //return;
            //TODO : uncomment this
        }
        if(tradeCombo.getValue().startsWith("Coin:")){
            if(customerCount > customer.getGold()){
                errorLabel.setText("You do not have enough resource");
                return;
            }
            if (withCombo.getValue().startsWith("Coin:")){
                if (sellerCount > seller.getGold()){
                    errorLabel.setText("Seller do not have enough resource");
                    return;
                }
                trade = new Trade(customer,seller, null, null,customerCount, sellerCount);
                GameController.getGame().getTrades().add(trade);
                errorLabel.setText("Request sent");
            }
            else{
                ResourceTemplate resource = getResources(withCombo.getValue());
                if(seller.getResources().get(resource) < sellerCount){
                    errorLabel.setText("Seller do not have enough resource");
                    return;
                }
                trade = new Trade(customer,seller, null, resource,customerCount, sellerCount);
                GameController.getGame().getTrades().add(trade);
                errorLabel.setText("Request sent");
            }
        }
        for (ResourceTemplate resourceTemplate : ResourceTemplate.values()) {
            if (tradeCombo.getValue().startsWith(resourceTemplate.getName())) {
                if (customerCount > customer.getResources().get(resourceTemplate)) {
                    errorLabel.setText("You do not have enough resource");
                    return;
                }
                if (withCombo.getValue().startsWith("Coin:")){
                    if (sellerCount > seller.getGold()){
                        errorLabel.setText("Seller do not have enough resource");
                        return;
                    }
                    trade = new Trade(customer,seller, resourceTemplate, null,customerCount, sellerCount);
                    GameController.getGame().getTrades().add(trade);
                    errorLabel.setText("Request sent");
                } else{
                    ResourceTemplate resource = getResources(withCombo.getValue());
                    if(seller.getResources().get(resource) < sellerCount){
                        errorLabel.setText("Seller do not have enough resource");
                        return;
                    }
                    trade = new Trade(customer,seller, resourceTemplate, resource,customerCount, sellerCount);
                    GameController.getGame().getTrades().add(trade);
                    errorLabel.setText("Request sent");
                }

            } 
        }
        if (trade != null){
            NetworkController.getInstance().sendTradeRequest(trade);
        }
        
        
    }

    private ResourceTemplate getResources(String option){
        for (ResourceTemplate resourceTemplate : ResourceTemplate.values()) {
            if (tradeCombo.getValue().startsWith(resourceTemplate.getName())) {
                return resourceTemplate;
            }
        }
        return null;
    }
}
