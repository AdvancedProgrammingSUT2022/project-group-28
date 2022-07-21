package views;

import java.io.File;
import java.util.Date;

import controllers.GameMenuController;
import controllers.GsonHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.Game;

public class LoadPage {
    @FXML
    private VBox scroll;

    @FXML
    private Label errorLabel;

    public void initialize() {
        getAvailableSaves();
    }

    @FXML
    private void back() {
        scroll.getScene().getWindow().hide();
    }

    private void getAvailableSaves() {
        File file = new File("data/save/");

        boolean found = false;
        for(File f : file.listFiles()){
            if (f.getName().endsWith(".civ")){
                found = true;
                scroll.getChildren().add(createFileInfoHBox(f));
            }
        }
        
        if (!found) {
            errorLabel.setText("No saves found");
            errorLabel.setFont(new Font(20));
        }
    }

    private HBox createFileInfoHBox(File file) {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setPrefWidth(700);
        hBox.getStyleClass().add("user_ranking_info");

        VBox filenameVBox = new VBox();
        filenameVBox.setPrefWidth(300);
        Text nickname = new Text(file.getName().split("\\.")[0]);
        nickname.setFont(Font.font("Verdana", 20));
        filenameVBox.getChildren().add(nickname);
        hBox.getChildren().add(filenameVBox);

        VBox dateBox = new VBox();
        dateBox.setPrefWidth(200);
        Text date = new Text(new Date(file.lastModified()).toString());
        date.setFont(Font.font("Verdana", 20));
        dateBox.getChildren().add(date);
        hBox.getChildren().add(dateBox);

        hBox.setOnMouseClicked(value -> {
            Game game = GsonHandler.importGame(file.getName().split("\\.")[0]);
            GameMenuController.setGame(game);
            back();
        });

        return hBox;
    }
}
