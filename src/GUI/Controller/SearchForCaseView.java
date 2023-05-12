package GUI.Controller;

import BE.Case;
import BE.Report;
import BE.ReportCaseAndCustomer;
import BE.Customer;
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
    private TextField txtReportName, txtCustomer, txtCustomerAddress, txtCaseName, txtTechnician;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Button btnFilter, btnClear;
    @FXML
    private TableView<ReportCaseAndCustomer> tblViewFilteredCases;
    @FXML
    private TableColumn colReportName, colCustomer, colCustomerAddress, colCaseName, colTechnician, colCreatedDate;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private ControllerAssistant controllerAssistant;
    private Model model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        addShadow(txtCustomer, txtCustomerAddress, txtCaseName, txtTechnician, dpDate, btnFilter, btnClear);
        addListeners();
        updateTableView();
    }

    private void updateTableView() {
        colReportName.setCellValueFactory(new PropertyValueFactory<>("reportName"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colCaseName.setCellValueFactory(new PropertyValueFactory<>("caseName"));
        colTechnician.setCellValueFactory(new PropertyValueFactory<>("technicianName"));
        colCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        tblViewFilteredCases.getColumns().addAll();

        ObservableList<ReportCaseAndCustomer> data = FXCollections.observableArrayList();
        List<ReportCaseAndCustomer> reportCaseAndCustomers;
        try {
            reportCaseAndCustomers = model.getAllReports();
            for (ReportCaseAndCustomer rCC: reportCaseAndCustomers) {
                data.add(rCC);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get Cases and Customers for list", ButtonType.CANCEL);
            alert.showAndWait();
        }
        tblViewFilteredCases.setItems(data);
    }


    private void addShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(shadow);
        }
    }


    public void handleFilter(ActionEvent actionEvent) {
        // Get the filter criteria from the text fields and date picker
        String reportName = txtReportName.getText().trim();
        String customerName = txtCustomer.getText().trim();
        String customerAddress = txtCustomerAddress.getText().trim();
        String caseName = txtCaseName.getText().trim();
        String technicianName = txtTechnician.getText().trim();
        LocalDate createdDate = dpDate.getValue();

        // Create a filtered list that contains only the rows that match the filter criteria
        ObservableList<ReportCaseAndCustomer> filteredList = FXCollections.observableArrayList();
        for (ReportCaseAndCustomer reportCaseAndCustomer : tblViewFilteredCases.getItems()) {
            if (reportCaseAndCustomer.getReportName().toLowerCase().contains(reportName.toLowerCase()) && reportCaseAndCustomer.getCustomerName().toLowerCase().contains(customerName.toLowerCase())
                    && reportCaseAndCustomer.getCustomerAddress().toLowerCase().contains(customerAddress.toLowerCase())
                    && reportCaseAndCustomer.getCaseName().toLowerCase().contains(caseName.toLowerCase())
                    && reportCaseAndCustomer.getTechnicianName().toLowerCase().contains(technicianName.toLowerCase())
                    && (createdDate == null || reportCaseAndCustomer.getCreatedDate().isEqual(createdDate))) {
                filteredList.add(reportCaseAndCustomer);
            }
        }

        // Update the TableView to display the filtered list
        tblViewFilteredCases.setItems(filteredList);
    }
    private void addListeners() {
        txtReportName.textProperty().addListener(tableViewUpdate);
        txtCustomer.textProperty().addListener(tableViewUpdate);
        txtCustomerAddress.textProperty().addListener(tableViewUpdate);
        txtCaseName.textProperty().addListener(tableViewUpdate);
        txtTechnician.textProperty().addListener(tableViewUpdate);
        dpDate.editorProperty().addListener((ChangeListener) tableViewUpdate );
    }
    ChangeListener<String> tableViewUpdate = (observable, oldValue, newValue) -> {
        String reportName = txtReportName.getText().trim();
        String customerName = txtCustomer.getText().trim();
        String customerAddress = txtCustomerAddress.getText().trim();
        String caseName = txtCaseName.getText().trim();
        String technicianName = txtTechnician.getText().trim();
        LocalDate createdDate = dpDate.getValue();

        ObservableList<ReportCaseAndCustomer> filteredList = FXCollections.observableArrayList();
        if (!txtReportName.getText().isEmpty() || !txtCustomer.getText().isEmpty() || !txtCustomerAddress.getText().isEmpty() || !txtCaseName.getText().isEmpty() || !txtTechnician.getText().isEmpty() || !dpDate.getValue().equals(null)) {
            for (ReportCaseAndCustomer reportCaseAndCustomer : tblViewFilteredCases.getItems()) {
                if (reportCaseAndCustomer.getReportName().toLowerCase().contains(reportName.toLowerCase()) && reportCaseAndCustomer.getCustomerName().toLowerCase().contains(customerName.toLowerCase())
                        && reportCaseAndCustomer.getCustomerAddress().toLowerCase().contains(customerAddress.toLowerCase())
                        && reportCaseAndCustomer.getCaseName().toLowerCase().contains(caseName.toLowerCase())
                        && reportCaseAndCustomer.getTechnicianName().toLowerCase().contains(technicianName.toLowerCase())
                        && (createdDate == null || reportCaseAndCustomer.getCreatedDate().isEqual(createdDate))) {
                    filteredList.add(reportCaseAndCustomer);
                }
            }
            tblViewFilteredCases.setItems(filteredList);
        } /**else if (txtReportName.getText().isEmpty() && txtCustomer.getText().isEmpty() && txtCustomerAddress.getText().isEmpty() && txtCaseName.getText().isEmpty() && txtTechnician.getText().isEmpty() && dpDate.getValue().equals(null)) {
            try {
                tblViewFilteredCases.setItems((ObservableList<ReportCaseAndCustomer>) model.getAllReports());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }*/
    };

    public void handleClear(ActionEvent actionEvent) {
        txtReportName.clear();
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

