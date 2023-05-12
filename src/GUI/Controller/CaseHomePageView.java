package GUI.Controller;

import BE.Case;
import BE.Customer;
import BE.Report;
import BLL.util.PDFGenerator;
import GUI.Model.Model;
import com.itextpdf.text.DocumentException;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CaseHomePageView implements Initializable {

    @FXML
    private ImageView imgBack, imgForward;
    @FXML
    private Label lblCaseName;
    @FXML
    private TextField txtSearchField, txtUpdateReportName;
    @FXML
    private TextArea txtUpdateReportDescription;
    @FXML
    private Button btnCreateNewReport, btnUpdateReport;
    @FXML
    private TableView tblViewExistingReports;
    @FXML
    private TableColumn colReportName, colTechnician, colCreatedDate, colStatus;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private ControllerAssistant controllerAssistant;
    private Model model;

    private Customer currentCustomer;
    private Case currentCase;

    private List<Report> reports;

    private ObservableList observableReports;
    private String back = "data/Images/Backward.png";
    private String forward = "data/Images/Forward.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        imgBack.setImage(loadImages(back));
        imgBack.setOnMouseClicked(event -> goBack());
        imgForward.setImage(loadImages(forward));
        imgForward.setDisable(true);
        imgForward.setOnMouseClicked(event -> goForward());
        currentCase = model.getCurrentCase();
        currentCustomer = model.getCurrentCustomer();
        updateTableView();
        disableUpdateReport();
        btnCreateNewReport.setDisable(true);
        lblCaseName.setText("Case Name: " + currentCase.getCaseName());
        addListeners();
        addShadow(txtSearchField, btnCreateNewReport);
        searchBarFilter();

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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Report is inactive, please make a new Report instead", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });

    }

    ChangeListener<Report> selectedItemListener = (observable, oldValue, newValue) -> {
        if (newValue != null) {
            if (newValue.getIsActive().equals("Active")) {
                txtUpdateReportName.setDisable(false);
                txtUpdateReportName.setText(newValue.getReportName());
                txtUpdateReportDescription.setDisable(false);
                txtUpdateReportDescription.setText(newValue.getReportDescription());
                btnUpdateReport.setDisable(false);
                addShadow(txtUpdateReportName, txtUpdateReportDescription, btnUpdateReport);
            } else {
                txtUpdateReportName.setDisable(true);
                txtUpdateReportDescription.setDisable(true);
                btnUpdateReport.setDisable(true);
                removeShadow(txtUpdateReportName, txtUpdateReportDescription, btnUpdateReport);
            }

        }
    };



    private void disableUpdateReport() {
        txtUpdateReportName.setDisable(true);
        txtUpdateReportDescription.setDisable(true);
        btnUpdateReport.setDisable(true);
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
        loader.setLocation(getClass().getResource("/GUI/View/PopUpCreateNewReport.fxml"));
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


    public void handleCreateNewAddendum(ActionEvent actionEvent)  {
        /**AddAddendumView addAddendumView = new AddAddendumView();
        Report selectedReport = (Report) tblViewExistingReports.getSelectionModel().getSelectedItem();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(addAddendumView);
        loader.setLocation(getClass().getResource("/GUI/View/AddAddendumView.fxml"));
        addAddendumView.setCaseAndReport(currentCase, selectedReport);
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Add Addendum Window", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateTableViewAddendums();*/
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

    public void handleUpdateReport(ActionEvent actionEvent) throws SQLException, DocumentException {
        PDFGenerator pdfGenerator = new PDFGenerator();
        pdfGenerator.generateReport((Report) tblViewExistingReports.getSelectionModel().getSelectedItem(), currentCase,currentCustomer);
    }
}

