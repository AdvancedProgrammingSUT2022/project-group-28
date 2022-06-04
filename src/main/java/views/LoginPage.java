package views;

import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginPage {
    public void goRegisterPage(MouseEvent mouseEvent) throws IOException {
        App.setRoot("registerPage");
    }
}
