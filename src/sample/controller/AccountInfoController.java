package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

        String password = passwordGenerator.generate(12);
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
