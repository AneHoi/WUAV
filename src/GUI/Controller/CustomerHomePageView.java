package GUI.Controller;

import BE.Case;
import BE.Customer;
import BE.Report;
import BE.Technician;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerHomePageView implements Initializable {
    @FXML
    private TextArea txtCaseDescription;
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
    private TextField txtCaseName, txtContactPerson, txtSearchBar;
    @FXML
    private Label lblCustomerName;
    private ControllerAssistant controllerAssistant;
    private Model model;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private ObservableList<Case> caseObservableList;
    private ObservableList<Technician> technicianObservableList;

    private String search = "data/Images/search.png";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        caseObservableList = FXCollections.observableArrayList();
        technicianObservableList = FXCollections.observableArrayList();
        lblCustomerName.setText(model.getCurrentCustomer().getCustomerName() + " Home Page");
        imgSearch.setImage(loadImages(search));
        addListeners();
        btnCreateNewCase.setDisable(true);
        addShadow(txtCaseName, txtCaseDescription, txtContactPerson);
        updateTableView();
        disableAddTechnicians();
        updateTechnicians();
        searchBarFilter();

    }

    private void updateTechnicians() {
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
        txtCaseDescription.textProperty().addListener(createNewCaseBtn);
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
        tblViewExistingCases.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblViewExistingCases.getSelectionModel().getSelectedItem() != null) {
                Case selectedItem = (Case) tblViewExistingCases.getSelectionModel().getSelectedItem();
                try {
                    model.setCurrentCase(selectedItem);
                    controllerAssistant.loadCenter("CaseHomePageView.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Case Home Page", ButtonType.CANCEL);
                    alert.showAndWait();
                }

            }
        });


    }

    private Image loadImages(String url) {
        Image image = null;
        try {
            InputStream img = new FileInputStream(url);
            image = new Image(img);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load an image, following error occurred:\n" + e, ButtonType.CANCEL);
            alert.showAndWait();
        }
        return image;

    }

    ChangeListener<String> createNewCaseBtn = (observable, oldValue, newValue) -> {
        if (txtCaseName.getText().isEmpty() || txtCaseDescription.getText().isEmpty() || txtContactPerson.getText().isEmpty()) {
            btnCreateNewCase.setDisable(true);
            removeShadow(btnCreateNewCase);
        } else {
            btnCreateNewCase.setDisable(false);
            addShadow(btnCreateNewCase);
        }
    };

    private void searchBarFilter() {
        // Create a list to hold the original unfiltered items in the tblViewCustomers TableView
        ObservableList<Case> originalList = FXCollections.observableArrayList(tblViewExistingCases.getItems());

        // Add a listener to the txtSearchBar TextField to filter the tblViewCustomers TableView based on the user's input
        txtSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            // Create a filtered list that contains all items from the tblViewCustomers TableView
            FilteredList<Case> filteredList = new FilteredList<>(originalList);

            // Set a predicate to filter the items based on the user's input
            if (newValue == null || newValue.isEmpty()) {
                // If the user has not entered any input, display all items
                tblViewExistingCases.setItems(originalList);
            } else {
                // Otherwise, filter the items based on the user's input
                String lowerCaseFilter = newValue.toLowerCase();
                filteredList.setPredicate(cases -> {
                    String caseID = String.valueOf(cases.getCaseID());
                    String caseDate = String.valueOf(cases.getCreatedDate());
                    String assignedTechnician = Objects.toString(cases.getAssignedTechnician(), "");
                    assignedTechnician = assignedTechnician.toLowerCase();
                    return cases.getCaseName().toLowerCase().contains(lowerCaseFilter)
                            || assignedTechnician.contains(lowerCaseFilter)
                            || caseDate.contains(lowerCaseFilter)
                            || caseID.contains(lowerCaseFilter);
                });
                tblViewExistingCases.setItems(filteredList);
            }
        });
    }

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
        String caseName = txtCaseName.getText();
        String caseContact = txtCaseName.getText();
        String caseDescription = txtCaseName.getText();
        int customerID = model.getCurrentCustomer().getCustomerID();
        try {
            model.createNewCase(caseName, caseContact, caseDescription, customerID);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create New Case", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateTableView();
    }

    public void handleAddTechnician(ActionEvent actionEvent) { //TODO need to make a separate table in DB to hold all Technicians related to each case
        Case selectedCase = (Case) tblViewExistingCases.getSelectionModel().getSelectedItem();
        int caseID = selectedCase.getCaseID();
        Technician technician = (Technician) cbTechnician.getSelectionModel().getSelectedItem();
        int technicianID = technician.getUserID();
        try {
            model.addTechnicianToCase(caseID, technicianID);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not add Technician to Case", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateTableView();

    }

}
