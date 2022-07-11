package views;

import controllers.GameController;
import javafx.event.EventHandler;
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
import models.units.enums.UnitTemplate;
import views.enums.CityMessage;


public class BuyUnitPage extends PageController {
    @FXML
    private VBox unitsContainer;
    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        City city = GameController.getGame().getSelectedCity();
        for (UnitTemplate unitTemplate : UnitTemplate.values()) {
            if (unitTemplate.checkPossibleToBuy(city) != CityMessage.REQUIRED_TECHNOLOGY) {
                this.unitsContainer.getChildren().add(createUnitItem(unitTemplate));
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
                GameMediator.getInstance().buyUnit(city, unitTemplate);
                BuyUnitPage.this.back();
            }
        });

        switch (unitTemplate.checkPossibleToBuy(city)) {
            case SUCCESS:
                button.setText("Buy");
                button.getStyleClass().add("valid_buy_button");
                break;
            case NOT_ENOUGH_GOLD:
                button.setText("Not enough gold");
                button.setDisable(true);
                button.getStyleClass().add("invalid_buy_button");
                break;
            case FULL_TILE:
                button.setText("Tile is full");
                button.setDisable(true);
                button.getStyleClass().add("invalid_buy_button");
                break;
            case REQUIRED_RESOURCE:
                button.setText("Resource required");
                button.setDisable(true);
                button.getStyleClass().add("invalid_buy_button");
                break;
            case UNHAPPY_PEOPLE:
                button.setText("Unhappy people");
                button.setDisable(true);
                button.getStyleClass().add("invalid_buy_button");
                break;
            case CITY_NOT_GREW:
                button.setText("City is small");
                button.setDisable(true);
                button.getStyleClass().add("invalid_buy_button");
                break;
            default:
                break;
        }

        unitItem.getChildren().add(button);

        return unitItem;
    }
}
