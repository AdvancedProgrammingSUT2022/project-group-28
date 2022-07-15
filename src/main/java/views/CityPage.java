package views;

import controllers.GameController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.civilization.City;


public class CityPage extends PageController {
    @FXML
    private VBox cityInfo;
    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        City city = GameController.getGame().getSelectedCity();
        Text cityName = new Text("Name: " + city.getNAME());
        cityName.getStyleClass().add("normal_text");
        Text founder = new Text("Founder: " + city.getCivilization().getCivilizationNames().getName());
        founder.getStyleClass().add("normal_text");
        Text unemployedCitizens = new Text("Unemployed Citizens: " + city.getCitizens());
        unemployedCitizens.getStyleClass().add("normal_text");
        Text hitPoint = new Text("Hit point:" + city.getHitPoint());
        hitPoint.getStyleClass().add("normal_text");
        Text foodBalance = new Text("Food balance: " + city.getFoodBalance());
        foodBalance.getStyleClass().add("normal_text");
        Text productionBalance = new Text("Production balance: " + city.getProductionBalance());
        productionBalance.getStyleClass().add("normal_text");

        cityInfo.getChildren().add(cityName);
        cityInfo.getChildren().add(founder);
        cityInfo.getChildren().add(unemployedCitizens);
        cityInfo.getChildren().add(hitPoint);
        cityInfo.getChildren().add(foodBalance);
        cityInfo.getChildren().add(productionBalance);
    }

    @FXML
    private void assignCitizen() {
        GamePage.getInstance().setMapState(GamePage.MapState.ASSIGN_CITIZEN);
        this.back();
        GamePage.getInstance().createMap(true);
    }

    @FXML
    private void buyTile() {
        GamePage.getInstance().setMapState(GamePage.MapState.BUY_TILE);
        this.back();
        GamePage.getInstance().createMap(true);
    }

    @FXML
    private void buyUnit() {
        GameMediator.getInstance().openBuyUnitMenu(backButton);
    }

    @FXML
    private void back() {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }

}
