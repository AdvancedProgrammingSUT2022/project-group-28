package views.components;

import controllers.GameController;
import controllers.TechnologyController;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.civilization.Civilization;
import models.civilization.Technology;
import models.civilization.enums.TechnologyTemplate;
import views.App;
import views.GameMediator;

import java.util.ArrayList;

public class CurrentTechnologyInfo extends Group {

    private HBox currentTechnologyPanel;
    private Circle currentTechnologyPicture;
    private Text currentTechnologyName;
    private ProgressIndicator currentTechnologyProgress;
    public CurrentTechnologyInfo(){
        currentTechnologyPanel = new HBox(14);
        currentTechnologyPanel.getStyleClass().add("current_technology_panel");
        currentTechnologyPanel.setAlignment(Pos.CENTER_LEFT);
        currentTechnologyPanel.setPrefWidth(350);
        currentTechnologyPanel.setLayoutX(30);
        currentTechnologyPanel.setLayoutY(35);
        currentTechnologyProgress = new ProgressIndicator();
        currentTechnologyProgress.setProgress(1);
        currentTechnologyProgress.getStyleClass().add("current_technology_progress");


        currentTechnologyPicture = new Circle(50);
        currentTechnologyName = new Text() ;
        currentTechnologyName.getStyleClass().add("current_technology_name");

        String address = App.class.getResource("../assets/image/technology/agriculture.png").toExternalForm();
        this.currentTechnologyName.setText("Agriculture");

        ImagePattern picture = new ImagePattern(new Image(address));
        this.currentTechnologyPicture.setFill(picture);

        currentTechnologyPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (GameController.getGame().getCurrentPlayer().equals(App.getCurrentUserCivilization())) {
                    GameMediator.getInstance().openTechnologyPage();
                }
            }
        });
        currentTechnologyPanel.getChildren().addAll(currentTechnologyPicture , currentTechnologyName , currentTechnologyProgress );
        this.getChildren().add(currentTechnologyPanel);

    }
    public void updateData(){


        String imageFileName = "";
        Technology currentStudyTechnology = App.getCurrentUserCivilization().getCurrentStudyTechnology();
        ArrayList<Technology> userTechnologies = App.getCurrentUserCivilization().getStudiedTechnologies();
        if(currentStudyTechnology == null){
            if(App.getCurrentUserCivilization().getStudiedTechnologies().size() <= 1){
                imageFileName = "agriculture";
                currentTechnologyName.setText("Agriculture");
                currentTechnologyProgress.setVisible(true);
                currentTechnologyProgress.setProgress(1);
            }
            else{
                imageFileName = "unknown";
                currentTechnologyName.setText("Unknown");
                currentTechnologyProgress.setVisible(false);
            }

        }
        else{
            imageFileName = currentStudyTechnology.getTechnologyTemplate().getFileName();
            currentTechnologyName.setText(currentStudyTechnology.getTechnologyTemplate().getName());
            currentTechnologyProgress.setVisible(true);
            currentTechnologyProgress.setProgress(setProgressNumber(currentStudyTechnology.getTechnologyTemplate()));
        }

        String address = App.class.getResource("../assets/image/technology/" + imageFileName + ".png").toExternalForm();

        ImagePattern picture = new ImagePattern(new Image(address));
        currentTechnologyPicture.setFill(picture);



        currentTechnologyPanel.getChildren().clear();
        currentTechnologyPanel.getChildren().addAll(currentTechnologyPicture , currentTechnologyName , currentTechnologyProgress);
    }

    private double setProgressNumber(TechnologyTemplate technologyTemplate){
        Civilization userCivilization = App.getCurrentUserCivilization();
        Technology technology = userCivilization.getTechnologyByTechnologyTemplate(technologyTemplate);
        if(technology == null){
            return 0;
        }
        return ((double)technology.getProgress() / technologyTemplate.getCost()) ;
    }
}
