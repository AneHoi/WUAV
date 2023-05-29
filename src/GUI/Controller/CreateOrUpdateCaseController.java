package GUI.Controller;

import BE.Case;
import BE.Customer;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateOrUpdateCaseController implements Initializable {

    @FXML
    private Label lblCreateOrUpdate, lblCaseName;
    @FXML
    private TextField txtCaseName, txtContactPerson;
    @FXML
    private TextArea txtCaseDescription;
    @FXML
    private Button btnCreateOrUpdateCase;
    private Model model;
    private Case currentCase;
    private Customer currentCustomer;
    private Util util = new Util();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        checkCurrentCase();
    }

    private void checkCurrentCase() {
        if (currentCase != null) {
            updateCase();
        } else {
            createCase();
        }
    }

    private void createCase() {
        lblCreateOrUpdate.setText("Create Case:");
        lblCaseName.setText("");
        txtCaseName.setPromptText("Case Name...");
        txtCaseDescription.setPromptText("Case Description...");
        util.removeShadow(btnCreateOrUpdateCase);
        btnCreateOrUpdateCase.setDisable(true);
        txtCaseName.textProperty().addListener(createNewCaseBtnListener);
        txtCaseDescription.textProperty().addListener(createNewCaseBtnListener);
        btnCreateOrUpdateCase.setText("Create Case");
    }

    private void updateCase() {
        lblCreateOrUpdate.setText("Update Case:");
        lblCaseName.setText(currentCase.getCaseName());
        txtCaseName.setPromptText("Update Case Name...");
        txtCaseName.setText(currentCase.getCaseName());
        txtContactPerson.setPromptText("Update Case Name...");
        txtContactPerson.setText(currentCase.getContactPerson());
        txtCaseDescription.setPromptText("Update Case Description...");
        txtCaseDescription.setText(currentCase.getCaseDescription());
        btnCreateOrUpdateCase.setText("Update Case");
        txtCaseName.textProperty().addListener(createNewCaseBtnListener);
        txtContactPerson.textProperty().addListener(createNewCaseBtnListener);
        txtCaseDescription.textProperty().addListener(createNewCaseBtnListener);
    }

    public void setCustomerAndCase(Case currentCase, Customer currentCustomer) {
        this.currentCase = currentCase;
        this.currentCustomer = currentCustomer;
    }

    public void handleCreateOrUpdateCase() {
        if (btnCreateOrUpdateCase.getText().equals("Create Case")) {
            handleCreateCase();
        } else {
            handleUpdateCase();
        }
    }

    private void handleUpdateCase() {
        int caseID = currentCase.getCaseID();
        String caseName = txtCaseName.getText();
        String contactPerson = txtContactPerson.getText();
        String caseDescription = txtCaseDescription.getText();
        try {
            model.updateCase(caseID, caseName, contactPerson, caseDescription);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not updated Case", ButtonType.CANCEL);
            alert.showAndWait();
        }
        Stage stage = (Stage) btnCreateOrUpdateCase.getScene().getWindow();
        stage.close();
    }

    private void handleCreateCase() {
        String caseName = txtCaseName.getText();
        String contactPerson = txtContactPerson.getText();
        String caseDescription = txtCaseDescription.getText();
        int customerID = currentCustomer.getCustomerID();
        try {
            model.createNewCase(caseName, contactPerson, caseDescription, customerID);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create a new Case", ButtonType.CANCEL);
            alert.showAndWait();
        }

        Stage stage = (Stage) btnCreateOrUpdateCase.getScene().getWindow();
        stage.close();
    }

    ChangeListener<String> createNewCaseBtnListener = (observable, oldValue, newValue) -> {
        if (txtCaseName.getText().isEmpty() || txtCaseDescription.getText().isEmpty() || txtContactPerson.getText().isEmpty()) {
            btnCreateOrUpdateCase.setDisable(true);
            util.removeShadow(btnCreateOrUpdateCase);
        } else {
            btnCreateOrUpdateCase.setDisable(false);
            util.addShadow(btnCreateOrUpdateCase);
        }
    };

    public void setOnlyCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }
}



