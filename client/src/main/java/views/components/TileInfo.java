package views.components;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.tiles.Resource;
import models.tiles.enums.ResourceTemplate;
import models.tiles.enums.Terrain;
import models.tiles.enums.TerrainFeature;
import views.App;

import java.util.HashMap;

public class TileInfo extends Group {
    private static ImagePattern foodPattern = new ImagePattern(new Image(App.class.getResource("../assets/image/food_icon.png").toExternalForm()));
    private static ImagePattern productionPattern = new ImagePattern(new Image(App.class.getResource("../assets/image/production_icon.png").toExternalForm()));
    private static ImagePattern goldPattern = new ImagePattern(new Image(App.class.getResource("../assets/image/gold_icon.png").toExternalForm()));
    private static final HashMap<ResourceTemplate, ImagePattern> resourceImages = new HashMap<>();

    static {
        for (ResourceTemplate resourceTemplate : ResourceTemplate.values()) {
            Image image = new Image(App.class.getResource("../assets/image/resource/" + resourceTemplate.getFilename() + ".png").toExternalForm());
            resourceImages.put(resourceTemplate, new ImagePattern(image));
        }
    }

    private VBox tileInfo;
    private HBox resource;
    private Text resourceText;
    private Circle resourceIcon;
    private Text resourceName;
    private Text terrain;
    private HBox output;
    private Circle foodIcon;
    private Text food;
    private Circle productionIcon;
    private Text production;
    private Circle goldIcon;
    private Text gold;

    public TileInfo(){
        tileInfo = new VBox(18);
        tileInfo.setAlignment(Pos.TOP_LEFT);
        tileInfo.getStyleClass().add("tile_info");
        //-------------------------------------------------
        resource = new HBox(10);
        resource.setAlignment(Pos.CENTER_LEFT);
        resourceText = new Text("Resource :");
        resourceText.getStyleClass().add("resource_name");
        resourceIcon = new Circle(15);
        resourceName = new Text();
        resourceName.getStyleClass().add("resource_name");
        resource.getChildren().addAll(resourceText,resourceIcon,resourceName);
        //-----------------------------------------------
        terrain = new Text();
        terrain.getStyleClass().add("terrain_name");
        //----------------------------------------------
        output = new HBox(10);
        output.setAlignment(Pos.CENTER_LEFT);

        foodIcon = new Circle(10);
        foodIcon.setFill(foodPattern);
        food = new Text();
        food.getStyleClass().add("output_text");

        productionIcon = new Circle(10);
        productionIcon.setFill(productionPattern);
        production = new Text();
        production.getStyleClass().add("output_text");

        goldIcon = new Circle(10);
        goldIcon.setFill(goldPattern);
        gold = new Text();
        gold.getStyleClass().add("output_text");

        output.getChildren().addAll(foodIcon,food,productionIcon,production,goldIcon,gold);
        //---------------------------------------------------------
        tileInfo.getChildren().addAll(resource , terrain , output);
        this.getChildren().add(tileInfo);

    }

    public void update(Hex hex){
        Resource resource1 = hex.getTile().getResource();
        if(resource1 != null){
            tileInfo.getChildren().clear();
            tileInfo.getChildren().addAll(resource , terrain , output);
            resourceIcon.setFill(resourceImages.get(resource1.getResourceTemplate()));
            resourceName.setText(resource1.getResourceTemplate().getName());
        }
        else{
            tileInfo.getChildren().clear();
            tileInfo.getChildren().addAll(terrain , output);
        }

        int sumFood;
        int sumProduction;
        int sumGold;
        Terrain terrain1 = hex.getTile().getTerrain();
        TerrainFeature terrainFeature = hex.getTile().getTerrainFeature();
        terrain.setText("Terrain : " + terrain1.getName());
        if(terrainFeature == null){
            sumFood = terrain1.getFood();
            sumProduction = terrain1.getProduction();
            sumGold = terrain1.getGold();
        }
        else {
            sumFood = terrain1.getFood() + terrainFeature.getFood() ;
            sumProduction = terrain1.getProduction() + terrainFeature.getProduction();
            sumGold = terrain1.getGold() + terrainFeature.getGold();
        }

        food.setText(Integer.toString(sumFood));
        production.setText(Integer.toString(sumProduction));
        gold.setText(Integer.toString(sumGold));

    }

}
