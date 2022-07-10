package views;

import controllers.GameController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import models.units.enums.UnitTemplate;
import views.enums.CityMessage;

public class ConstructionPage extends PageController {

    @FXML
    private ScrollPane constructionsContainer;

    @FXML
    public void initialize() {

    }

    @FXML
    public void back() {

    }

    private HBox getUnitItem(UnitTemplate unitTemplate) {
        HBox unitItem = new HBox();

        City city = GameController.getGame().getSelectedCity();

        Circle icon = new Circle(20);
        icon.setFill(new ImagePattern(new Image(App.class.getResource("../unit/" + unitTemplate.getFilename() + ".png").toExternalForm())));
        unitItem.getChildren().add(icon);

        Text name = new Text(unitTemplate.getName());
        unitItem.getChildren().add(name);

        Text cost = new Text("Cost: " + unitTemplate.getCost());
        unitItem.getChildren().add(cost);

        Button button = new Button();
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().startUnitConstruction(unitTemplate, city);
            }
        });
        // TODO: set invalid button
        switch (unitTemplate.checkPossibilityOfConstruction(city)) {
            case SUCCESS:
                button.setText("Construct");
                button.getStyleClass().add("valid_construction_button");
                break;
            case REQUIRED_RESOURCE:
                button.setText("Resource required");
                button.getStyleClass().add("invalid_construction_button");
                break;
            case UNHAPPY_PEOPLE:
                button.setText("Unhappy people");
                button.getStyleClass().add("invalid_construction_button");
                break;
            case CITY_NOT_GREW:
                button.setText("City is small");
                button.getStyleClass().add("invalid_construction_button");
                break;
            default:
                break;
        }
        
        return unitItem;
    }


}
