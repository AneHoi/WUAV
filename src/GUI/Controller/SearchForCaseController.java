package GUI.Controller;

import BE.Case;
import BE.Customer;
import BE.Report;
import BE.ReportCaseAndCustomer;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class SearchForCaseController implements Initializable {
    @FXML
    private TextField txtReportName, txtCustomer, txtCustomerAddress, txtCaseName, txtTechnician;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Button btnClear;
    @FXML
    private TableView<ReportCaseAndCustomer> tblViewFilteredReports;
    @FXML
    private TableColumn colReportName, colCustomer, colCustomerAddress, colCaseName, colTechnician, colCreatedDate;
    private ControllerAssistant controllerAssistant;
    private Model model;
    private Util util = new Util();
    private Report selectedReport;
    private Case selectedCase;
    private Customer selectedCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        checkForOldCases();
        controllerAssistant = ControllerAssistant.getInstance();
        util.addShadow(txtReportName, txtCustomer, txtCustomerAddress, txtCaseName, txtTechnician, dpDate, btnClear);
        dpDate.setPrefWidth(600);
        addListeners();
        updateTableView();
    }

    /**
     * Check if any cases are too old, if any case is to old opens too pop-up with too old cases
     */
    private void checkForOldCases() {
        List<Case> caseList;
        try {
            caseList = model.getAllCases();
            for (Case caseBE : caseList) {
                if (util.tooOld(caseBE)) {
                    openCaseAgePopUp();
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get Cases from the database", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
    }

    /**
     * Opens new view PopUpAgeOfCases.fxml with PopUpAgeOfCasesController as the controller. Sets title of view to Cases about to expire.
     */
    private void openCaseAgePopUp() {
        PopUpAgeOfCasesController popUpAgeOfCasesController = new PopUpAgeOfCasesController();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(popUpAgeOfCasesController);
        loader.setLocation(getClass().getResource("/GUI/View/PopUpAgeOfCases.fxml"));
        stage.setTitle("Cases about to expire");
        stage.initModality(Modality.APPLICATION_MODAL);
        util.openNewWindow(stage, loader, "Could not open cases about to expire Window");
    }

    /**
     * Creates list of ReportCaseAndCustomers, gets all reports from model, and inserts them into list. Sorts list by status.
     */
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
            Comparator<ReportCaseAndCustomer> byStatus = (ReportCaseAndCustomer pcc1, ReportCaseAndCustomer pcc2) -> pcc1.getReportStatus().compareTo(pcc2.getReportStatus());
            Collections.sort(reportCaseAndCustomers, byStatus);

            //Sorting the list by active cases, and does not add the "Submitted for review" status for the technicians
            if (controllerAssistant.getLoggedInUser().getUserType() == 3) {

                Comparator<ReportCaseAndCustomer> byOpenClosed = (ReportCaseAndCustomer pcc1, ReportCaseAndCustomer pcc2) -> pcc1.getReportStatus().compareTo(pcc2.getReportStatus());
                Collections.sort(reportCaseAndCustomers, Collections.reverseOrder(byOpenClosed));
                for (ReportCaseAndCustomer rCC : reportCaseAndCustomers) {
                    if (!rCC.getReportStatus().equalsIgnoreCase("submitted for review")) {
                        data.add(rCC);
                    }
                }
            }
            //Sorting by "Submitted for review", "open" and "closed" if a project manager is logged in
            else if (controllerAssistant.getLoggedInUser().getUserType() == 2) {
                //Sort the reports
                Comparator<ReportCaseAndCustomer> bySubOpenClosed = (ReportCaseAndCustomer report1, ReportCaseAndCustomer report2) -> {
                    if (report1.getReportStatus().equalsIgnoreCase("submitted for review") && report2.getReportStatus().equalsIgnoreCase("open")
                            || report1.getReportStatus().equalsIgnoreCase("submitted for review") && report2.getReportStatus().equalsIgnoreCase("closed")) {
                        //"Submit for review" is 'Highest'
                        return 1;
                    } else if (report1.getReportStatus().equalsIgnoreCase("open") && report2.getReportStatus().equalsIgnoreCase("closed")) {
                        //"Open" is 'higher' than "Closed"
                        return 1;
                    } else if (report1.getReportStatus().equalsIgnoreCase("closed") && report2.getReportStatus().equalsIgnoreCase("submitted for review")
                            || report1.getReportStatus().equalsIgnoreCase("closed") && report2.getReportStatus().equalsIgnoreCase("open")
                            || report1.getReportStatus().equalsIgnoreCase("open") && report2.getReportStatus().equalsIgnoreCase("submitted for review")) {
                        //"Closed" is the absolute 'lowest'
                        return -1;
                    } else {
                        return 0;
                    }
                };
                Collections.sort(reportCaseAndCustomers, Collections.reverseOrder(bySubOpenClosed));
                for (ReportCaseAndCustomer rCC : reportCaseAndCustomers) {
                    data.add(rCC);
                }

            } else {
                for (ReportCaseAndCustomer rCC : reportCaseAndCustomers) {
                    data.add(rCC);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get Cases and Customers for list", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
        tblViewFilteredReports.setItems(data);
    }

    /**
     *  adds listeners to tableviews where if double-clicked changes view to report homepage from current customer, current case and current report.
     *  adds listeners to text fields, and filters list in tableview based on changes in text fields. Updates tableview to show changes.
     */
    private void addListeners() {
        tblViewFilteredReports.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblViewFilteredReports.getSelectionModel().getSelectedItem() != null) {
                int reportID = tblViewFilteredReports.getSelectionModel().getSelectedItem().getReportId();
                int caseId = tblViewFilteredReports.getSelectionModel().getSelectedItem().getCaseId();
                int customerId = tblViewFilteredReports.getSelectionModel().getSelectedItem().getCustomerId();
                try {
                    Report chosenReport = model.getChosenReport(reportID);
                    Case chosenCase = model.getChosenCase(caseId);
                    Customer chosenCustomer = model.getChosenCustomer(customerId);
                    System.out.println(chosenReport.getReportID() + chosenCase.getCaseID() + chosenCustomer.getCustomerID());
                    model.setCurrentReport(chosenReport);
                    model.setCurrentCase(chosenCase);
                    model.setCurrentCustomer(chosenCustomer);
                    controllerAssistant.loadCenter("ReportHomePageView.fxml");

                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Report Home Page", ButtonType.CANCEL);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
                    dialogPane.getStyleClass().add("dialog");
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
        if (!txtReportName.getText().isEmpty() || !txtCustomer.getText().isEmpty() || !txtCustomerAddress.getText().isEmpty() || !txtCaseName.getText().isEmpty() || !txtTechnician.getText().isEmpty() || dpDate.getValue() != null) {
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
        } else {
            updateTableView();
        }
    };

    /**
     * If clear button is pressed, removes text from text fields and sets date in datepicker to null.
     */
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

