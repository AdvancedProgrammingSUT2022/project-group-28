package views.components;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import models.civilization.enums.TechnologyTemplate;
import views.App;

import java.util.HashMap;

public class TechnologyNode {

    public static final HashMap<TechnologyTemplate, ImagePattern> technologies = new HashMap<>();

    static {
        for (TechnologyTemplate technologyTemplate: TechnologyTemplate.values()) {
            Image image = new Image(App.class.getResource("../assets/image/technology/" + technologyTemplate.getFileName() + ".png").toExternalForm());
            technologies.put(technologyTemplate , new ImagePattern(image));
        }
    }

}
