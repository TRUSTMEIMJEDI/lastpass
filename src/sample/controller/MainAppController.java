package sample.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import sample.Main;

public class MainAppController {
    private Main mainApp;
    AppController app;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleNewDomain() {

    }

    @FXML
    private void handleNewEntry() {

    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    @FXML
    private void handleAbout() {
        app.mountAbout();
    }
}
