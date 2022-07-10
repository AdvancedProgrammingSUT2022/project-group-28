package views;

import controllers.CityController;
import controllers.GameController;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Constructable;
import models.civilization.City;
import models.units.enums.UnitTemplate;

import java.awt.*;

public class GameMediator {
    private static GameMediator instance;

    private GameMediator() {}

    public static GameMediator getInstance() {
        if (instance == null) instance = new GameMediator();
        return instance;
    }
    // TODO:
    public void startConstruction(Constructable constructable, City city) {
        CityController.startConstructing(city, constructable);
        GamePage.getInstance().createMap(true);
    }

    public void openConstructionMenu(City city) {
        GameController.getGame().setSelectedCity(city);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(GamePage.getInstance().getGameContent().getParent().getScene().getWindow());
        Scene scene;
        scene = new Scene(App.loadFXML("constructionPage"), 788, 488);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }

}
