package views.components;

import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.civilization.City;
import views.App;

public class CityBanner extends HBox {
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
        this.setPrefWidth(200);
        this.getStyleClass().add("city_banner");
        this.city = city;

        Text population = new Text(String.valueOf(city.getPopulation()));
        population.getStyleClass().add("adsf");
        this.getChildren().add(population);

        ProgressBar growth = new ProgressBar(0.4);
        growth.setRotate(-90);
        this.getChildren().add(growth);


        Text cityName = new Text(city.getNAME());
//        if (city.equals(city.getCivilization().getCurrentCapital())) {
        if (true) {
            ImageView capitalStar = sdf;
//            System.out.println(sdf);
            capitalStar.setFitWidth(20);
            capitalStar.setFitHeight(20);
            this.getChildren().add(capitalStar);
        }


        this.getChildren().add(cityName);
    }

    public City getCity() {
        return city;
    }
}
