package GUI.Controller;

import BE.Case;
import BE.Technician;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageTechniciansView implements Initializable {

    @FXML
    private VBox vboxAllTechnicians, vboxComboBoxes;
    @FXML
    Button btnConfirmChoices;
    private List<Technician> alreadyAssignedTechs, chosenTechnicians;
    private Model model;
    private Case selectedCase;

    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chosenTechnicians = new ArrayList<>();
        model = Model.getInstance();
        btnConfirmChoices.getStyleClass().add("orangeButtons");
        addShadow(btnConfirmChoices);
        updateTechnicians();
    }

    private void updateTechnicians() {
        List<Technician> technicians = new ArrayList<>();
        try {
            technicians.addAll(model.getAllTechnicians());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get Technicians from Database", ButtonType.OK);
            alert.showAndWait();
        }
        for (Technician t : technicians) {
            Label technicianName = new Label(t.getFullName());
            technicianName.setStyle("-fx-font-size: 24");
            CheckBox cbChosenTech = new CheckBox();
            if (alreadyAssignedTechs.contains(t)) {
                cbChosenTech.setSelected(true);
            }
            cbChosenTech.setStyle("-fx-font-size: 24");
            vboxAllTechnicians.getChildren().add(technicianName);
            vboxComboBoxes.getChildren().add(cbChosenTech);
            addShadow(cbChosenTech);
            // Add a listener to the checkbox
            cbChosenTech.setOnAction(event -> {
                if (cbChosenTech.isSelected()) {
                    chosenTechnicians.add(t);
                } else {
                    chosenTechnicians.remove(t);
                }
            });
        }
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

    public void setSelectedCase(Case selectedCase, List<Technician> alreadyAssignedTechs) {
        this.selectedCase = selectedCase;
        this.alreadyAssignedTechs = alreadyAssignedTechs;
    }

    public void handleConfirmChoices(ActionEvent actionEvent) {
        int caseID = selectedCase.getCaseID();
        try {
            model.addTechnicianToCase(caseID, chosenTechnicians);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not assign Technicians to Case", ButtonType.OK);
            alert.showAndWait();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Updated case successfully", ButtonType.OK);
        alert.showAndWait();
        Stage stage = (Stage) btnConfirmChoices.getScene().getWindow();
        stage.close();
    }

}
