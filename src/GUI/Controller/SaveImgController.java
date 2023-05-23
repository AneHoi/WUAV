package GUI.Controller;

import BE.DrawingIcon;
import BE.Report;
import BE.TextsAndImagesOnReport;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class SaveImgController implements Initializable, Serializable {

    @FXML
    private Button btnSave, btnChooseImage;
    @FXML
    private TextField txtAddComment;
    private DrawingIcon drawingIcon;
    private byte[] dataImage;
    @FXML
    private VBox vBox;
    @FXML
    private Label lblImage, lblTitle;
    @FXML
    private ImageView imgView;
    private Image image;
    private Report currentReport;
    private TextsAndImagesOnReport textOrImage;
    private ControllerAssistant controllerAssistant;
    private Model model;
    private int nextPosition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgView.setImage(image);
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
    }


    public Report getCurrentReport() {
        return currentReport;
    }

    public void setCurrentReport(Report currentReport) {
        this.currentReport = currentReport;
    }

    public int getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }

    public void setImgView(Image img){
        this.image = img;
    }

    public void handleSave(ActionEvent event) {

        int position = nextPosition;
        int reportID = currentReport.getReportID();
        String comment = txtAddComment.getText();
        int userID = controllerAssistant.getLoggedInUser().getUserID();
        LocalDate createdDate = LocalDate.now();
        LocalTime createdTime = LocalTime.now();
        try {
            int random = (int)(Math.random()*(100-1+1)+1);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("data/sketches/drawingFile.png"));
            File selectedFile = new File("data/sketches/drawingFile.png");
            if (selectedFile != null) {
                dataImage = Files.readAllBytes(selectedFile.getAbsoluteFile().toPath());
            }
            model.SaveImageToReport(position, reportID, dataImage, comment, userID, createdDate, createdTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //This closes the window
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
