package GUI.Controller;

import BE.Case;
import BE.Customer;
import BE.Report;
import BE.Technician;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerHomePageView implements Initializable {
    @FXML
    private ComboBox cbTechnician;
    @FXML
    private TableColumn colCaseName, colCaseID, colTechnicians, colCreatedDate;
    @FXML
    private TableView tblViewExistingCases;
    @FXML
    private ImageView imgSearch;
    @FXML
    private Button btnCreateNewCase, btnAddTechnician;
    @FXML
    private TextField txtCaseName, txtCaseID, txtContactPerson, txtSearchBar;
    @FXML
    private Label lblCustomerName;
    private ControllerAssistant controllerAssistant;
    private Model model;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private ObservableList<Case> caseObservableList;
    private ObservableList<Technician> technicianObservableList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        caseObservableList = FXCollections.observableArrayList();
        technicianObservableList = FXCollections.observableArrayList();
        lblCustomerName.setText(model.getCurrentCustomer().getCustomerName() + " Home Page");
        addListeners();
        btnCreateNewCase.setDisable(true);
        addShadow(txtCaseName, txtCaseID, txtContactPerson);
        updateTableView();
        disableAddTechnicians();
        updateTechnicians();

    }

    private void updateTechnicians() { //TODO continue from here and make sure all Users are represented by Names only in the ComboBox
        try {
            technicianObservableList.addAll(model.getAllTechnicians());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cbTechnician.setItems(technicianObservableList);

    }

    private void disableAddTechnicians() {
        btnAddTechnician.setDisable(true);
        cbTechnician.setDisable(true);
    }

    private void updateTableView() {
        colCaseID.setCellValueFactory(new PropertyValueFactory<>("caseID"));
        colCaseName.setCellValueFactory(new PropertyValueFactory<>("caseName"));
        colTechnicians.setCellValueFactory(new PropertyValueFactory<>("assignedTechnician"));
        colCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        caseObservableList.clear();
        try {
            caseObservableList.addAll(model.getCasesForThisCustomer(model.getCurrentCustomer().getCustomerID()));
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get cases from database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        tblViewExistingCases.setItems(caseObservableList);
    }

    private void addListeners() {
        txtCaseName.textProperty().addListener(createNewCaseBtn);
        txtCaseID.textProperty().addListener(createNewCaseBtn);
        txtContactPerson.textProperty().addListener(createNewCaseBtn);

        tblViewExistingCases.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // A case is selected, so enable the technician combobox
                cbTechnician.setDisable(false);
                // Add a listener to the technician combobox to enable the "Add Technician" button when a technician is chosen
                cbTechnician.getSelectionModel().selectedItemProperty().addListener((obs2, oldTechnician, newTechnician) -> {
                    if (newTechnician != null) {
                        // A technician is selected, so enable the "Add Technician" button
                        btnAddTechnician.setDisable(false);
                        addShadow(btnAddTechnician);
                    } else {
                        // No technician is selected, so disable the "Add Technician" button
                        btnAddTechnician.setDisable(true);
                        removeShadow(btnAddTechnician);
                    }
                });
            } else {
                // No case is selected, so disable the technician combobox and "Add Technician" button
                cbTechnician.setDisable(true);
                cbTechnician.getSelectionModel().clearSelection();
                btnAddTechnician.setDisable(true);
                removeShadow(btnAddTechnician);
            }
        });

    }

    ChangeListener<String> createNewCaseBtn = (observable, oldValue, newValue) -> {
        if (txtCaseName.getText().isEmpty() || txtCaseID.getText().isEmpty() || txtContactPerson.getText().isEmpty()) {
            btnCreateNewCase.setDisable(true);
            removeShadow(btnCreateNewCase);
        } else {
            btnCreateNewCase.setDisable(false);
            addShadow(btnCreateNewCase);
        }
    };


    private void addShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(shadow);
        }
    }

    private void removeShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(null);
        }
    }

    public void handleCreateNewCase(ActionEvent actionEvent) {
    }

    public void search(MouseEvent mouseEvent) {
    }

    public void handleAddTechnician(ActionEvent actionEvent) {
    }

    public void searchKey(KeyEvent keyEvent) {
    }
}
