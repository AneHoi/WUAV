package GUI.Controller;

import BE.Case;
import GUI.Model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.Struct;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PopUpAgeOfCasesController implements Initializable {


    @FXML
    private Label lblMonthsOld, lblInfo1, lblInfo2;
    @FXML
    private Button btnDeleteCase, btnKeepCase;
    @FXML
    private TableView tblCases;
    @FXML
    private TableColumn clmCaseName, clmCreated, clmClosed;
    private Model model;
    private ObservableList<Case> tooOldCases;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));


    public PopUpAgeOfCasesController() {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        updateTableView();
        addListeners();
        addShadow(btnKeepCase, btnDeleteCase);
        disable(true);
    }
    private void addShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(shadow);
        }
    }

    private void addListeners() {
        tblCases.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && tblCases.getSelectionModel().getSelectedItem() != null) {
                disable(false);
                Case casen = (Case) tblCases.getSelectionModel().getSelectedItem();
                LocalDateTime dateToday = LocalDate.now().atStartOfDay();
                LocalDateTime dateClosed = casen.getDateClosed().atStartOfDay();
                long daysBetween = Duration.between(dateClosed, dateToday).toDays();
                long monthsBetween = Math.round(daysBetween / 30);
                lblMonthsOld.setText(String.valueOf(monthsBetween));
            }
        });
    }

    private void disable(boolean bool) {
        btnDeleteCase.setDisable(bool);
        btnKeepCase.setDisable(bool);
        if (bool) {
            lblMonthsOld.setVisible(false);
            lblMonthsOld.setText("");
            lblInfo1.setVisible(false);
            lblInfo2.setVisible(false);

        }else {
            lblMonthsOld.setVisible(true);
            lblInfo1.setVisible(true);
            lblInfo2.setVisible(true);
        }
    }


    private void updateTableView() {
        clmCaseName.setCellValueFactory(new PropertyValueFactory<>("caseName"));
        clmCreated.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        clmClosed.setCellValueFactory(new PropertyValueFactory<>("dateClosed"));
        tblCases.getColumns().addAll();
        ObservableList<Case> oldCases = FXCollections.observableArrayList();
        List<Case> caseList;
        try {
            caseList = model.getAllCases();
            for (Case caseBE : caseList) {
                if (tooOld(caseBE)) {
                    oldCases.add(caseBE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get Cases from the database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        tblCases.setItems(oldCases);
    }


    /**
     * Checks if the case is older than 4 years.
     * @param casen is the case to be checked
     * @return boolean
     */
    private boolean tooOld(Case casen) {
        LocalDateTime dateToday = LocalDate.now().atStartOfDay();
        if (casen.getDateClosed() != null) {
            LocalDateTime dateClosed = casen.getDateClosed().atStartOfDay();
            long daysBetween = Duration.between(dateClosed, dateToday).toDays();
            if(daysBetween > casen.getDaysToKeep()){
                return true;
            }
        }
        return false;
    }

    public void keepCaseLonger(ActionEvent event) {
        Case casen = (Case) tblCases.getSelectionModel().getSelectedItem();
        Alert alertCaseForDeleting = new Alert(Alert.AlertType.CONFIRMATION);
        alertCaseForDeleting.setTitle("Expanding the time for keeping a case");
        alertCaseForDeleting.setHeaderText("How much time would you like to expand the time of keeping this case:\n" + casen.getCaseName());

        ButtonType oneMonth = new ButtonType("1 month");
        ButtonType halfAYear = new ButtonType("0.5 year");
        ButtonType aYear = new ButtonType("1 year");
        ButtonType fourYear = new ButtonType("4 year");

        alertCaseForDeleting.getButtonTypes().clear();
        alertCaseForDeleting.getButtonTypes().addAll(oneMonth, halfAYear, aYear, fourYear);

        Optional<ButtonType> option = alertCaseForDeleting.showAndWait();
        if (option.get() == oneMonth) {
            expandKeepingTime(casen, 30);
        } else if (option.get() == halfAYear) {
            expandKeepingTime(casen, 183);
        } else if (option.get() == aYear){
            expandKeepingTime(casen, 365);
        } else if (option.get() == fourYear) {
            expandKeepingTime(casen, 1460);
        } else {}
        updateTableView();
        disable(true);
    }

    private void expandKeepingTime(Case casen, int daysToFutherKeep) {
        int daysToKeep = casen.getDaysToKeep();
        daysToKeep = daysToKeep + daysToFutherKeep;
        try {
            model.expandKeepingTime(casen, daysToKeep);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not expand the time for keeping the case in the program", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }

    public void deleteCase(ActionEvent event) {
        Case casen = (Case) tblCases.getSelectionModel().getSelectedItem();
        LocalDateTime dateToday = LocalDate.now().atStartOfDay();
        LocalDateTime dateClosed = casen.getDateClosed().atStartOfDay();
        long daysBetween = Duration.between(dateClosed, dateToday).toDays();
        long monthsBetween = Math.round(daysBetween / 30);
        wantToDeleteCase(casen, monthsBetween);
    }

    private void wantToDeleteCase(Case casen, long monthsBetween) {
        Alert alertCaseForDeleting = new Alert(Alert.AlertType.CONFIRMATION);
        alertCaseForDeleting.setTitle("Deleting a case");
        alertCaseForDeleting.setHeaderText("Are you sure you want to delete this case:\n" + casen.getCaseName() +
                "\t it is: " + monthsBetween + " month old");

        ButtonType deleteCase = new ButtonType("Delete case");
        ButtonType cancel = new ButtonType("Cancel");

        alertCaseForDeleting.getButtonTypes().clear();
        alertCaseForDeleting.getButtonTypes().addAll(deleteCase, cancel);

        Optional<ButtonType> option = alertCaseForDeleting.showAndWait();
        if (option.get() == deleteCase) {
            try {
                model.deleteCase(casen);
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete the case from the program", ButtonType.CANCEL);
                alert.showAndWait();
            }
            updateTableView();
            disable(true);
        }
    }
}
