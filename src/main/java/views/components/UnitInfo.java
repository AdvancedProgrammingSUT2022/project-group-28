package views.components;

import controllers.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.units.Unit;
import views.App;

public class UnitInfo extends Group {
    public enum UnitAction {
        ;
        public void callMediator(Unit unit) {}
    }

    private Circle icon;
    private Rectangle background;
    private Text name;
    private Text movePoint;
    private HBox actions;
    public UnitInfo() {
        this.background = new Rectangle(350, 130);
        this.background.setLayoutY(70);
        this.background.setFill(Color.TRANSPARENT);
        this.getChildren().add(this.background);

        VBox info = new VBox(5);
        info.setPrefWidth(150);
        info.setLayoutX(150);
        info.setLayoutY(70);
        info.setAlignment(Pos.CENTER);

        // TODO: add more info of unit

        this.name = new Text();
        this.name.getStyleClass().add("normal_text");
        info.getChildren().add(this.name);

        this.movePoint = new Text();
        this.movePoint.getStyleClass().add("normal_text");
        info.getChildren().add(this.movePoint);

        this.getChildren().add(info);

        this.actions = new HBox();
        this.getChildren().add(this.actions);

        this.icon = new Circle(50);
        this.icon.setFill(Color.TRANSPARENT);
        this.icon.setLayoutX(60);
        this.icon.setLayoutY(60);
        this.getChildren().add(this.icon);

    }

    public void update() {
        Unit unit = GameController.getGame().getSelectedUnit();
        if (unit != null) {
            this.background.setFill(Color.WHITE);

            String imageAddress = App.class.getResource("../assets/image/unit/" + unit.getUnitTemplate().getFilename() + ".png").toExternalForm();
            this.icon.setFill(new ImagePattern(new Image(imageAddress)));

            this.name.setText("Move point: " + unit.getUnitTemplate().getName());

            this.movePoint.setText(String.valueOf(unit.getMovePoint()));

            // TODO: add possible actions
        } else {
            this.background.setFill(Color.TRANSPARENT);

            this.icon.setFill(Color.TRANSPARENT);

            this.name.setText("");

            this.movePoint.setText("");
        }
    }
}
