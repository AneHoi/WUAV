package GUI.Controller;

import BE.Case;
import BE.Customer;
import BE.Report;
import BE.ReportCaseAndCustomer;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class SearchForCaseController implements Initializable {
    @FXML
    private TextField txtReportName, txtCustomer, txtCustomerAddress, txtCaseName, txtTechnician;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Button btnFilter, btnClear;
    @FXML
    private TableView<ReportCaseAndCustomer> tblViewFilteredReports;
    @FXML
    private TableColumn colReportName, colCustomer, colCustomerAddress, colCaseName, colTechnician, colCreatedDate;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private ControllerAssistant controllerAssistant;
    private Model model;
    private Report selectedReport;
    private Case selectedCase;
    private Customer selectedCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        checkForOldCases();
        controllerAssistant = ControllerAssistant.getInstance();
        addShadow(txtCustomer, txtCustomerAddress, txtCaseName, txtTechnician, dpDate, btnFilter, btnClear);
        dpDate.setPrefWidth(600);
        addListeners();
        updateTableView();
    }

    private void checkForOldCases() {
        ObservableList<Case> oldCases = FXCollections.observableArrayList();
        List<Case> caseList;
        try {
            caseList = model.getAllCases();
            for (Case caseBE : caseList) {
                if (tooOld(caseBE)) {
                    oldCases.add(caseBE);
                }
            }
            if(oldCases.size() > 0){
                openCaseAgePopUp(true, oldCases);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get Cases from the database", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }

    /**
     * Checks if the case is older than 4 years.
     * @param casen is the case to be checked
     * @return boolean
     */
    private boolean tooOld(Case casen) {
        LocalDateTime dateToday = LocalDate.now().atStartOfDay();
        LocalDateTime dateCreated = casen.getCreatedDate().atStartOfDay();
        long daysBetween = Duration.between(dateCreated, dateToday).toDays();
        long yearsBetween = Math.round(daysBetween/365);
        if(yearsBetween > 4){
            return true;
        }
        else {
            return false;
        }
    }

    private void openCaseAgePopUp(boolean open, ObservableList<Case> oldCases) {
        if (open) {
            PopUpAgeOfCasesController popUpAgeOfCasesController = new PopUpAgeOfCasesController();
            popUpAgeOfCasesController.setTooOldCases(oldCases);
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setController(popUpAgeOfCasesController);
            loader.setLocation(getClass().getResource("/GUI/View/PopUpAgeOfCases.fxml"));
            stage.setTitle("age of cases");
            try {
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open age of cases Window", ButtonType.CANCEL);
                alert.showAndWait();
            }
        }
    }

    private void updateTableView() {
        colReportName.setCellValueFactory(new PropertyValueFactory<>("reportName"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colCaseName.setCellValueFactory(new PropertyValueFactory<>("caseName"));
        colTechnician.setCellValueFactory(new PropertyValueFactory<>("technicianName"));
        colCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        tblViewFilteredReports.getColumns().addAll();

        ObservableList<ReportCaseAndCustomer> data = FXCollections.observableArrayList();
        List<ReportCaseAndCustomer> reportCaseAndCustomers;
        try {
            reportCaseAndCustomers = model.getAllReports();
            for (ReportCaseAndCustomer rCC : reportCaseAndCustomers) {
                data.add(rCC);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get Cases and Customers for list", ButtonType.CANCEL);
            alert.showAndWait();
        }
        tblViewFilteredReports.setItems(data);
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
        for (ReportCaseAndCustomer reportCaseAndCustomer : tblViewFilteredReports.getItems()) {
            if (reportCaseAndCustomer.getReportName().toLowerCase().contains(reportName.toLowerCase()) && reportCaseAndCustomer.getCustomerName().toLowerCase().contains(customerName.toLowerCase())
                    && reportCaseAndCustomer.getCustomerAddress().toLowerCase().contains(customerAddress.toLowerCase())
                    && reportCaseAndCustomer.getCaseName().toLowerCase().contains(caseName.toLowerCase())
                    && reportCaseAndCustomer.getTechnicianName().toLowerCase().contains(technicianName.toLowerCase())
                    && (createdDate == null || reportCaseAndCustomer.getCreatedDate().isEqual(createdDate))) {
                filteredList.add(reportCaseAndCustomer);
            }
        }

        // Update the TableView to display the filtered list
        tblViewFilteredReports.setItems(filteredList);
    }

    private void addListeners() {
        tblViewFilteredReports.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblViewFilteredReports.getSelectionModel().getSelectedItem() != null) {
                int reportID = tblViewFilteredReports.getSelectionModel().getSelectedItem().getReportId();
                int caseId = tblViewFilteredReports.getSelectionModel().getSelectedItem().getCaseId();
                int customerId = tblViewFilteredReports.getSelectionModel().getSelectedItem().getCustomerId();
                try {
                    Report chosenReport = model.getChosenReport(reportID).get(0);
                    Case chosenCase = model.getChosenCase(caseId).get(0);
                    Customer chosenCustomer = model.getChosenCustomer(customerId).get(0);
                    model.setCurrentReport(chosenReport);
                    model.setCurrentCase(chosenCase);
                    model.setCurrentCustomer(chosenCustomer);
                    controllerAssistant.loadCenter("ReportHomePageView.fxml");

                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Report Home Page", ButtonType.CANCEL);
                    alert.showAndWait();
                }

            }
        });
        txtReportName.textProperty().addListener(tableViewUpdate);
        txtCustomer.textProperty().addListener(tableViewUpdate);
        txtCustomerAddress.textProperty().addListener(tableViewUpdate);
        txtCaseName.textProperty().addListener(tableViewUpdate);
        txtTechnician.textProperty().addListener(tableViewUpdate);
        dpDate.editorProperty().addListener((ChangeListener) tableViewUpdate);
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
            for (ReportCaseAndCustomer reportCaseAndCustomer : tblViewFilteredReports.getItems()) {
                if (reportCaseAndCustomer.getReportName().toLowerCase().contains(reportName.toLowerCase()) && reportCaseAndCustomer.getCustomerName().toLowerCase().contains(customerName.toLowerCase())
                        && reportCaseAndCustomer.getCustomerAddress().toLowerCase().contains(customerAddress.toLowerCase())
                        && reportCaseAndCustomer.getCaseName().toLowerCase().contains(caseName.toLowerCase())
                        && reportCaseAndCustomer.getTechnicianName().toLowerCase().contains(technicianName.toLowerCase())
                        && (createdDate == null || reportCaseAndCustomer.getCreatedDate().isEqual(createdDate))) {
                    filteredList.add(reportCaseAndCustomer);
                }
            }
            tblViewFilteredReports.setItems(filteredList);
        }
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

}

