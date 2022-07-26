package views.components;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.Constructable;
import models.civilization.City;
import models.civilization.enums.BuildingTemplate;
import models.units.enums.UnitTemplate;
import views.App;
import views.GameMediator;

import java.util.HashMap;

import controllers.GameController;

public class CityBanner extends HBox {
    private static ImagePattern star = new ImagePattern(new Image(App.class.getResource("../assets/image/star.png").toExternalForm()));
    private static ImagePattern unknown = new ImagePattern(new Image(App.class.getResource("../assets/image/unit/unknown.png").toExternalForm()));
    private static HashMap<Constructable, ImagePattern> constructions = new HashMap<>();
    static {
        for (UnitTemplate unitTemplate : UnitTemplate.values()) {
            Image image = new Image(App.class.getResource("../assets/image/unit/" + unitTemplate.getFilename() + ".png").toExternalForm());
            constructions.put(unitTemplate, new ImagePattern(image));
        }
        for (BuildingTemplate buildingTemplate : BuildingTemplate.values()) {
            Image image = new Image(App.class.getResource("../assets/image/building/" + buildingTemplate.getFilename() + ".png").toExternalForm());
            constructions.put(buildingTemplate, new ImagePattern(image));
        }
    }

    private final City city;
    public CityBanner(City city) {
        super(-10);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("city_banner");
        this.setStyle("-fx-background-color: " + city.getCivilization().getCivilizationNames().getColorHex() + "aa");
        this.city = city;

        HBox growthBox = new HBox(-10);
        growthBox.setAlignment(Pos.CENTER);

        Text population = new Text(String.valueOf(city.getPopulation()));
        population.getStyleClass().add("population");
        growthBox.getChildren().add(population);

        int growthLimit = city.getPopulation() * (city.getPopulation() + 1) / 2 + 12;
        double growthProgress = (double) city.getGrowthBucket() / growthLimit;
        ProgressBar growth = new ProgressBar(growthProgress);
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
        cityName.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameMediator.getInstance().openCityMenu(CityBanner.this.city);
            }
        });
        nameBox.getChildren().add(cityName);

        this.getChildren().add(nameBox);

        HBox productionBox = new HBox(-20);
        productionBox.setAlignment(Pos.CENTER);

        if (city.getConstruction() != null) {
            double progress = (double) city.getConstruction().getSpentProduction() / city.getConstruction().getConstructionTemplate().getCost();
            ProgressBar productionProgress = new ProgressBar(progress);
            productionProgress.getStyleClass().add("production_bar");
            productionProgress.setMaxWidth(50);
            productionProgress.setRotate(-90);
            productionBox.getChildren().add(productionProgress);

            Constructable constructable = city.getConstruction().getConstructionTemplate();
            Circle production = new Circle(30);
            if (GameController.getGame().getCurrentPlayer().equals(city.getCivilization())) {
                production.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (GameController.getGame().getCurrentPlayer().equals(CityBanner.this.city.getCivilization())) {
                            GameMediator.getInstance().openConstructionMenu(CityBanner.this.city);
                        }
                    }
                });
            }
            
            production.setFill(constructions.get(constructable));
            productionBox.getChildren().add(production);
        } else {
            ProgressBar productionProgress = new ProgressBar(0.1);
            productionProgress.getStyleClass().add("production_bar");
            productionProgress.setMaxWidth(50);
            productionProgress.setRotate(-90);
            productionBox.getChildren().add(productionProgress);

            Circle production = new Circle(30);
            production.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (GameController.getGame().getCurrentPlayer().equals(CityBanner.this.city.getCivilization())) {
                        GameMediator.getInstance().openConstructionMenu(CityBanner.this.city);
                    }
                }
            });
            production.setFill(unknown);
            productionBox.getChildren().add(production);
        }
        this.getChildren().add(productionBox);
    }

    public City getCity() {
        return city;
    }

}
