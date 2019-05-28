package sample.controller;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sample.Main;
import sample.cellStuff.ListViewCell;
import sample.cellStuff.TableViewRow;
import sample.model.Account;
import sample.model.Field;
import sample.repository.*;
import sample.view.res.ImageManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    //Repositories
    IAccountRepository accountRepository;
    IFieldRepository fieldRepository;
    IUserRepository userRepository;

    //ObservableList of Fields and Accounts
    private ObservableList<Field> fields;
    private ObservableList<Account> accounts;

    //Selected Field and Account
    private Field fieldSelected = null;
    private Account accountSelected = null;

    //Image manager
    private ImageManager imageManager = new ImageManager();

    //Przekazywanie do głownej klasy
    private Main mainApp;

    // OTHER VIEWS
    private FieldInfoController fieldInfoController;
    private Parent infoFieldView;

    // FXML
    @FXML
    private AnchorPane root;
    @FXML
    private VBox vbRoot;
    //Field Panel
    @FXML
    private AnchorPane detailsRoot;

    @FXML
    private ListView<Field> lvDomains;
    @FXML
    private ScrollPane spDomain;
    //search field
    @FXML
    private TextField tfFilter;
    @FXML
    private Button bOverwriteFilter;

    //TableView
    @FXML
    private TableView<Account> tvAccounts;
    @FXML
    private TableColumn<Account, Integer> tvAccountNumber;
    @FXML
    private TableColumn<Account, String> tvAccountName;
    @FXML
    private TableColumn<Account, String> tvAccountPassword;
    @FXML
    private TableColumn<Account, String> tvAccountCreationDate;

    //do domeny
    @FXML
    private Label lDetailsTitle;
    @FXML
    private Label lDetailsDomain;
    @FXML
    private Label lDetailsAccountSize;
    @FXML
    private Label lDetailsNotes;

    @FXML
    private Button bAddDomain;
    @FXML
    private Button bAddAccount;
    @FXML
    private Button bModifyDomain;
    @FXML
    private Button bModifyAccount;
    @FXML
    private Button bRemoveDomain;
    @FXML
    private Button bRemoveAccount;
    @FXML
    private Button bMoveUpDomainByOne;
    @FXML
    private Button bMoveUpAccountByOne;
    @FXML
    private Button bMoveDownDomainByOne;
    @FXML
    private Button bMoveDownAccountByOne;
    @FXML
    private Button bMoveUpDomainTop;
    @FXML
    private Button bMoveUpAccountTop;
    @FXML
    private Button bMoveDownDomainLast;
    @FXML
    private Button bMoveDownAccountLast;
    @FXML
    private Button bShowAndHidePassword;
    private Boolean hidePassword = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountRepository = new AccountRepository();
        fieldRepository = new FieldRepository();
        userRepository = new UserRepository();

        lvDomains.setPlaceholder(new Label("No domain"));
        tvAccounts.setPlaceholder(new Label("No account"));

        initButtons();
        initDomainList();
        initUi();
    }

    private void initInfoView() {
        FXMLLoader fxmlLoaderFieldInfo = new FXMLLoader(getClass().getResource(Main.FXML_FIELDINFO));

        try {
            infoFieldView = fxmlLoaderFieldInfo.load();
            fieldInfoController = fxmlLoaderFieldInfo.getController();

            fieldInfoController.bindParent(this);


            setAnchor(infoFieldView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void findByFilter() {
        tfFilter.setText("");
    }

    @FXML
    public void test() {

    }

    private void initDomainList() {
        lvDomains.prefWidthProperty().bind(spDomain.widthProperty().subtract(2));
        lvDomains.prefHeightProperty().bind(spDomain.heightProperty().subtract(2));

        fields = FXCollections.observableArrayList(fieldRepository.getFields());
        lvDomains.setItems(fields);
        //Ustawienie szczegolow kazdej komorki
        AppController self = this;
        lvDomains.setCellFactory(param -> new ListViewCell(self));
        lvDomains.getSelectionModel().selectedItemProperty().addListener((l, ov, nv) -> updateFieldPanel(nv));

        setFieldIdlePanel();
        updateFieldControllers();
    }

    private void initAccountTable(Field field) {
        accounts = FXCollections.observableArrayList(field.getAccounts());
        tvAccounts.setItems(accounts);
        AppController self = this;
        tvAccounts.setRowFactory(param -> new TableViewRow(self));
        tvAccounts.setEditable(false);

        tvAccountNumber.setCellFactory(column -> new TableCell<Account, Integer>() {
            @Override
            protected void updateItem(Integer i, boolean empty) {
                super.updateItem(i, empty);

                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(tvAccounts.getItems().indexOf(getTableRow().getItem()) + 1));
                }
            }
        });
        tvAccountName.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getUsername()));
        showAndHidePassword();
        //tvAccountPassword.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getPassword(true)));
        tvAccountCreationDate.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getCreationDate()));

        tvAccounts.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> selectAccount(nv));
        //tvAccounts.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> updateAccount(nv));
        updateAccountControllers();
    }

    private void showAndHidePassword() {
        if (accountSelected == null) {
            tvAccountPassword.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getPassword(true)));
            return;
        }
        hidePassword = (hidePassword) ? false : true;

        if (hidePassword) {
            tvAccountPassword.setCellValueFactory(a -> {
                if (a.getValue().equals(accountSelected)) {
                    return new SimpleStringProperty(a.getValue().getPassword(true));
                } else {
                    return new SimpleStringProperty(a.getValue().getPassword(true));
                }
            });
        } else {
            tvAccountPassword.setCellValueFactory(a -> {
                if (a.getValue().equals(accountSelected)) {
                    return new SimpleStringProperty(a.getValue().getPassword(false));
                } else {
                    return new SimpleStringProperty(a.getValue().getPassword(true));
                }
            });
        }
    }

    private void initButtons() {
        // Remove texts from Scene Builder
        bMoveUpDomainByOne.setText(null);
        bMoveUpAccountByOne.setText(null);
        bMoveDownDomainByOne.setText(null);
        bMoveDownAccountByOne.setText(null);
        bMoveUpDomainTop.setText(null);
        bMoveUpAccountTop.setText(null);
        bMoveDownDomainLast.setText(null);
        bMoveDownAccountLast.setText(null);
        bAddDomain.setText(null);
        bAddAccount.setText(null);
        bModifyDomain.setText(null);
        bModifyAccount.setText(null);
        bRemoveDomain.setText(null);
        bRemoveAccount.setText(null);
        bShowAndHidePassword.setText(null);
        bOverwriteFilter.setText(null);

        // Tootips
        bMoveUpDomainByOne.setTooltip(new Tooltip("Fit the selected domain of a place"));
        bMoveUpAccountByOne.setTooltip(new Tooltip("Fit the selected account of a place"));
        bMoveDownDomainByOne.setTooltip(new Tooltip("Move down the selected domain by one place"));
        bMoveDownAccountByOne.setTooltip(new Tooltip("Move down the selected account by one place"));
        bMoveUpDomainTop.setTooltip(new Tooltip("Move the selected domain to the first place"));
        bMoveUpAccountTop.setTooltip(new Tooltip("Move the selected account to the first place"));
        bMoveDownDomainLast.setTooltip(new Tooltip("Move down the selected domain to the last place"));
        bMoveDownAccountLast.setTooltip(new Tooltip("Move down the selected account to the last place"));
        bAddDomain.setTooltip(new Tooltip("Add a new domain"));
        bAddAccount.setTooltip(new Tooltip("Add a new account"));
        bModifyDomain.setTooltip(new Tooltip("Edit the selected domain"));
        bModifyAccount.setTooltip(new Tooltip("Edit the selected account"));
        bRemoveDomain.setTooltip(new Tooltip("Delete the selected domain"));
        bRemoveAccount.setTooltip(new Tooltip("Delete the selected account"));
        bShowAndHidePassword.setTooltip(new Tooltip("Show and hide password"));
        bOverwriteFilter.setTooltip(new Tooltip("Cancel searching"));

        // Management of button activations
        bMoveUpDomainByOne.setDisable(true);
        bMoveUpAccountByOne.setDisable(true);
        bMoveDownDomainByOne.setDisable(true);
        bMoveDownAccountByOne.setDisable(true);
        bMoveUpDomainTop.setDisable(true);
        bMoveUpAccountTop.setDisable(true);
        bMoveDownDomainLast.setDisable(true);
        bMoveDownAccountLast.setDisable(true);
        bAddDomain.setDisable(true);
        bAddAccount.setDisable(true);
        bModifyDomain.setDisable(true);
        bModifyAccount.setDisable(true);
        bRemoveDomain.setDisable(true);
        bRemoveAccount.setDisable(true);
        bShowAndHidePassword.setDisable(true);
        bOverwriteFilter.setDisable(true);

        // Setup images
        bMoveUpDomainByOne.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_UP, 16, 16, true));
        bMoveUpAccountByOne.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_UP, 16, 16, true));
        bMoveDownDomainByOne.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_DOWN, 16, 16, true));
        bMoveDownAccountByOne.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_DOWN, 16, 16, true));
        bMoveUpDomainTop.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_DOUBLE_UP, 16, 16, true));
        bMoveUpAccountTop.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_DOUBLE_UP, 16, 16, true));
        bMoveDownDomainLast.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_DOUBLE_DOWN, 16, 16, true));
        bMoveDownAccountLast.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_DOUBLE_DOWN, 16, 16, true));
        bAddDomain.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_ADD, 16, 16, true));
        bAddAccount.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_ADD, 16, 16, true));
        bModifyDomain.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_INFO, 16, 16, true));
        bModifyAccount.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_INFO, 16, 16, true));
        bRemoveDomain.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_REMOVE, 16, 16, true));
        bRemoveAccount.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_REMOVE, 16, 16, true));
        bShowAndHidePassword.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_EYE, 16, 16, true));
        bOverwriteFilter.setGraphic(imageManager.constructImageViewFrom(ImageManager.ICON_REMOVE, 16, 16, true));
    }

    private void updateFieldControllers() {
        bMoveUpDomainTop.setDisable(false);
        bMoveUpDomainByOne.setDisable(false);
        bMoveDownDomainByOne.setDisable(false);
        bMoveDownDomainLast.setDisable(false);
        bAddDomain.setDisable(false);
        bModifyDomain.setDisable(false);
        bRemoveDomain.setDisable(false);
        bOverwriteFilter.setDisable(false);
    }

    private void updateAccountControllers() {
        bMoveUpAccountTop.setDisable(false);
        bMoveUpAccountByOne.setDisable(false);
        bMoveDownAccountByOne.setDisable(false);
        bMoveDownAccountLast.setDisable(false);
        bAddAccount.setDisable(false);
        bModifyAccount.setDisable(false);
        bRemoveAccount.setDisable(false);
        bShowAndHidePassword.setDisable(false);
    }

    @FXML
    public void addField() {
        System.out.println("[FIELD] Add clicked!");
    }

    @FXML
    public void modifyField() {
        System.out.println("[FIELD] Modify clicked!");
    }

    @FXML
    public void removeField() {
        System.out.println("[FIELD] Remove clicked!");
    }

    @FXML
    public void addAccount() {
        System.out.println("[ACCOUNT] Add clicked!");
    }

    @FXML
    public void modifyAccount() {
        System.out.println("[ACCOUNT] Modify clicked!");
    }

    @FXML
    public void removeAccount() {
        System.out.println("[ACCOUNT] Remove clicked!");
    }

    private void updateFieldPanel(Field field) {
        if (field == null || field.equals(fieldSelected)) return;

        fieldSelected = field;
        accountSelected = null;

        //detailsRoot.getChildren().remove(detailsIdleView);
        initAccountTable(field);
        //accounts = FXCollections.observableArrayList(field.getAccounts());
        //tvAccounts.setItems(accounts);
        //accounts.addListener((InvalidationListener) observable -> updateUi());

        lDetailsTitle.textProperty().bind(new SimpleStringProperty(field.getName()));
        lDetailsDomain.textProperty().bind(new SimpleStringProperty(field.getData()));
        lDetailsAccountSize.textProperty().bind(Bindings.size(accounts).asString());
        lDetailsNotes.textProperty().bind(Bindings.createStringBinding(() -> {
            if (field.getNotes().length() > 0) {
                lDetailsNotes.getStyleClass().clear();
                return field.getNotes();
            } else {
                lDetailsNotes.getStyleClass().add("lItalic");
                return "Empty notes";
            }
        }, new SimpleStringProperty(field.getNotes())));

        //updateUi();
    }

//    private void updateAccount(Account account) {
//        System.out.println("Update account clicked!");
//    }

//    private void updateUi() {
//        if (lvDomains.getItems().size() == 0) {
//            setFieldIdlePanel();
//        }
//
//        //initAccountTable();
//    }

    private void setFieldIdlePanel() {
        lDetailsTitle.setText("");
        lDetailsDomain.setText("");
        lDetailsAccountSize.setText("");
        lDetailsNotes.setText("");
    }


    private void setAnchor(Node n) {
        AnchorPane.setTopAnchor(n, .0);
        AnchorPane.setRightAnchor(n, .0);
        AnchorPane.setBottomAnchor(n, .0);
        AnchorPane.setLeftAnchor(n, .0);
    }

    private void setAnchor(Node... n) {
        for (Node ns : n)
            setAnchor(ns);
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    //Account
    @FXML
    public void moveUpAccountTop() {
        if (fields == null || fieldSelected == null || accountSelected == null) return;

        int li = fieldSelected.getAccounts().lastIndexOf(accountSelected);
        if (li == 0) return; //jest juz na pierwszej pozycji
        Account account = fieldSelected.getAccounts().get(li);
        Account accTop = fieldSelected.getAccounts().get(0);

        List<Account> accountList = new ArrayList<>();
        fieldSelected.getAccounts().forEach(a -> {
            if(a.equals(accTop)) {
                accountRepository.updateAccount(a.replaceAccount(account));
                accountList.add(a.replaceAccount(account));
                return;
            }
            if(a.equals(account)) {
                accountRepository.updateAccount(a.replaceAccount(accTop));
                accountList.add(a.replaceAccount(accTop));
                return;
            }
            accountList.add(a);
        });
        fieldSelected.setAccounts(accountList);
        initAccountTable(fieldSelected);
        
//        Account account = fieldSelected.getAccounts().get(li);
//        fieldSelected.getAccounts().remove(account);
//        accountList.add(account);
//        fieldSelected.getAccounts().forEach(a -> accountList.add(a));
//        fieldSelected.setAccounts(accountList);
//        deSelection(1);
//        initAccountTable(fieldSelected);
        //selection(0, 0);
    }

    @FXML
    public void moveUpAccountByOne() {
        if (fields == null || fieldSelected == null || accountSelected == null) return;

        int li = fieldSelected.getAccounts().lastIndexOf(accountSelected);
        if (li == 0) return; //jest juz na pierwszej pozycji
        Account account1 = fieldSelected.getAccounts().get(li - 1);
        Account account2 = fieldSelected.getAccounts().get(li);

//        System.out.println(account1.getUsername()+":"+account1.getPassword(true));
//        System.out.println(account2.getUsername()+":"+account2.getPassword(true));
        List<Account> accountList = new ArrayList<>();
        fieldSelected.getAccounts().forEach(a -> {
            if (a.equals(account1)) {
                accountList.add(a.replaceAccount(account2));
                accountRepository.updateAccount(a.replaceAccount(account2));
                return;
            }
            if (a.equals(account2)) {
                accountList.add(a.replaceAccount(account1));
                accountRepository.updateAccount(a.replaceAccount(account1));
                return;
            }
            accountList.add(a);
        });
        fieldSelected.setAccounts(accountList);
        initAccountTable(fieldSelected);
        //selection(0, li - 1);
    }

    @FXML
    public void moveDownAccountByOne() {
        if (fields == null || fieldSelected == null || accountSelected == null) return;

        int li = fieldSelected.getAccounts().lastIndexOf(accountSelected);
        if (li == fieldSelected.getAccounts().size() - 1) return; //jest już na ostatniej pozycji
        Account account1 = fieldSelected.getAccounts().get(li + 1);
        Account account2 = fieldSelected.getAccounts().get(li);

//        System.out.println(account1.getUsername()+":"+account1.getPassword(true));
//        System.out.println(account2.getUsername()+":"+account2.getPassword(true));
        List<Account> accountList = new ArrayList<>();
        fieldSelected.getAccounts().forEach(a -> {
            if (a.equals(account1)) {
                accountRepository.updateAccount(a.replaceAccount(account2));
                accountList.add(a.replaceAccount(account2));
                return;
            }
            if (a.equals(account2)) {
                accountRepository.updateAccount(a.replaceAccount(account1));
                accountList.add(a.replaceAccount(account1));
                return;
            }
            accountList.add(a);
        });
        fieldSelected.setAccounts(accountList);
        initAccountTable(fieldSelected);
        //selection(0, li + 1);
    }

    @FXML
    public void moveDownAccountLast() {
        if (fields == null || fieldSelected == null || accountSelected == null) return;

        int litem = fieldSelected.getAccounts().size() - 1;
        int li = fieldSelected.getAccounts().lastIndexOf(accountSelected);
        if (li == litem) return; //jest już na ostatniej pozycji

        Account account = fieldSelected.getAccounts().get(li);
        Account accDown = fieldSelected.getAccounts().get(litem);

        List<Account> accountList = new ArrayList<>();
        fieldSelected.getAccounts().forEach(a -> {
            if(a.equals(accDown)) {
                accountRepository.updateAccount(a.replaceAccount(account));
                accountList.add(a.replaceAccount(account));
                return;
            }
            if(a.equals(account)) {
                accountRepository.updateAccount(a.replaceAccount(accDown));
                accountList.add(a.replaceAccount(accDown));
                return;
            }
            accountList.add(a);
        });
        fieldSelected.setAccounts(accountList);
        initAccountTable(fieldSelected);

//        Account account = fieldSelected.getAccounts().get(li);
//        List<Account> accountList = new ArrayList<>();
//        fieldSelected.getAccounts().remove(account);
//        fieldSelected.getAccounts().forEach(a -> accountList.add(a));
//        accountList.add(account);
//        fieldSelected.setAccounts(accountList);
//        deSelection(1);
//        initAccountTable(fieldSelected);
//        //selection(0, litem);
    }

    @FXML
    public void OnClickshowAndHidePassword() {
        initAccountTable(fieldSelected);
    }

    //Field
    @FXML
    public void moveUpDomainTop() {
        if (fields == null || fieldSelected == null) return;

        int li = fields.lastIndexOf(fieldSelected);
        if (li == 0) return; //jest juz na pierwszej pozycji

        selection(1, 0);
    }

    @FXML
    public void moveUpDomainByOne() {
        if (fields == null || fieldSelected == null) return;

        int li = fields.lastIndexOf(fieldSelected);
        if (li == 0) return; //jest juz na pierwszej pozycji

        selection(1, li - 1);
    }

    @FXML
    public void moveDownDomainByOne() {
        if (fields == null || fieldSelected == null) return;

        int li = fields.lastIndexOf(fieldSelected);
        if (li == fields.size() - 1) return; //jest juz na ostatniej pozycji

        selection(1, li + 1);
    }

    @FXML
    public void moveDownDomainLast() {
        if (fields == null || fieldSelected == null) return;

        int litem = fields.size() - 1;
        int li = fields.lastIndexOf(fieldSelected);
        if (li == litem) return; //jest już na ostatniej pozycji

        selection(1, litem);
    }

    //Search filter
    void initUi() {
        // Filter (http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/)
        FilteredList<Field> filteredList = new FilteredList<>(fields, p -> true);
        tfFilter.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(name -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            if (name.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (name.getData().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }

            return false;
        }));

        lvDomains.setItems(filteredList);
        filteredList.addListener((InvalidationListener) observable -> updateFieldPanel(filteredList.get(0)));
    }

    private void selection(int level, int item) { // 0 = Account, 1 = Field
        switch (level) {
            case 0:
                if (fieldSelected != null && item < tvAccounts.getItems().size())
                    tvAccounts.getSelectionModel().select(item);
                break;
            case 1:
                if (fields != null && item < lvDomains.getItems().size())
                    lvDomains.getSelectionModel().select(item);
                break;
        }
    }

    private void deSelection(int level) { // level = 1 : Field, 2 : Field + Account
        if (level > 0 && tvAccounts.getSelectionModel().getSelectedItems() != null) {
            tvAccounts.getSelectionModel().clearSelection();
            accountSelected = null;
        }

        if (level > 1 && lvDomains.getSelectionModel().getSelectedItem() != null) {
            lvDomains.getSelectionModel().clearSelection();
            fieldSelected = null;
        }
    }

    void selectField(Field d) {
        if (lvDomains.getItems().contains(d)) {
            lvDomains.getSelectionModel().select(d);
            fieldSelected = d;
        }
    }

    void selectAccount(Account c) {
        if (tvAccounts.getItems().contains(c)) {
            tvAccounts.getSelectionModel().select(c);
            accountSelected = c;
        }
    }
}
