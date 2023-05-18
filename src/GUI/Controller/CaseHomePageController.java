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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CaseHomePageController implements Initializable {
    @FXML
    private ImageView imgBack, imgForward;
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
        imgForward.setImage(util.loadImages(forward));
        imgForward.setDisable(true);
        imgForward.setOnMouseClicked(event -> goForward());
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

    private void goForward() {
        try {
            controllerAssistant.loadCenter("ReportHomePageView.fxml");
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

    public void handleCreateNewReportPopUp(ActionEvent actionEvent) {
        PopUpCreateNewReportController popUpCreateNewReportController = new PopUpCreateNewReportController();
        popUpCreateNewReportController.setCurrentCase(currentCase);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(popUpCreateNewReportController);
        loader.setLocation(getClass().getResource("/GUI/View/PopUpCreateNewOrUpdateReport.fxml"));
        stage.setTitle("Create a new report");
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open page for creating a new report", ButtonType.CANCEL);
            alert.showAndWait();
        }
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

    public void handleUpdateReport(ActionEvent actionEvent) {
        PopUpCreateNewReportController popUpCreateNewReportController = new PopUpCreateNewReportController();
        popUpCreateNewReportController.setCurrentCase(currentCase);
        popUpCreateNewReportController.setCurrentReport((Report) tblViewExistingReports.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(popUpCreateNewReportController);
        loader.setLocation(getClass().getResource("/GUI/View/PopUpCreateNewOrUpdateReport.fxml"));
        stage.setTitle("Update report");
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open page for updating report", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateTableView();

    }

    public void handleDeleteReport(ActionEvent actionEvent) {
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


