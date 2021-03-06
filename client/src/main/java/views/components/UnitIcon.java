package views.components;

import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import models.units.Unit;
import models.units.enums.UnitTemplate;
import views.App;

import java.util.HashMap;

public class UnitIcon extends Group {
    private static final HashMap<UnitTemplate, ImagePattern> icons = new HashMap<>();
    static {
        for (UnitTemplate unitTemplate : UnitTemplate.values()) {
            Image icon = new Image(App.class.getResource("../assets/image/unit_icon/" + unitTemplate.getFilename() + ".png").toExternalForm());
            icons.put(unitTemplate, new ImagePattern(icon));
        }
    }

    private final Unit unit;
    public UnitIcon(Unit unit) {
        this.unit = unit;

        Circle background = new Circle(25);

        background.setFill(this.unit.getCivilization().getCivilizationNames().getColor());
        this.getChildren().add(background);

        Circle icon = new Circle(25);
        icon.setFill(icons.get(this.unit.getUnitTemplate()));

        this.getChildren().add(icon);

        double hpValue = (double) unit.getHealth() / 10;
        ProgressBar hpBar = new ProgressBar(hpValue);
        hpBar.getStyleClass().add("hp_bar");
        hpBar.setMaxWidth(50);
        hpBar.setRotate(-90);

        this.getChildren().add(hpBar);
    }

    public Unit getUnit() {
        return unit;
    }
}
