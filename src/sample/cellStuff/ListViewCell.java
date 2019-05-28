package sample.cellStuff;

import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import sample.controller.AppController;
import sample.model.Field;

public class ListViewCell extends ListCell<Field> {
    private final static String CLASS = "listViewCell";
    private final static String CLASS_NAME = "name";
    private final static String CLASS_DOMAIN = "domain";

    AppController appController;

    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem modifyItem = new MenuItem();
    private MenuItem removeItem = new MenuItem();

    private GridPane gridPane = new GridPane();
    private Label name = new Label();
    private Label domain = new Label();

    public ListViewCell(AppController appController) {
        this.appController = appController;

        configureGridPane();
        configureName();
        configureDomain();
        initContextMenu();
        addControlsGridPane();
    }

    private void configureGridPane() {
        gridPane.setHgap(10);
        gridPane.setVgap(2);
        gridPane.setPadding(new Insets(0, 10, 0, 10));
    }

    private void addControlsGridPane() {
        gridPane.getStyleClass().add(CLASS);
        gridPane.add(name, 1, 0);
        gridPane.add(domain, 1, 1);
    }

    private void configureName() {
        name.getStyleClass().add(CLASS_NAME);
    }

    private void configureDomain() {
        domain.getStyleClass().add(CLASS_DOMAIN);
    }

    private void initContextMenu() {
        modifyItem.setOnAction(event -> appController.modifyField());
        removeItem.setOnAction(event -> appController.removeField());
        contextMenu.getItems().addAll(modifyItem, removeItem);
    }

    private void addContent(Field d) {
        setText(null);
        setContextMenu(contextMenu);
        setOnMouseClicked(event -> {
            if (event.getClickCount() == 2)
                appController.modifyField();
        });

        modifyItem.setText("Edit " + getItem().getName());
        removeItem.setText("Delete " + getItem().getName());

        name.setText(d.getName());
        domain.setText(d.getData());

        setGraphic(gridPane);
    }

    private void clearContent() {
        setText(null);
        setGraphic(null);
        setOnMouseClicked(null);
        setContextMenu(null);
    }

    @Override
    protected void updateItem(Field field, boolean b) {
        super.updateItem(field, b);
        if(b) {
            clearContent();
        } else {
            addContent(field);
        }
    }
}
