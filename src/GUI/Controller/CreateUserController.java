package GUI.Controller;

import BE.User;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateUserController implements Initializable {
    @FXML
    private TableColumn colName, colUserName, colUserType, colStatus;
    @FXML
    private TableView tblViewExistingUsers;
    @FXML
    private Button btnCreateNewUser, btnUpdateUser;
    @FXML
    private ComboBox cbUserActive;
    @FXML
    private TextField txtFullNameUpdate, txtUserNameUpdate, txtTelephoneUpdate, txtEmailUpdate;
    private Model model;
    private ControllerAssistant controllerAssistant;
    private ObservableList<String> userTypes;
    private ObservableList<User> allUsers;

    private ObservableList<String> activeOrInactive;
    private Util util = new Util();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        disableButtons(btnUpdateUser, txtFullNameUpdate, txtUserNameUpdate, txtTelephoneUpdate, txtEmailUpdate, cbUserActive);
        updateTableView();
        addListeners();
        userTypes = FXCollections.observableArrayList();
        activeOrInactive = FXCollections.observableArrayList();
        userTypes = util.checkLoggedInUser(userTypes);
        util.addShadow(btnCreateNewUser);

        activeOrInactive.add("Active");
        activeOrInactive.add("Inactive");
    }

    private void disableButtons(Node... node) {
        for (Node nodes : node) {
            nodes.setDisable(true);
        }
    }

    private void enableButtons(Node... node) {
        for (Node nodes : node) {
            nodes.setDisable(false);
        }
    }

    private void addListeners() {
        tblViewExistingUsers.getSelectionModel().selectedItemProperty().addListener(selectedUserListener);
        txtFullNameUpdate.textProperty().addListener(updateUserBtn);
        txtUserNameUpdate.textProperty().addListener(updateUserBtn);
        txtTelephoneUpdate.textProperty().addListener(updateUserBtn);
        txtEmailUpdate.textProperty().addListener(updateUserBtn);
        cbUserActive.valueProperty().addListener(updateUserBtn); // Add listener to ComboBox value property
    }

    ChangeListener<String> updateUserBtn = (observable, oldValue, newValue) -> {
        updateUpdateButtonState(); // Extracted method to update the button state
    };

    ChangeListener<User> selectedUserListener = (observable, oldValue, newValue) -> {
        if (newValue != null) {
            enableButtons(btnUpdateUser, txtFullNameUpdate, txtUserNameUpdate, txtTelephoneUpdate, txtEmailUpdate, cbUserActive);
            util.addShadow(btnUpdateUser, txtFullNameUpdate, txtUserNameUpdate, txtTelephoneUpdate, txtEmailUpdate, cbUserActive);
            txtFullNameUpdate.setText(newValue.getFullName());
            txtUserNameUpdate.setText(newValue.getUserName());
            txtTelephoneUpdate.setText(newValue.getTelephone());
            txtEmailUpdate.setText(newValue.getEmail());
            cbUserActive.setItems(activeOrInactive);
            cbUserActive.getSelectionModel().select(newValue.getIsActive());

            updateUpdateButtonState(); // Update the button state when a user is selected
        }
        else {
            disableButtons(btnUpdateUser, txtFullNameUpdate, txtUserNameUpdate, txtTelephoneUpdate, txtEmailUpdate, cbUserActive);
            util.removeShadow(btnUpdateUser, txtFullNameUpdate, txtUserNameUpdate, txtTelephoneUpdate, txtEmailUpdate, cbUserActive);
            txtFullNameUpdate.clear();
            txtUserNameUpdate.clear();
            txtTelephoneUpdate.clear();
            txtEmailUpdate.clear();
            cbUserActive.setItems(activeOrInactive);
            cbUserActive.getSelectionModel().select(null);
            updateUpdateButtonState(); // Update the button state when a user is selected
        }
    };

    // Method to update the state of the "Update User" button
    private void updateUpdateButtonState() {
        if (txtFullNameUpdate.getText().isEmpty() || txtUserNameUpdate.getText().isEmpty() || txtTelephoneUpdate.getText().isEmpty() || txtEmailUpdate.getText().isEmpty() || cbUserActive.getSelectionModel().getSelectedItem() == null) {
            btnUpdateUser.setDisable(true);
            util.removeShadow(btnUpdateUser);
        } else {
            btnUpdateUser.setDisable(false);
            util.addShadow(btnUpdateUser);
        }
    }


    private void updateTableView() {
        allUsers = FXCollections.observableArrayList();
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colUserType.setCellValueFactory(new PropertyValueFactory<>("userStringType"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        tblViewExistingUsers.getColumns().addAll();
        try {
            allUsers.addAll(model.getAllUsers());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get users from database", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
        tblViewExistingUsers.setItems(allUsers);
        tblViewExistingUsers.getSortOrder().add(colStatus);
    }

    public void handleCreateNewUser() {
        PopUpCreateUserController popUpCreateUserController = new PopUpCreateUserController();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(popUpCreateUserController);

        loader.setLocation(getClass().getResource("/GUI/View/PopUpCreateUserView.fxml"));
        stage.setTitle("Create new user");
        util.openNewWindow(stage, loader, "Could not create new user");

        updateTableView();
    }

    public void handleUpdateUser() {
        String fullName = txtFullNameUpdate.getText();
        String userName = txtUserNameUpdate.getText();
        String userTlf = txtTelephoneUpdate.getText();
        String userEmail = txtEmailUpdate.getText();
        boolean userActive = cbUserActive.getSelectionModel().getSelectedItem().equals("Active");
        User user = (User) tblViewExistingUsers.getSelectionModel().getSelectedItem();
        int userID = user.getUserID();

            if(cbUserActive.getSelectionModel().getSelectedItem().equals("Inactive")){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to make: " + user.getFullName() + " Inactive", ButtonType.YES, ButtonType.NO);
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
                dialogPane.getStyleClass().add("dialog");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    try {
                        model.updateUser(userID, fullName, userName, userTlf, userEmail, userActive);
                        Alert success = new Alert(Alert.AlertType.INFORMATION, "User: " + user.getFullName() + " was made inactive", ButtonType.OK );
                        success.showAndWait();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert alert1 = new Alert(Alert.AlertType.ERROR, "Could not update User", ButtonType.CANCEL);
                        DialogPane dialogPane1 = alert1.getDialogPane();
                        dialogPane1.getStylesheets().add("/GUI/View/css/Main.css");
                        dialogPane1.getStyleClass().add("dialog");
                        alert1.showAndWait();
                    }
                }
            }
            else {
                try {
                    model.updateUser(userID, fullName, userName, userTlf, userEmail, userActive);
                    Alert success = new Alert(Alert.AlertType.INFORMATION, "User: " + user.getFullName() + " was made active again", ButtonType.OK );
                    DialogPane dialogPane1 = success.getDialogPane();
                    dialogPane1.getStylesheets().add("/GUI/View/css/Main.css");
                    dialogPane1.getStyleClass().add("dialog");
                    success.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert1 = new Alert(Alert.AlertType.ERROR, "Could not update User", ButtonType.CANCEL);
                    DialogPane dialogPane2 = alert1.getDialogPane();
                    dialogPane2.getStylesheets().add("/GUI/View/css/Main.css");
                    dialogPane2.getStyleClass().add("dialog");
                    alert1.showAndWait();
                }
            }

        updateTableView();
    }
}
