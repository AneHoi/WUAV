package GUI.Controller;

import BE.Case;
import BE.Customer;
import BE.Report;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportHomePageView implements Initializable {
    public Button btnAddSection;
    @FXML
    private Label lblCustomerName, lblReportName, lblCustomerAddress, lblCustomerEmail, lblCustomerTelephone, lblCaseName, lblCaseID, lblCaseCreated, lblCaseTechnicians, lblCaseContactPerson, lblReportDescription;
    @FXML
    private VBox vboxSectionAdding;
    private Customer currentCustomer;
    private Case currentCase;
    private Report currentReport;
    private Model model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        currentReport = model.getCurrentReport();
        currentCase = model.getCurrentCase();
        currentCustomer = model.getCurrentCustomer();
        updateReportInfo();
    }

    private void updateReportInfo() {
        lblCustomerName.setText(currentCustomer.getCustomerName());
        lblReportName.setText(currentReport.getReportName());
        lblCustomerAddress.setText(currentCustomer.getAddress());
        lblCustomerEmail.setText(currentCustomer.getEmail());
        lblCustomerTelephone.setText(currentCustomer.getPhoneNumber());
        lblCaseName.setText(currentCase.getCaseName());
        lblCaseID.setText(String.valueOf(currentCase.getCaseID()));
        lblCaseCreated.setText(String.valueOf(currentCase.getCreatedDate()));
        lblCaseTechnicians.setText(currentCase.getAssignedTechnician());
        lblCaseContactPerson.setText(currentCase.getContactPerson());
        lblReportDescription.setText(currentReport.getReportDescription());

    }

    public void handleOpenSectionView(ActionEvent actionEvent) {
        AddSectionView sectionView = new AddSectionView();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(sectionView);
        loader.setLocation(getClass().getResource("/GUI/View/AddSectionView.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Add Section Window", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }
}
