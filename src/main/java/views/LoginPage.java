package views;

import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginPage extends PageController {
    public void goRegisterPage(MouseEvent mouseEvent) throws IOException {
        App.setRoot("registerPage");
    }

    public void goMainMenu(MouseEvent mouseEvent) {
    }
}
