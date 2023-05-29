package GUI.Controller;

import BE.Case;
import GUI.Controller.Util.Util;
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
import java.time.temporal.ChronoUnit;
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
    private Util util = new Util();
    public PopUpAgeOfCasesController() {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        updateTableView();
        addListeners();
        util.addShadow(btnKeepCase, btnDeleteCase);
        disable(true);
    }

    /**
     * Adds a mouse click listener to the tblCases table view.
     * When a single mouse click occurs on a row and an item is selected,
     * it enables certain buttons and updates the label displaying the months old information.
     */
    private void addListeners() {
        tblCases.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && tblCases.getSelectionModel().getSelectedItem() != null) {
                disable(false);
                long monthsOld = chosenCaseAge();
                lblMonthsOld.setText(String.valueOf(monthsOld));
            }
        });
    }

    /**
     * Calculates and returns the age of the selected case in months.
     * The method retrieves the selected case from the tblCases table view.
     * It calculates the number of days between the closed date of the case and today's date.
     * Then, it calculates the number of months by dividing the days by 30 and rounding to the nearest whole number.
     * The calculated number of months is returned.
     */
    private long chosenCaseAge() {
        Case selectedCase = (Case) tblCases.getSelectionModel().getSelectedItem();
        LocalDateTime dateToday = LocalDate.now().atStartOfDay();
        LocalDateTime dateClosed = selectedCase.getDateClosed().atStartOfDay();
        long daysBetween = Duration.between(dateClosed, dateToday).toDays();
        long monthsBetween = Math.round(daysBetween / 30);
        return monthsBetween;
    }

    /**
     * Disables or enables certain UI elements based on the specified boolean value.
     * If the boolean value is true, the btnDeleteCase and btnKeepCase buttons are disabled,
     * and the shadow effect is removed from them. The lblMonthsOld, lblInfo1, and lblInfo2 labels
     * are also hidden.
     * If the boolean value is false, the btnDeleteCase and btnKeepCase buttons are enabled,
     * and a shadow effect is added to them. The lblMonthsOld, lblInfo1, and lblInfo2 labels
     * are made visible.
     */
    private void disable(boolean bool) {
        btnDeleteCase.setDisable(bool);
        btnKeepCase.setDisable(bool);
        if (bool) {
            util.removeShadow(btnDeleteCase,btnKeepCase);
            lblMonthsOld.setVisible(false);
            lblMonthsOld.setText("");
            lblInfo1.setVisible(false);
            lblInfo2.setVisible(false);

        } else {
            util.addShadow(btnKeepCase,btnDeleteCase);
            lblMonthsOld.setVisible(true);
            lblInfo1.setVisible(true);
            lblInfo2.setVisible(true);
        }
    }

    /**
     * Updates the table view with the list of cases.
     * It sets up the cell value factories for the table columns.
     * The table columns are added to the table view.
     * It retrieves the list of cases from the model.
     * For each case, if it is considered too old according to the util.tooOld() method, it is added to the oldCases list.
     * The oldCases list is set as the items of the table view.
     */
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
                if (util.tooOld(caseBE)) {
                    oldCases.add(caseBE);
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
        tblCases.setItems(oldCases);
    }

    /**
     * Allows the user to extend the keeping time for the selected case by choosing a predefined duration.
     * It retrieves the selected case from the table view.
     * A confirmation alert is displayed to ask the user how much time to extend the case keeping.
     * The user can choose from options like 1 month, 0.5 year, 1 year, 4 years, or cancel.
     * The expansion of keeping time is performed by calling the expandKeepingTime() method with the selected case and the chosen duration.
     * After the extension, the table view is updated and the application is disabled.
     */
    public void keepCaseLonger() {
        Case selectedCase = (Case) tblCases.getSelectionModel().getSelectedItem();
        Alert alertCaseForDeleting = new Alert(Alert.AlertType.CONFIRMATION);
        alertCaseForDeleting.setTitle("Expanding the time for keeping a case");
        alertCaseForDeleting.setHeaderText("How much time would you like to expand the time of keeping this case:\n" + selectedCase.getCaseName());

        ButtonType oneMonth = new ButtonType("1 month");
        ButtonType halfAYear = new ButtonType("0.5 year");
        ButtonType aYear = new ButtonType("1 year");
        ButtonType fourYear = new ButtonType("4 year");
        ButtonType cancel = new ButtonType("Cancel");

        alertCaseForDeleting.getButtonTypes().clear();
        alertCaseForDeleting.getButtonTypes().addAll(oneMonth, halfAYear, aYear, fourYear, cancel);

        Optional<ButtonType> option = alertCaseForDeleting.showAndWait();
        if (option.get() == oneMonth) {
            expandKeepingTime(selectedCase, 30);
        } else if (option.get() == halfAYear) {
            expandKeepingTime(selectedCase, 183);
        } else if (option.get() == aYear) {
            expandKeepingTime(selectedCase, 365);
        } else if (option.get() == fourYear) {
            expandKeepingTime(selectedCase, 1460);
        } else if (option.get() == cancel) {
            expandKeepingTime(selectedCase, 0);
        }
        updateTableView();
        disable(true);
    }

    /**
     * Expands the keeping time for the selected case by adding the specified number of days.
     * It calculates the number of days between the case's closed date and the current date.
     * The calculated days are incremented by the additional days to keep.
     * The expansion of keeping time is performed by calling the model's expandKeepingTime() method.
     */
    private void expandKeepingTime(Case selectedCase, int daysToFurtherKeep) {
        LocalDate closedDate = selectedCase.getDateClosed();
        LocalDate today = LocalDate.now();
        long daysBetweenClosedAndNow = ChronoUnit.DAYS.between(closedDate,today);
        int daysToKeep = Math.toIntExact(daysBetweenClosedAndNow);
        daysToKeep = daysToKeep + daysToFurtherKeep;
        try {
            model.expandKeepingTime(selectedCase, daysToKeep);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not expand the time for keeping the case in the program", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
    }

    /**
     * Deletes the selected case after confirming with the user.
     * It determines the number of months the case has been open using the chosenCaseAge() method.
     * The case to be deleted is obtained from the selected item in the table view.
     * The deletion is initiated by calling the wantToDeleteCase() method.
     */
    public void deleteCase() {
        long monthsOld = chosenCaseAge();
        Case casen = (Case) tblCases.getSelectionModel().getSelectedItem();
        wantToDeleteCase(casen, monthsOld);
    }

    /**
     * Displays a confirmation dialog to prompt the user for case deletion.
     * The dialog includes the case name and the number of months since its creation.
     * If the user confirms the deletion, the case is deleted using the model.
     * After the deletion, the table view is updated, and certain controls are disabled.
     */
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
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
                dialogPane.getStyleClass().add("dialog");
                alert.showAndWait();
            }
            updateTableView();
            disable(true);
        }
    }
}
