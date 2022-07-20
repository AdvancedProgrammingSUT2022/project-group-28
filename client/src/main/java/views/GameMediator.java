package views;

import controllers.*;
import controllers.units.UnitController;
import controllers.units.WorkerController;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Constructable;
import models.civilization.City;
import models.civilization.enums.TechnologyTemplate;
import models.tiles.Tile;
import models.tiles.enums.ImprovementTemplate;
import models.units.Unit;
import models.units.Worker;
import models.units.enums.UnitTemplate;
import views.components.MessageBox;
import views.enums.CityMessage;
import views.notifications.CivilizationNotification;
import views.notifications.GameNotification;
import views.notifications.NotificationTemplate;

public class GameMediator {
    private static GameMediator instance;

    private GameMediator() {}

    public static GameMediator getInstance() {
        if (instance == null) instance = new GameMediator();
        return instance;
    }

    public void openCityMenu(City city) {
        // TODO: notify server
        GameController.getGame().setSelectedCity(city);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(GamePage.getInstance().getGameContent().getParent().getScene().getWindow());
        Scene scene;
        scene = new Scene(App.loadFXML("cityPage"));
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }

    public void tryFreeCitizen(int i, int j) {
        // TODO: notify server
        // certainly it is successful
        CityController.freeCitizen(i, j);

        GamePage.getInstance().updateGamePage();
    }

    public void tryAssignCitizen(int i, int j) {
        if (CityController.assignCitizen(i, j) == CityMessage.SUCCESS) {
            // TODO: notify server
        } else {
            // TODO: change message type
            HUDController.getInstance().addMessage("There is no free citizen in city", MessageBox.Type.ALERT);
        }

        GamePage.getInstance().updateGamePage();
    }

    public void openBuyUnitMenu(Node node) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(node.getParent().getScene().getWindow());
        Scene scene;
        scene = new Scene(App.loadFXML("buyUnitPage"));
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }

    public void buyUnit(City city, UnitTemplate unitTemplate) {
        // TODO: check if the buy decrease gold or not
        // TODO: notify server
        CivilizationController.buyUnit(city.getCivilization(), city.getTile(), unitTemplate);

        // TODO: update hud

        GamePage.getInstance().updateGamePage();
    }

    public void tryBuyTile(City city, Tile tile) {
        if (CityController.buyTile(city, tile) == CityMessage.SUCCESS) {
            // TODO: notify server
        } else {
            // TODO: change message type
            HUDController.getInstance().addMessage("You don not have enough gold", MessageBox.Type.ALERT);
        }

        GamePage.getInstance().updateGamePage();
    }

    public void openConstructionMenu(City city) {
        // TODO: notify server
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

    public void startConstruction(Constructable constructable, City city) {
        // TODO: notify server
        CityController.startConstructing(city, constructable);

        GamePage.getInstance().updateGamePage();
    }

    public void moveUnit(Unit unit, Tile destination){
        int i = destination.getCoordinates()[0];
        int j = destination.getCoordinates()[1];
        UnitController.moveUnitToTarget(i, j);

        GamePage.getInstance().updateGamePage();
    }

    public void attack(Unit unit, Tile destination){
        int i = destination.getCoordinates()[0];
        int j = destination.getCoordinates()[1];
        CombatController.unitAttack(i, j, GameMenu.getInstance());

        GamePage.getInstance().updateGamePage();
    }

    public void cityAttack(Tile destination){
        int i = destination.getCoordinates()[0];
        int j = destination.getCoordinates()[1];
        CombatController.cityAttack(i, j);

        GamePage.getInstance().updateGamePage();
    }

    public void nextTurn() {
        GameNotification gameNotification = GameMenuController.nextTurn();
        NotificationTemplate notification = gameNotification.getNotificationTemplate();
        // TODO: change add message template
        if (notification == CivilizationNotification.FREE_UNIT) {
            HUDController.getInstance().addMessage("You have free unit", MessageBox.Type.ALERT);
            int freeUnitI = Integer.parseInt(gameNotification.getData().get(0));
            int freeUnitJ = Integer.parseInt(gameNotification.getData().get(1));
            GamePage.getInstance().setBaseI(freeUnitI);
            GamePage.getInstance().setBaseJ(freeUnitJ);
            GamePage.getInstance().setOffsetI(0);
            GamePage.getInstance().setOffsetJ(0);
        } else if (notification == CivilizationNotification.NO_CONSTRUCTION) {
            HUDController.getInstance().addMessage("The city has no construction", MessageBox.Type.ALERT);
            CityController.selectCityByName(gameNotification.getData().get(0), false);
            City city = GameController.getGame().getSelectedCity();
            int cityI = city.getTile().getCoordinates()[0];
            int cityJ = city.getTile().getCoordinates()[1];
            GamePage.getInstance().setBaseI(cityI);
            GamePage.getInstance().setBaseJ(cityJ);
            GamePage.getInstance().setOffsetI(0);
            GamePage.getInstance().setOffsetJ(0);
        } else if (notification == CivilizationNotification.NO_TECHNOLOGY) {
            HUDController.getInstance().addMessage("You have to study a technology", MessageBox.Type.ALERT);
        } else {
            System.out.println("done");
            // TODO: notify server
            GameMenuController.startNewTurn();
        }

        // TODO: handle start new turn
        GamePage.getInstance().updateGamePage();
    }

    public void openTechnologyTree() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(GamePage.getInstance().getGameContent().getParent().getScene().getWindow());
        Scene scene;
        scene = new Scene(App.loadFXML("technologyTreePage"));
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }

    public void openTechnologyPage(){
        // TODO: notify server
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(GamePage.getInstance().getGameContent().getParent().getScene().getWindow());
        stage.setX(170);
        stage.setY(200);
        Scene scene;
        scene = new Scene(App.loadFXML("technologyPage"), 388, 688);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }

    public String leadToMaker(TechnologyTemplate technologyTemplate){
        String out = "";
        for (TechnologyTemplate technology: TechnologyTemplate.values()) {
             if(technology.getRequiredTechnologies().contains(technologyTemplate)){
                 if(out.length() == 0){
                     out += technology.getName() + "\n";
                 }
                 else {
                     out += "                     " + technology.getName() + "\n";
                 }

             }
        }
        if(out.length() == 0){
            out = "nothing :)";
        }
        return out;
    }

    public void startStudyTechnology(TechnologyTemplate technologyTemplate){
        TechnologyController.getNewTechnology(technologyTemplate);
    }

    public void openBuildMenu(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(GamePage.getInstance().getGameContent().getParent().getScene().getWindow());
        Scene scene;
        scene = new Scene(App.loadFXML("buildPage"));
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }

    public void build(ImprovementTemplate improvementTemplate){
        WorkerController.startImprovement((Worker) GameController.getGame().getSelectedUnit(), improvementTemplate);
    }

    public void openFriendshipRequestMenu() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(App.currentScene.getWindow());
        Scene scene;
        scene = new Scene(App.loadFXML("friendshipRequestPage"));
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }
}
