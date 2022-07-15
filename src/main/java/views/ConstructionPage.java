package views;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.Constructable;
import models.civilization.City;
import models.civilization.Construction;
import models.civilization.enums.BuildingTemplate;
import models.units.enums.UnitTemplate;
import views.enums.CityMessage;

public class ConstructionPage extends PageController {

    @FXML
    private VBox constructionsContainer;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        City city = GameController.getGame().getSelectedCity();
        for (UnitTemplate unitTemplate : UnitTemplate.values()) {
            if (unitTemplate.checkPossibilityOfConstruction(city) != CityMessage.REQUIRED_TECHNOLOGY) {
                constructionsContainer.getChildren().add(createUnitItem(unitTemplate));
            }
        }

        for (BuildingTemplate buildingTemplate : BuildingTemplate.values()) {
            if (buildingTemplate.isAvailableToBuild(city)) {
                constructionsContainer.getChildren().add(createBuildingItem(buildingTemplate));
            }
        }
    }

    @FXML
    private void back() {
        this.onExit();
        this.backButton.getParent().getScene().getWindow().hide();
    }

    private HBox createUnitItem(UnitTemplate unitTemplate) {
        HBox unitItem = new HBox(15);
        unitItem.setAlignment(Pos.CENTER_LEFT);

        Circle icon = new Circle(30);
        String address = App.class.getResource("../assets/image/unit/" + unitTemplate.getFilename() + ".png").toExternalForm();
        icon.setFill(new ImagePattern(new Image(address)));
        unitItem.getChildren().add(icon);

        Text name = new Text(unitTemplate.getName());
        name.getStyleClass().add("normal_text");
        unitItem.getChildren().add(name);

        Text cost = new Text("Cost: " + unitTemplate.getCost());
        cost.getStyleClass().add("normal_text");
        unitItem.getChildren().add(cost);

        Button button = new Button();

        City city = GameController.getGame().getSelectedCity();
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().startConstruction(unitTemplate, city);
                ConstructionPage.this.back();
            }
        });

        switch (unitTemplate.checkPossibilityOfConstruction(city)) {
            case SUCCESS:
                button.setText("Construct");
                button.getStyleClass().add("valid_construction_button");
                break;
            case REQUIRED_RESOURCE:
                button.setText("Required " + unitTemplate.getRequiredResource().getName());
                button.setDisable(true);
                button.getStyleClass().add("invalid_construction_button");
                break;
            case UNHAPPY_PEOPLE:
                button.setText("Unhappy people");
                button.setDisable(true);
                button.getStyleClass().add("invalid_construction_button");
                break;
            case CITY_NOT_GREW:
                button.setText("City is small");
                button.setDisable(true);
                button.getStyleClass().add("invalid_construction_button");
                break;
            default:
                break;
        }
        
        unitItem.getChildren().add(button);

        return unitItem;
    }

    private HBox createBuildingItem(BuildingTemplate buildingTemplate) {
        HBox buildingItem = new HBox(15);
        buildingItem.setAlignment(Pos.CENTER_LEFT);

        Circle icon = new Circle(30);
        String address = App.class.getResource("../assets/image/building/" + buildingTemplate.getFilename() + ".png").toExternalForm();
        icon.setFill(new ImagePattern(new Image(address)));
        buildingItem.getChildren().add(icon);

        Text name = new Text(buildingTemplate.getName());
        name.getStyleClass().add("normal_text");
        buildingItem.getChildren().add(name);

        Text cost = new Text("Cost: " + buildingTemplate.getCost());
        cost.getStyleClass().add("normal_text");
        buildingItem.getChildren().add(cost);

        Button button = new Button("Construct");
        button.getStyleClass().add("valid_construction_button");

        City city = GameController.getGame().getSelectedCity();
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().startConstruction(buildingTemplate, city);
                ConstructionPage.this.back();
            }
        });

        buildingItem.getChildren().add(button);

        return buildingItem;
    }


}
