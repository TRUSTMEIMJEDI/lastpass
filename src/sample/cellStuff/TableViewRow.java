package sample.cellStuff;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import sample.controller.AppController;
import sample.model.Account;

public class TableViewRow extends TableRow<Account> {
    private AppController appController;

    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem modifyItem = new MenuItem();
    private MenuItem removeItem = new MenuItem();

    public TableViewRow(AppController appController) {
        this.appController = appController;

        initContextMenu();
    }

    private void initContextMenu() {
        modifyItem.setOnAction(event -> appController.modifyAccount());
        removeItem.setOnAction(event -> appController.removeAccount());
        contextMenu.getItems().addAll(modifyItem, removeItem);
    }

    @Override
    protected void updateItem(Account account, boolean b) {
        super.updateItem(account, b);

        if (b) {
            setOnMouseClicked(null);
        } else {
            modifyItem.setText("Edit " + getItem().getUsername());
            removeItem.setText("Remove " + getItem().getUsername());
            setContextMenu(contextMenu);
            setOnMouseClicked(event -> {
                if (event.getClickCount() == 2)
                    appController.modifyAccount();
            });
        }
    }
}
