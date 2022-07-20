package views.components;

import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.civilization.enums.TechnologyTemplate;

public class TechnologyInfo extends Group {
    private VBox technologyInfo;
    private Text name;
    private Text cost;
    private Text leadTo;


    public TechnologyInfo(){
        technologyInfo = new VBox(15);
        technologyInfo.getStyleClass().add("technology_info");

        name = new Text();
        name.getStyleClass().add("name_of_technology");

        cost = new Text();
        cost.getStyleClass().add("cost_of_technology");

        leadTo = new Text();
        leadTo.getStyleClass().add("lead_to_of_technology");

        technologyInfo.getChildren().addAll(name,cost,leadTo);
        this.getChildren().add(technologyInfo);
    }

    public void update(TechnologyTemplate  technologyTemplate){
        name.setText(technologyTemplate.getName());
        cost.setText(Integer.toString(technologyTemplate.getCost()));
        leadTo.setText("sdlflsdf");

    }

}
