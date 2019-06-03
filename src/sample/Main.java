package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.controller.AppController;
import sample.controller.MainAppController;

public class Main extends Application {

    private final static String FOLDER_FXML = "view/";
    private final static String FOLDER_CSS = FOLDER_FXML + "stylesheets/";
    public final static String FOLDER_FONTS = FOLDER_FXML + "fonts/";
    public final static String FOLDER_IMAGES = "images/";

    private final static String FXML_MAIN_APP = FOLDER_FXML + "MainApp.fxml";
    private final static String FXML_APP = FOLDER_FXML + "App.fxml";
    public final static String FXML_FIELDINFO = FOLDER_FXML + "FieldInfo.fxml";
    public final static String FXML_ACCOUNTINFO = FOLDER_FXML + "AccountInfo.fxml";
    public final static String FXML_ABOUT = FOLDER_FXML + "About.fxml";

    private final static String CSS_BOOTSTRAP = FOLDER_CSS + "bootstrap3.css";
    private final static String CSS_PASSWORDMANAGER = FOLDER_CSS + "style.css";

    private Stage stage;
    private BorderPane rootLayout;
    private MainAppController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        HibernateUtil.getSessionFactory();
    }

    @Override
    public void stop() throws Exception {
        HibernateUtil.shutdown();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        //FXMLLoader loader = FXMLLoader.load(getClass().getResource(FXML_MAIN_APP));

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(FXML_MAIN_APP));

        rootLayout = loader.load();
        controller = loader.getController();
        controller.setMainApp(this);
        stage.setTitle("LastPass");

        Scene s = new Scene(rootLayout);
        s.getStylesheets().addAll(
                getClass().getResource(CSS_BOOTSTRAP).toExternalForm(),
                getClass().getResource(CSS_PASSWORDMANAGER).toExternalForm()
        );

        stage.setScene(s);
        stage.show();

        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(Main.class.getResource(FXML_APP));
        AnchorPane appLayout = loader2.load();

        // set App.fxml into the center of RootLayout
        rootLayout.setCenter(appLayout);
        // give the controller access to MainApp
        AppController controller2 = loader2.getController();
        controller2.setMainApp(this);

        stage.setResizable(false);
        stage.show();
    }
}
