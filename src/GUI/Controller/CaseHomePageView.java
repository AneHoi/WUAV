package GUI.Controller;

import BE.Addendum;
import BE.Case;
import BE.Customer;
import BE.Report;
import GUI.Model.Model;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class CaseHomePageView implements Initializable {
    @FXML
    private Label lblCaseName;
    @FXML
    private TextField txtReportName, txtSearchField, txtAddendumName;
    @FXML
    private TextArea txtReportDescription, txtAddendumDescription;
    @FXML
    private Button btnCreateNewReport, btnCreateNewAddendum;
    @FXML
    private TableView tblViewExistingReports;
    @FXML
    private TableColumn colReportName, colTechnician, colCreatedDate, colStatus;

    private ControllerAssistant controllerAssistant;

    private Model model;

    private Customer currentCustomer;
    private Case currentCase;

    private List<Report> reports;

    private ObservableList observableReports;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentCase = new Case(1, "Hej", "Hej", "ConBob", 1, "TechBob", LocalDate.now());
        currentCustomer = new Customer(1, "Norlys", "Gade 1", "+45 75 12 00 00", "email@email.com", 12345678, "Corporate");
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        updateTableView();
        disableAddendum();
        btnCreateNewReport.setDisable(true);
        lblCaseName.setText(currentCase.getCaseName());
        addListeners();


    }

    private void addListeners() {
        txtReportName.textProperty().addListener(createNewReportBtnListener);
        txtReportDescription.textProperty().addListener(createNewReportBtnListener);
        txtAddendumName.textProperty().addListener(createNewAddendumBtnListener);
        txtAddendumDescription.textProperty().addListener(createNewAddendumBtnListener);
        tblViewExistingReports.getSelectionModel().selectedItemProperty().addListener(selectedItemListener);
        txtAddendumName.textProperty().addListener(textFieldListener);
        txtAddendumDescription.textProperty().addListener(textFieldListener);

        tblViewExistingReports.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblViewExistingReports.getSelectionModel().getSelectedItem() != null) {
                Report selectedItem = (Report) tblViewExistingReports.getSelectionModel().getSelectedItem();
                if (selectedItem.isActive()) {
                    try {
                        model.setCurrentReport(selectedItem);
                        model.setCurrentCase(currentCase);
                        model.setCurrentCustomer(currentCustomer);
                        controllerAssistant.loadCenter("ReportHomePageView.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Report Home Page", ButtonType.CANCEL);
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Report is inactive, please make a Addendum instead", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });

    }


    ChangeListener<String> createNewReportBtnListener = (observable, oldValue, newValue) -> {
        if (txtReportName.getText().isEmpty() || txtReportDescription.getText().isEmpty()) {
            btnCreateNewReport.setDisable(true);
        } else {
            btnCreateNewReport.setDisable(false);
        }
    };

    ChangeListener<String> createNewAddendumBtnListener = (observable, oldValue, newValue) -> {
        if (txtAddendumName.getText().isEmpty() || txtAddendumDescription.getText().isEmpty()) {
            btnCreateNewAddendum.setDisable(true);
        } else {
            btnCreateNewAddendum.setDisable(false);
        }
    };

    ChangeListener<Report> selectedItemListener = (observable, oldValue, newValue) -> {
        if (newValue != null) {
            if (newValue.getIsActive().equals("Inactive")) {
                txtAddendumName.setDisable(false);
                txtAddendumDescription.setDisable(false);
            } else {
                txtAddendumName.setDisable(true);
                txtAddendumDescription.setDisable(true);
            }

            if (!txtAddendumName.getText().isEmpty() && !txtAddendumDescription.getText().isEmpty()) {
                btnCreateNewAddendum.setDisable(false);
            } else {
                btnCreateNewAddendum.setDisable(true);
            }
        } else {
            txtAddendumName.setDisable(true);
            txtAddendumDescription.setDisable(true);
            btnCreateNewAddendum.setDisable(true);
        }
    };

    ChangeListener<String> textFieldListener = (observable, oldValue, newValue) -> {
        if (tblViewExistingReports.getSelectionModel().getSelectedItem() != null) {
            if (!txtAddendumName.getText().isEmpty() && !txtAddendumDescription.getText().isEmpty()) {
                btnCreateNewAddendum.setDisable(false);
            } else {
                btnCreateNewAddendum.setDisable(true);
            }
        }
    };

    private void disableAddendum() {
        txtAddendumName.setDisable(true);
        txtAddendumDescription.setDisable(true);
        btnCreateNewAddendum.setDisable(true);
    }

    private void updateTableView() {
        try {
            reports = model.getReports(currentCase.getCaseID());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get reports from Database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        observableReports = FXCollections.observableArrayList();
        observableReports.addAll(reports);
        colCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        colReportName.setCellValueFactory(new PropertyValueFactory<>("reportName"));
        colTechnician.setCellValueFactory(new PropertyValueFactory<>("assignedTechnician"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        tblViewExistingReports.getColumns().addAll();
        tblViewExistingReports.setItems(observableReports);

    }

    public void handleCreateNewReport(ActionEvent actionEvent) {
        String reportName = txtReportName.getText();
        String reportDescription = txtReportDescription.getText();
        int caseID = currentCase.getCaseID();
        try {
            model.createNewReport(reportName, reportDescription, caseID, controllerAssistant.getLoggedInUser().getUserID()); //TODO UserID might not be right here, we need to fix this.
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create a new report", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateTableView();
    }

    public void handleCreateNewAddendum(ActionEvent actionEvent) {
        String addendumName = txtAddendumName.getText();
        String addendumDescription = txtAddendumDescription.getText();

        //TODO Finish this once Addendums are created in the DB
    }
}

