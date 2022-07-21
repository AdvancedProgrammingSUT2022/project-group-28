package views.components;

import controllers.GameController;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.civilization.Civilization;
import views.App;

public class InfoBar extends Group {
    private BorderPane infoBar;
    private HBox data;
    private HBox otherData;
    private Circle scienceIcon;
    private Text science;

    private Circle goldIcon;
    private Text gold;
    private Circle happinessIcon;
    private Text happiness;
    private Civilization civilization;



    public InfoBar(){
        civilization = GameController.getGame().getCurrentPlayer();

        infoBar = new BorderPane();
        infoBar.getStyleClass().add("info_bar");

        data = new HBox(13);
        data.setAlignment(Pos.CENTER);
        data.getStyleClass().add("data_section");

        otherData = new HBox(15);
        otherData.setAlignment(Pos.CENTER);
        otherData.getStyleClass().add("other_data_section");

        scienceIcon = new Circle(10);
        scienceIcon.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/science_icon.png").toExternalForm())));

        science = new Text("+" + civilization.getScienceBalance());
        science.getStyleClass().add("science_text");

        goldIcon = new Circle(10);
        goldIcon.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/gold_icon.png").toExternalForm())));

        gold = new Text(civilization.getGold() + " ( +" + civilization.getGoldBalance() + " )");
        gold.getStyleClass().add("gold_text");

        happinessIcon = new Circle(10);
        happinessIcon.setFill(new ImagePattern(new Image(App.class.getResource("../assets/image/happiness_icon.png").toExternalForm())));

        happiness = new Text(Integer.toString(civilization.getHappiness()));
        happiness.getStyleClass().add("happiness_text");

        data.getChildren().addAll(scienceIcon,science,goldIcon,gold,happinessIcon,happiness);
        infoBar.setLeft(data);
        infoBar.setRight(otherData);
        this.getChildren().add(infoBar);
    }


    public void update(){
        science.setText("+" + civilization.getScienceBalance());
        gold.setText(civilization.getGold() + " ( +" + civilization.getGoldBalance() + " )");
        happiness.setText(Integer.toString(civilization.getHappiness()));
    }
}
