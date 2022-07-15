package views.components;

import java.util.ArrayList;
import java.util.HashMap;

import controllers.CombatController;
import controllers.GameController;
import controllers.units.SettlerController;
import controllers.units.UnitController;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.units.Unit;
import views.App;
import views.GameMenu;
import views.GamePage;

public class UnitInfo extends Group {
    private static final HashMap<UnitAction, Image> icons = new HashMap<>();
    static{
        for (UnitAction unitAction : UnitAction.values()) {
            Image icon = new Image(App.class.getResource("../assets/image/unit_action/" + unitAction.getFilename() + ".png").toExternalForm());
            icons.put(unitAction, icon);
        }
    }
    public enum UnitAction {
        FOUND_CITY("Found_City"){
            @Override
            public void callMediator(Unit unit){
                SettlerController.foundCity();
            }
        },
        FORTIFY("Fortify"){
            @Override
            public void callMediator(Unit unit){
                UnitController.fortifyUnit(unit);
            }
        },
        PREPARE("Prepare"){
            @Override
            public void callMediator(Unit unit){
                UnitController.prepareUnit();
            }
        },
        BUILD("Build"){
            @Override
            public void callMediator(Unit unit){
                System.out.println("Build");
            }
        },
        SLEEP("Sleep"){
            @Override
            public void callMediator(Unit unit){
                UnitController.sleepUnit(unit);
            }
        },
        WAKE("Wake"){
            @Override
            public void callMediator(Unit unit){
                UnitController.wakeUnit(unit);
            }
        },
        ALERT("Alert"){
            @Override
            public void callMediator(Unit unit){
                UnitController.alertUnit(unit);
            }
        },
        HEAL("Heal"){
            @Override
            public void callMediator(Unit unit){
                UnitController.healUnit(unit);
            }
        },
        PILLAGE("Pillage"){
            @Override
            public void callMediator(Unit unit){
                CombatController.pillageTile(unit);
            }
        },
        INFO("Delete"){
            @Override
            public void callMediator(Unit unit){
                GameMenu.getInstance().showUnitInfo();
            }
        },
        DELETE("Delete"){
            @Override
            public void callMediator(Unit unit){
                UnitController.deleteUnit();
            }
        },
        FREE("Free"){
            @Override
            public void callMediator(Unit unit){
                UnitController.freeUnit();
            }
        };

        private String fileName;

        private UnitAction(String fileName){
            this.fileName = fileName;
        }

        public abstract void callMediator(Unit unit);

        public String getFilename(){
            return fileName;
        }
    }

    private Circle icon;
    private Rectangle background;
    private Text name;
    private Text movePoint;
    private HBox actions;
    public UnitInfo() {
        this.background = new Rectangle(350, 150);
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
        actions.setLayoutX(40);
        actions.setLayoutY(150);
        actions.setAlignment(Pos.CENTER);
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

            ArrayList<UnitAction> availableActions = UnitController.getPossibleActions(unit);
            this.actions.getChildren().clear();
            for (UnitAction unitAction : availableActions) {
                Image icon = icons.get(unitAction);
                Circle actionIcon = new Circle(20);
                Tooltip tooltip = new Tooltip(unitAction.name());
                Tooltip.install(actionIcon, tooltip);
                actionIcon.setFill(new ImagePattern(icon));
                actionIcon.setOnMouseClicked(e -> {
                    unitAction.callMediator(unit);
                    GamePage.getInstance().createMap(true);
                });
                this.actions.getChildren().add(actionIcon);
            }
        } else {
            this.background.setFill(Color.TRANSPARENT);

            this.icon.setFill(Color.TRANSPARENT);

            this.name.setText("");

            this.movePoint.setText("");
        }
    }
}
