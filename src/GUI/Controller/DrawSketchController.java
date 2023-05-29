package GUI.Controller;

import BE.CabelAndColor;
import BE.DrawingIcon;
import BE.Report;
import GUI.Controller.Util.DraggableMaker;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DrawSketchController implements Initializable {
    @FXML
    private HBox options;
    private CheckBox checkEraser;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox vboxCables, vboxIcons;
    @FXML
    private ScrollPane scrollPaneIcons;
    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane iconPane;
    @FXML
    private Canvas canvasForCables;
    private Util util = new Util();
    private Model model;
    private Report currentReport;
    private int nextPosition;
    private ContextMenu contextMenu = new ContextMenu();
    private DraggableMaker draggableMaker = new DraggableMaker();
    private List<DrawingIcon> imageIcons = new ArrayList<>();
    private ArrayList<CabelAndColor> cableAndColors = new ArrayList<>();
    private Color color;
    private Node chosenNode;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new Model();
        iconPane.setStyle("-fx-border-color: rgba(23,38,58,1); -fx-border-width: 10;");
        setIconPaneInFront(true);
        //Makes sure the Icons are dragged correctly on the iconPane
        draggableMaker.setStartVal(scrollPaneIcons.getPrefWidth());
        createErasor();
        createContextMenu();
        getAllIconsAndCables();
        createAllCables();
        addAllIconsAndNames();
        drawingListener();
    }

    private void createErasor() {
        checkEraser = new CheckBox("Eraser");
        checkEraser.setStyle("-fx-font-size: 24");
        checkEraser.setVisible(false);
        checkEraser.setOnAction(e -> {
            if(checkEraser.isSelected()){
                setIconPaneInFront(false);
            }
        });

        options.getChildren().add(checkEraser);
    }

    /**
     * Setting the icon pane in front or not
     * @param bool determines if iconPane should be in front
     */
    private void setIconPaneInFront(boolean bool) {
        stackPane.getChildren().clear();
        if (bool)
            stackPane.getChildren().addAll(canvasForCables, iconPane);
        else
            stackPane.getChildren().addAll(iconPane, canvasForCables);
    }

    /**
     * Create the popup menu when right-clicking on items
     */
    private void createContextMenu() {
        //Creating a context menu
        //Creating the menu Items for the context menu
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> iconPane.getChildren().remove(chosenNode));
        contextMenu.getItems().addAll(deleteItem);
    }


    /**
     * Gets all the DrawingIcons form the database and all the cables
     */
    private void getAllIconsAndCables() {
        try {
            vboxIcons.getChildren().clear();
            imageIcons.clear();
            imageIcons = model.getAllDrawingIcons();
            vboxCables.getChildren().clear();
            cableAndColors.clear();
            cableAndColors = model.getAllCables();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get the icons", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }
    private void drawingListener() {
        GraphicsContext g = canvasForCables.getGraphicsContext2D();
        canvasForCables.setOnMouseDragged(e -> {
            double size = Double.parseDouble(String.valueOf(10));
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            if(checkEraser.isSelected()){
                g.clearRect(x,y,size*2,size*2);
            }
            else if (color != null) {
                g.setFill(color);
                g.fillRect(x, y, size, size);
            }
        });
    }

    private void createAllCables() {
        for (CabelAndColor cable: cableAndColors) {
            Button cableBtn = new Button(cable.getCabelName());
            cableBtn.getStyleClass().clear();
            cableBtn.getStyleClass().add("infoLabel");
            cableBtn.setStyle("-fx-border-radius: 15");
            cableBtn.setBackground(Background.fill(cable.getCabelColor()));
            cableBtn.setPrefSize(80, 80);
            cableBtn.setOnMouseClicked(e ->{
                checkEraser.setVisible(true);
                checkEraser.setSelected(false);
                setIconPaneInFront(false);
                color = cable.getCabelColor();
            });
            Label cableName = createLabel(cable.getCabelName());
            vboxCables.getChildren().add(cableBtn);
            vboxCables.getChildren().add(cableName);
        }
    }

    private Label createLabel(String name) {
        Label lbl = new Label(name);
        lbl.getStyleClass().add("infoLabel");
        lbl.setStyle("-fx-alignment: center");
        lbl.setPrefWidth(80);
        lbl.setOnMouseClicked(event -> {
            checkEraser.setSelected(false);
            SpawnLabelName(lbl);
            setIconPaneInFront(true);
        });
        return lbl;
    }

    private void addAllIconsAndNames() {
        for (DrawingIcon drawingIcon: imageIcons) {
            Label lblIcon = new Label();
            Label lblIconName = createLabel(drawingIcon.getImageComment());
            //Set graphics for lblIcon
            graphicsForLabelIcon(lblIcon, drawingIcon);

            vboxIcons.getChildren().add(lblIcon);
            vboxIcons.getChildren().add(lblIconName);
        }
    }

    private void graphicsForLabelIcon(Label lblIcon, DrawingIcon drawingIcon) {

        // Creating a graphic (image) on the label
        Image img = drawingIcon.getImage();
        ImageView view = new ImageView(img);
        view.setFitHeight(80);
        view.setPreserveRatio(true);
        lblIcon.setGraphic(view);
        lblIcon.setPrefSize(80,80);

        lblIcon.setOnMouseClicked(event -> {
            checkEraser.setSelected(false);
            spawnNewLbl(lblIcon, img);
            setIconPaneInFront(true);
        });
    }

    private void SpawnLabelName(Label lbl) {
        Label newLabel = new Label();
        newLabel.setPrefSize(lbl.getWidth(), lbl.getHeight());
        newLabel.setText(lbl.getText());
        newLabel.getStyleClass().add("infoLabel");

        newLabel.setLayoutX(100);
        newLabel.setLayoutY(100);
        draggableMaker.makeDraggable(newLabel);
        newLabel.setContextMenu(contextMenu);
        newLabel.setOnMouseClicked(e -> chosenNode = newLabel);
        iconPane.getChildren().add(newLabel);
    }

    private void spawnNewLbl(Label lbl, Image graphic) {
        Label newLabel = new Label();
        newLabel.setPrefSize(lbl.getPrefWidth(), lbl.getPrefHeight());
        newLabel.setText(lbl.getText());

        ImageView view = new ImageView(graphic);
        view.setFitHeight(80);
        view.setPreserveRatio(true);
        newLabel.setGraphic(view);
        newLabel.setLayoutX(100);
        newLabel.setLayoutY(100);
        newLabel.setContextMenu(contextMenu);
        newLabel.setOnMouseClicked(e -> chosenNode = newLabel);
        draggableMaker.makeDraggable(newLabel);
        iconPane.getChildren().add(newLabel);
    }

    public void snapshot() {
        setIconPaneInFront(true);
        WritableImage snapshot = borderPane.getCenter().snapshot(new SnapshotParameters(), null);
        try {
            SaveImgController saveImgController = new SaveImgController();
            saveImgController.setImgView(snapshot);
            saveImgController.setCurrentReport(currentReport);
            saveImgController.setNextPosition(nextPosition);
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GUI/View/SaveImg.fxml"));
            loader.setController(saveImgController);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open the SaveImage page", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }

    public void clear() {
        iconPane.getChildren().clear();
        GraphicsContext gc = canvasForCables.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasForCables.getWidth(), canvasForCables.getHeight());
    }

    /**
     * Opens a new window for adding an icon to the drawingListener program
     */
    public void addIcon(){
        AddIconController addIconController = new AddIconController();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddIconView.fxml"));
        loader.setController(addIconController);
        util.openNewWindow(stage, loader, "Could not open Add Image Window");
        getAllIconsAndCables();
        createAllCables();
        addAllIconsAndNames();
    }

    public void setCurrentReport(Report currentReport) {
        this.currentReport = currentReport;
    }

    public void setNextPosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }
}
