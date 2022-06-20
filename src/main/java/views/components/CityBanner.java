package views.components;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.Constructable;
import models.civilization.City;
import models.civilization.Construction;
import models.units.enums.UnitTemplate;
import views.App;

import java.util.ArrayList;
import java.util.HashMap;

public class CityBanner extends HBox {
    private static ImagePattern star = new ImagePattern(new Image(App.class.getResource("../assets/image/star.png").toExternalForm()));
    private static ImagePattern unknown = new ImagePattern(new Image(App.class.getResource("../assets/image/unit/unknown.png").toExternalForm()));
    private static HashMap<Constructable, ImagePattern> constructions = new HashMap<>();
    static {
        for (UnitTemplate unitTemplate : UnitTemplate.values()) {
            Image image = new Image(App.class.getResource("../assets/image/unit/" + unitTemplate.getFilename() + ".png").toExternalForm());
            constructions.put(unitTemplate, new ImagePattern(image));
        }
    }


    private static ImageView sdf = new ImageView(new Image(App.class.getResource("../assets/image/icon.png").toExternalForm()));
    // assign citizen
    // buy tile
    // construction
    // buy buildings and units
    // change captured city mode
    // population


    // a banner includes: - name
    //                    - capital start
    //                    - civilization color
    //                    - current construction and progress bar on click assign new or change
    //                    - population and growth bar
    //                    - onClick open 3 menus 1. buy tile 2. buy unit and building 3. assign citizens
    private final City city;
    public CityBanner(City city) {
        super(-10);
        this.setAlignment(Pos.CENTER);
        // TODO: set background color to civ color
        this.getStyleClass().add("city_banner");
        this.city = city;

        HBox growthBox = new HBox(-10);
        growthBox.setAlignment(Pos.CENTER);

        Text population = new Text(String.valueOf(city.getPopulation()));
        population.getStyleClass().add("population");
        growthBox.getChildren().add(population);


        // TODO: set this value
        ProgressBar growth = new ProgressBar(0.4);
        growth.getStyleClass().add("growth_bar");
        growth.setMaxWidth(50);
        growth.setRotate(-90);
        growthBox.getChildren().add(growth);

        this.getChildren().add(growthBox);


        HBox nameBox = new HBox(0);
        nameBox.setAlignment(Pos.CENTER);

        if (city.equals(city.getCivilization().getCurrentCapital())) {
            Circle capitalStar = new Circle(10);
            capitalStar.setFill(star);
            nameBox.getChildren().add(capitalStar);
        }

        Button cityName = new Button(city.getNAME());
        cityName.getStyleClass().add("city_name");
        nameBox.getChildren().add(cityName);

        this.getChildren().add(nameBox);

        HBox productionBox = new HBox(-20);
        productionBox.setAlignment(Pos.CENTER);

        if (city.getConstruction() != null) {
            // TODO: set this value
            ProgressBar productionProgress = new ProgressBar(0.4);
            productionProgress.getStyleClass().add("production_bar");
            productionProgress.setMaxWidth(50);
            productionProgress.setRotate(-90);
            productionBox.getChildren().add(productionProgress);

            Constructable constructable = city.getConstruction().getConstructionTemplate();
            Circle production = new Circle(30);
            production.setFill(constructions.get(constructable));
            productionBox.getChildren().add(production);
        } else {
            ProgressBar productionProgress = new ProgressBar(0.1);
            productionProgress.getStyleClass().add("production_bar");
            productionProgress.setMaxWidth(50);
            productionProgress.setRotate(-90);
            productionBox.getChildren().add(productionProgress);

            Circle production = new Circle(30);
            production.setFill(unknown);
            productionBox.getChildren().add(production);
        }
        this.getChildren().add(productionBox);
    }

    public City getCity() {
        return city;
    }
}
