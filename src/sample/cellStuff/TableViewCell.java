package sample.cellStuff;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import sample.model.Account;

public class TableViewCell extends TableCell<Account, String> {
    private TextField textField = new TextField();

    public TableViewCell() {
    }

    @Override
    protected void updateItem(String s, boolean b) {
        super.updateItem(s, b);
        if(b) {
            setText(null);
        } else {
            initTextField(s);
        }
    }

    private void initTextField(String s) {
        textField.setText(s);
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
    }
}
