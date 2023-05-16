package GUI.Controller;

import BE.Case;
import GUI.Model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PopUpAgeOfCasesController implements Initializable {


    @FXML
    private TableView tblCases;
    @FXML
    private TableColumn clmCaseName, clmCreated;
    private Model model;
    private ObservableList<Case> tooOldCases;

    public PopUpAgeOfCasesController() {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        updateTableView();
        addListeners();

    }

    private void addListeners() {
        tblCases.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && tblCases.getSelectionModel().getSelectedItem() != null) {
                Case casen = (Case) tblCases.getSelectionModel().getSelectedItem();
                LocalDateTime dateToday = LocalDate.now().atStartOfDay();
                LocalDateTime dateCreated = casen.getCreatedDate().atStartOfDay();
                long daysBetween = Duration.between(dateCreated, dateToday).toDays();
                long yearsBetween = Math.round(daysBetween / 365);
                WantToDeleteCase(casen, yearsBetween);
            }
        });
    }

    private void WantToDeleteCase(Case casen, long yearsBetween) {

        Alert alertCaseForDeleting = new Alert(Alert.AlertType.CONFIRMATION);
        alertCaseForDeleting.setTitle("Deleting a case");
        alertCaseForDeleting.setHeaderText("Are you sure you want to delete this case:\n" + casen.getCaseName() +
                "\t it is: " + yearsBetween + " years old");

        ButtonType deleteCase = new ButtonType("Delete case");
        ButtonType cancel = new ButtonType("Cancel");

        alertCaseForDeleting.getButtonTypes().clear();
        alertCaseForDeleting.getButtonTypes().addAll(deleteCase, cancel);

        Optional<ButtonType> option = alertCaseForDeleting.showAndWait();
        if (option.get() == deleteCase) {
            try {
                model.deleteCase(casen);
                tooOldCases.remove(casen);
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete the case from the program", ButtonType.CANCEL);
                alert.showAndWait();
            }
            updateTableView();
        }
    }


    private void updateTableView() {
        clmCaseName.setCellValueFactory(new PropertyValueFactory<>("caseName"));
        clmCreated.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        tblCases.getColumns().addAll();
        tblCases.setItems(tooOldCases);
    }

    public void setTooOldCases(ObservableList<Case> tooOldCases) {
        this.tooOldCases = tooOldCases;
    }

}
