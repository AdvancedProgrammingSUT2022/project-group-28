package views.components;

import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.civilization.enums.TechnologyTemplate;
import views.GameMediator;

public class TechnologyInfo extends Group {
    private VBox technologyInfo;
    private Text name;
    private Text line;
    private Text cost;
    private Text leadTo;


    public TechnologyInfo(){
        technologyInfo = new VBox(8);
        technologyInfo.getStyleClass().add("technology_info");

        name = new Text();
        name.getStyleClass().add("name_of_technology");

        line = new Text("----------------------");
        line.getStyleClass().add("name_of_technology");

        cost = new Text();
        cost.getStyleClass().add("cost_of_technology");

        leadTo = new Text();
        leadTo.getStyleClass().add("lead_to_of_technology");

        technologyInfo.getChildren().addAll(name,line,cost,leadTo);
        this.getChildren().add(technologyInfo);
    }

    public void update(TechnologyTemplate  technologyTemplate){
        name.setText(technologyTemplate.getName().toUpperCase());
        cost.setText("-> cost  |  " + technologyTemplate.getCost());
        leadTo.setText("-> lead to  |  " + GameMediator.getInstance().leadToMaker(technologyTemplate));
    }

}
