package GUI.Controller;

import BE.Case;
import BE.Customer;
import BE.Report;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class CaseHomePageController implements Initializable {
    @FXML
    private ImageView imgBack;
    @FXML
    private Label lblCaseName;
    @FXML
    private TextField txtSearchField;
    @FXML
    private Button btnCreateNewReport, btnEditReport, btnDeleteReport;
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
    private String back = "data/Images/Backward.png";
    private String forward = "data/Images/Forward.png";
    private Util util = new Util();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        imgBack.setImage(util.loadImages(back));
        imgBack.setOnMouseClicked(event -> goBack());
        currentCase = model.getCurrentCase();
        currentCustomer = model.getCurrentCustomer();
        updateTableView();
        disableEditAndDelete();
        lblCaseName.setText("Case Name: " + currentCase.getCaseName());
        addListeners();
        util.addShadow(txtSearchField, btnCreateNewReport);
        searchBarFilter();

    }
    private void goBack() {
        try {
            controllerAssistant.loadCenter("CustomerHomePageView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not go back", ButtonType.OK);
            alert.showAndWait();
        }
    }
    private void addListeners() {
        tblViewExistingReports.getSelectionModel().selectedItemProperty().addListener(selectedItemListener);

        tblViewExistingReports.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblViewExistingReports.getSelectionModel().getSelectedItem() != null) {
                Report selectedItem = (Report) tblViewExistingReports.getSelectionModel().getSelectedItem();
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

            }
        });

    }

    ChangeListener<Report> selectedItemListener = (observable, oldValue, newValue) -> {
        if (newValue != null && newValue.getIsActive().equals("Open")) {
            btnEditReport.setDisable(false);
            btnEditReport.setOpacity(1);
            btnDeleteReport.setDisable(false);
            btnDeleteReport.setOpacity(1);
            util.addShadow(btnEditReport, btnDeleteReport);
        } else {
            btnEditReport.setDisable(true);
            btnEditReport.setOpacity(0);
            btnDeleteReport.setDisable(true);
            btnDeleteReport.setOpacity(0);
            util.removeShadow(btnEditReport, btnDeleteReport);
        }
    };


    private void disableEditAndDelete() {
        btnDeleteReport.setDisable(true);
        btnDeleteReport.setOpacity(0);
        btnEditReport.setDisable(true);
        btnEditReport.setOpacity(0);
        util.removeShadow(btnDeleteReport, btnEditReport);
    }

    private void updateTableView() {
        observableReports = FXCollections.observableArrayList();
        try {
            reports = model.getReports(currentCase.getCaseID());
            //Sort the reports
            Comparator<Report> byStatus = (Report report1, Report report2) -> report1.getIsActive().compareTo(report2.getIsActive());
            Collections.sort(reports, byStatus);

            //Sorting the list by active cases, and does not add the "Submitted for review" status for the technicians
            if (controllerAssistant.getLoggedInUser().getUserType() == 3){
                Comparator<Report> byOpenClosed = (Report report1, Report report2) -> report1.getIsActive().compareTo(report2.getIsActive());
                Collections.sort(reports, Collections.reverseOrder(byOpenClosed));
                for (Report report : reports) {
                    if (!report.getIsActive().equalsIgnoreCase("submitted for review")) {
                        observableReports.add(report);
                    }
                }
            }
            //Sorting by "Submitted for review", "open" and "closed" if a project manager is logged in
            else if (controllerAssistant.getLoggedInUser().getUserType() == 2) {
                //Sort the reports
                Comparator<Report> bySubOpenClosed = (Report report1, Report report2) -> {
                    if (report1.getIsActive().equalsIgnoreCase("submitted for review") && report2.getIsActive().equalsIgnoreCase("open")
                            || report1.getIsActive().equalsIgnoreCase("submitted for review") && report2.getIsActive().equalsIgnoreCase("closed")) {
                        //"Submit for review" is 'Highest'
                        return 1;
                    } else if (report1.getIsActive().equalsIgnoreCase("open") && report2.getIsActive().equalsIgnoreCase("closed")) {
                        //"Open" is 'higher' than "Closed"
                        return 1;
                    } else if (report1.getIsActive().equalsIgnoreCase("closed") && report2.getIsActive().equalsIgnoreCase("submitted for review")
                            || report1.getIsActive().equalsIgnoreCase("closed") && report2.getIsActive().equalsIgnoreCase("open")
                            || report1.getIsActive().equalsIgnoreCase("open") && report2.getIsActive().equalsIgnoreCase("submitted for review")) {
                        //"Closed" is the absolute 'lowest'
                        return -1;
                    }else {
                        return 0;
                    }
                };
                Collections.sort(reports, Collections.reverseOrder(bySubOpenClosed));
                for (Report report : reports) {
                    observableReports.add(report);
                }
            } else {
                for (Report report : reports) {
                    observableReports.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get reports from Database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        colCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        colReportName.setCellValueFactory(new PropertyValueFactory<>("reportName"));
        colTechnician.setCellValueFactory(new PropertyValueFactory<>("assignedTechnician"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        tblViewExistingReports.getColumns().addAll();

        tblViewExistingReports.setItems(observableReports);

    }

    public void handleCreateNewReportPopUp() {
        PopUpCreateNewReportController popUpCreateNewReportController = new PopUpCreateNewReportController();
        popUpCreateNewReportController.setCurrentCase(currentCase);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(popUpCreateNewReportController);
        loader.setLocation(getClass().getResource("/GUI/View/PopUpCreateNewOrUpdateReport.fxml"));
        stage.setTitle("Create a new report");
        util.openNewWindow(stage, loader, "Could not open page for creating a new report");
        updateTableView();
    }


    private void searchBarFilter() {
        // Create a list to hold the original unfiltered items in the tblViewCustomers TableView
        ObservableList<Report> originalList = FXCollections.observableArrayList(tblViewExistingReports.getItems());

        // Add a listener to the txtSearchBar TextField to filter the tblViewCustomers TableView based on the user's input
        txtSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Create a filtered list that contains all items from the tblViewCustomers TableView
            FilteredList<Report> filteredList = new FilteredList<>(originalList);

            // Set a predicate to filter the items based on the user's input
            if (newValue == null || newValue.isEmpty()) {
                // If the user has not entered any input, display all items
                tblViewExistingReports.setItems(originalList);
            } else {
                // Otherwise, filter the items based on the user's input
                String lowerCaseFilter = newValue.toLowerCase();
                filteredList.setPredicate(reports -> {
                    String reportDate = String.valueOf(reports.getCreatedDate());
                    String assignedTechnician = Objects.toString(reports.getAssignedTechnician(), "");
                    assignedTechnician = assignedTechnician.toLowerCase();
                    return reports.getReportName().toLowerCase().contains(lowerCaseFilter)
                            || assignedTechnician.contains(lowerCaseFilter)
                            || reportDate.contains(lowerCaseFilter);
                });
                tblViewExistingReports.setItems(filteredList);
            }
        });
    }

    public void handleUpdateReport() {
        PopUpCreateNewReportController popUpCreateNewReportController = new PopUpCreateNewReportController();
        popUpCreateNewReportController.setCurrentCase(currentCase);
        popUpCreateNewReportController.setCurrentReport((Report) tblViewExistingReports.getSelectionModel().getSelectedItem());

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(popUpCreateNewReportController);
        loader.setLocation(getClass().getResource("/GUI/View/PopUpCreateNewOrUpdateReport.fxml"));
        stage.setTitle("Update report");
        util.openNewWindow(stage, loader, "Could not open page for updating report");
        updateTableView();
    }

    public void handleDeleteReport() {
        Report selectedReport = (Report) tblViewExistingReports.getSelectionModel().getSelectedItem();
        Alert reallyWannaDelete = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete this report: " + selectedReport.getReportName() + "?", ButtonType.YES, ButtonType.NO);
        reallyWannaDelete.showAndWait();
        if (reallyWannaDelete.getResult() == ButtonType.YES) {
            try {
                model.deleteReport(selectedReport.getReportID());
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete report from database", ButtonType.OK);
                alert.showAndWait();
            }
        }
        updateTableView();
    }
}


