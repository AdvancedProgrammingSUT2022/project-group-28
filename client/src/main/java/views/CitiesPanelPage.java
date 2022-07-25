package views;

import controllers.GameController;
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
import models.civilization.City;
import models.civilization.Civilization;

public class CitiesPanelPage extends PageController {
    @FXML
    private Button backButton;
    @FXML
    private VBox citiesPanelContainer;

    public void initialize(){
        Civilization civilization = App.getCurrentUserCivilization();
        if(civilization.getCities().size() != 0){
            citiesPanelContainer.getChildren().add(createCityItem(civilization.getCurrentCapital()));

            for (City city: civilization.getCities()) {
                if(!civilization.getCities().contains(civilization.getCurrentCapital()) && civilization.getCities().size() > 1){
                    citiesPanelContainer.getChildren().add(createCityItem(city));
                }
            }
        }
    }

    private HBox createCityItem(City city){
        HBox cityItem = new HBox(25);
        cityItem.setAlignment(Pos.CENTER_LEFT);

        Circle icon = new Circle(30);
        String address = "";
        if(city.equals(city.getCivilization().getCurrentCapital())){
            address = App.class.getResource("../assets/image/capital_city.png").toExternalForm();
            cityItem.getStyleClass().add("capital_city_item");
        }
        else {
            address = App.class.getResource("../assets/image/city.png").toExternalForm();
            cityItem.getStyleClass().add("city_item");
        }
        icon.setFill(new ImagePattern(new Image(address)));
        cityItem.getChildren().add(icon);

        Text cityName= new Text("name  |  " + city.getNAME());
        cityName.getStyleClass().add("city_name");
        cityItem.getChildren().add(cityName);

        Text population = new Text("population  |  " + city.getPopulation());
        cityItem.getChildren().add(population);

        Text combatStrength = new Text("combat strength  |  " + city.getCombatStrength());
        cityItem.getChildren().add(combatStrength);


        return cityItem;
    }

    public void back(MouseEvent mouseEvent) {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }
}
