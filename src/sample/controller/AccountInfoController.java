package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import sample.model.Account;
import sample.model.Field;
import sample.repository.*;
import sample.security.PasswordGenerator;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AccountInfoController implements Initializable {
    AppController appController;
    IAccountRepository accountRepository;
    IFieldRepository fieldRepository;

    private Account accountToEdit;
    private Account newAccount;
    private Field field;

    private boolean update = false;
    private int passwordSize;

    @FXML
    TextField tfUser;
    @FXML
    TextField tfPassword;
    @FXML
    Label lDateCreation;
    @FXML
    Button bGenerate;
    @FXML
    Button bOk;
    @FXML
    Button bCancel;
    @FXML
    Label lTitle;
    @FXML
    Slider passSize;
    @FXML
    Label sliderValue;

    public void btnOk() {
        if (update) {
            accountToEdit.setUsername(tfUser.getText());
            accountToEdit.setPassword(tfPassword.getText());
            accountToEdit.setCreationDate();
            accountRepository.updateAccount(accountToEdit);
        } else {
            newAccount = new Account();
            newAccount.setUsername(tfUser.getText());
            newAccount.setPassword(tfPassword.getText());
            newAccount.setCreationDate();
            fieldRepository.addAccount(field, newAccount);
        }
        appController.unMountAccountView();
    }

    public void btnCancel() {
        appController.unMountAccountView();
    }

    public void GeneratePassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();

        passwordSize = (int) passSize.getValue();
        String password = passwordGenerator.generate(passwordSize);
        tfPassword.setText(password);
    }

    void initAccountInfo(Account account) {
        update = true;
        lTitle.setText("Edit - " + account.getUsername());
        accountToEdit = account;
        tfUser.setText(account.getUsername());
        tfPassword.setText(account.getPassword(false));
        lDateCreation.setText(account.getCreationDate());
    }

    void createNewAccount(Field f) {
        passSize.setMin(8);
        passSize.setMax(128);
        passSize.setValue(30);
        passSize.setBlockIncrement(1.0);
        passSize.setShowTickLabels(true);
        passSize.setShowTickMarks(true);

        passSize.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {

                String liczba = String.valueOf(newValue);
                sliderValue.setText(liczba.split("\\.")[0]);
                passwordSize = (int) newValue;
            }
        });

        update = false;
        lTitle.setText("Add new account");
        field = f;
        tfUser.setText("");
        tfPassword.setText("");
        lDateCreation.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    void bindParent(AppController c) {
        appController = c;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountRepository = new AccountRepository();
        fieldRepository = new FieldRepository();
    }
}
