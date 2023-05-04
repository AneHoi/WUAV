package GUI.Controller;

import BE.Case;
import BE.CaseAndCustomer;
import BE.Customer;
import GUI.Model.Model;
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
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class SearchForCaseView implements Initializable {
    @FXML
    private TextField txtCustomer, txtCustomerAddress, txtCaseName, txtTechnician;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Button btnFilter, btnClear;
    @FXML
    private TableView<CaseAndCustomer> tblViewFilteredCases;
    @FXML
    private TableColumn colCustomer, colCustomerAddress, colCaseName, colTechnician, colCreatedDate;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private ControllerAssistant controllerAssistant;
    private Model model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        addShadow(txtCustomer, txtCustomerAddress, txtCaseName, txtTechnician, dpDate, btnFilter, btnClear);
        updateTableView();
    }

    private void updateTableView() {
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colCaseName.setCellValueFactory(new PropertyValueFactory<>("caseName"));
        colTechnician.setCellValueFactory(new PropertyValueFactory<>("technicianName"));
        colCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));

        ObservableList<CaseAndCustomer> data = FXCollections.observableArrayList();
        List<Case> cases;
        List<Customer> customers;
        try {
            cases = model.getAllCases();
            customers = model.getAllCustomers();
            for (int i = 0; i < cases.size(); i++) {
                Case caseObj = cases.get(i);
                Customer customerObj = customers.get(i);
                CaseAndCustomer caseAndCustomer = new CaseAndCustomer(caseObj, customerObj);
                data.add(caseAndCustomer);
            }
            tblViewFilteredCases.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get Cases and Customers for list", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }


    private void addShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(shadow);
        }
    }


    public void handleFilter(ActionEvent actionEvent) {
        // Get the filter criteria from the text fields and date picker
        String customerName = txtCustomer.getText().trim();
        String customerAddress = txtCustomerAddress.getText().trim();
        String caseName = txtCaseName.getText().trim();
        String technicianName = txtTechnician.getText().trim();
        LocalDate createdDate = dpDate.getValue();

        // Create a filtered list that contains only the rows that match the filter criteria
        ObservableList<CaseAndCustomer> filteredList = FXCollections.observableArrayList();
        for (CaseAndCustomer caseAndCustomer : tblViewFilteredCases.getItems()) {
            if (caseAndCustomer.getCustomerName().toLowerCase().contains(customerName.toLowerCase())
                    && caseAndCustomer.getCustomerAddress().toLowerCase().contains(customerAddress.toLowerCase())
                    && caseAndCustomer.getCaseName().toLowerCase().contains(caseName.toLowerCase())
                    && caseAndCustomer.getTechnicianName().toLowerCase().contains(technicianName.toLowerCase())
                    && (createdDate == null || caseAndCustomer.getCreatedDate().isEqual(createdDate))) {
                filteredList.add(caseAndCustomer);
            }
        }

        // Update the TableView to display the filtered list
        tblViewFilteredCases.setItems(filteredList);
    }

    public void handleClear(ActionEvent actionEvent) {
        txtCustomer.clear();
        txtCustomerAddress.clear();
        txtCaseName.clear();
        txtTechnician.clear();
        dpDate.setValue(null);
        updateTableView();
    }

    public void chooseCaseAndCustomer(){
        //TODO needs implementation when you double click a case/customer it opens the Customer (or Case) Home Page and changes the currentCustomer and CurrentCase in the model;
    }

}

