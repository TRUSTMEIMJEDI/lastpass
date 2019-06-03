package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AboutController {
    AppController app;

    @FXML
    Button bOk;

    @FXML
    void unMountAbout() {
        app.unMountViews();
    }

    void bindParent(AppController c) {
        app = c;
    }
}
