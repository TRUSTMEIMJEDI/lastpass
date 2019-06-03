package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.model.Account;
import sample.model.Field;
import sample.repository.FieldRepository;
import sample.repository.IFieldRepository;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class FieldInfoController implements Initializable {
    AppController appController;
    IFieldRepository fieldRepository;

    private Field fieldToEdit;
    private Field newField;
    private boolean update = false;

    @FXML
    TextField tfName;
    @FXML
    TextField tfDomain;
    @FXML
    TextArea taNotes;
    @FXML
    Button bOk;
    @FXML
    Button bCancel;
    @FXML
    Label fieldLabel;


    public void btnOk() {
        if (update) {
            fieldToEdit.setName(tfName.getText());
            fieldToEdit.setData(tfDomain.getText());
            fieldToEdit.setNotes(taNotes.getText());
            fieldRepository.updateField(fieldToEdit);
        } else {
            newField = new Field();
            List<Account> accounts = new ArrayList<>();
            newField.setName(tfName.getText());
            newField.setData(tfDomain.getText());
            newField.setNotes(taNotes.getText());
            newField.setAccounts(accounts);
            fieldRepository.createField(newField);
        }
        appController.unMountFieldView();
    }

    public void btnCancel() {
        appController.unMountFieldView();
    }

    void initFieldInfo(Field field) {
        update = true;
        fieldToEdit = field;
        fieldLabel.setText(field.getName());
        tfName.setText(field.getName());
        tfDomain.setText(field.getData());
        taNotes.setText(field.getNotes());
    }

    void createNewField() {
        update = false;
        fieldLabel.setText("Add new field");
        tfName.setText("");
        tfDomain.setText("");
        taNotes.setText("");
    }

    void bindParent(AppController c) {
        appController = c;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fieldRepository = new FieldRepository();
    }
}
