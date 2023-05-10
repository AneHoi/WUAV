package GUI.Controller;

import BE.Case;
import BE.Technician;
import GUI.Model.Model;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerHomePageView implements Initializable {


    @FXML
    private TableColumn colCaseName, colCaseID, colCaseDescription, colCreatedDate, colTechAssigned;
    @FXML
    private TableView tblViewExistingCases, tblViewTechAssigned;
    @FXML
    private ImageView imgSearch, imgBack, imgForward;
    @FXML
    private Button btnCreateNewCase, btnManageTech, btnUpdateCase;
    @FXML
    private TextField txtSearchBar;
    @FXML
    private Label lblCustomerName;
    private ControllerAssistant controllerAssistant;
    private Model model;
    private final DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private ObservableList<Case> caseObservableList;
    private ObservableList<Technician> technicianObservableList;

    private final String search = "data/Images/search.png";
    private final String back = "data/Images/Backward.png";
    private final String forward = "data/Images/Forward.png";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        caseObservableList = FXCollections.observableArrayList();
        technicianObservableList = FXCollections.observableArrayList();
        lblCustomerName.setText(model.getCurrentCustomer().getCustomerName() + " Home Page");
        imgSearch.setImage(loadImages(search));
        imgBack.setImage(loadImages(back));
        imgBack.setOnMouseClicked(event -> goBack());
        imgForward.setImage(loadImages(forward));
        imgForward.setDisable(true);
        imgForward.setOnMouseClicked(event -> goForward());
        addListeners();
        addShadow(btnCreateNewCase);
        updateTableView();
        searchBarFilter();
        btnManageTech.setDisable(true);
        btnUpdateCase.setDisable(true);
        tblViewTechAssigned.setDisable(true);

    }

    private void goBack() {
        try {
            controllerAssistant.loadCenter("CustomerView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not go back", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void goForward() {
        try {
            controllerAssistant.loadCenter("CaseHomePageView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not go back", ButtonType.OK);
            alert.showAndWait();
        }
    }


    private void updateTableView() {
        colCaseID.setCellValueFactory(new PropertyValueFactory<>("caseID"));
        colCaseName.setCellValueFactory(new PropertyValueFactory<>("caseName"));
        colCaseDescription.setCellValueFactory(new PropertyValueFactory<>("caseDescription"));
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
        tblViewExistingCases.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ObservableList techniciansAssigned = FXCollections.observableArrayList();
                btnManageTech.setDisable(false);
                btnUpdateCase.setDisable(false);
                addShadow(btnManageTech,btnUpdateCase);
                techniciansAssigned.add(newSelection);
                tblViewTechAssigned.setDisable(false);
                colTechAssigned.setCellValueFactory(new PropertyValueFactory<>("assignedTechnician"));
                tblViewTechAssigned.setItems(techniciansAssigned);
            } else {
                tblViewTechAssigned.setDisable(true);
                tblViewTechAssigned.getItems().clear();
                btnManageTech.setDisable(true);
                removeShadow(btnManageTech);
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
        CreateOrUpdateCaseView createOrUpdateCaseView = new CreateOrUpdateCaseView();
        Case selectedCase = (Case) tblViewExistingCases.getSelectionModel().getSelectedItem();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(createOrUpdateCaseView);
        loader.setLocation(getClass().getResource("/GUI/View/CreateOrEditCaseView.fxml"));
        createOrUpdateCaseView.setOnlyCustomer(model.getCurrentCustomer());
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Case Window", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateTableView();
    }


    public void handleUpdateCase(ActionEvent actionEvent) {
        CreateOrUpdateCaseView createOrUpdateCaseView = new CreateOrUpdateCaseView();
        Case selectedCase = (Case) tblViewExistingCases.getSelectionModel().getSelectedItem();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(createOrUpdateCaseView);
        loader.setLocation(getClass().getResource("/GUI/View/CreateOrEditCaseView.fxml"));
        createOrUpdateCaseView.setCustomerAndCase(selectedCase, model.getCurrentCustomer());
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Case Window", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateTableView();
    }

    public void handleManageTech(ActionEvent actionEvent) {
    }
}
