package BLL;

import BE.CabelAndColor;
import BE.DrawingIcon;
import DAL.DrawingDAO;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DrawingManager {

    private DrawingDAO drawingDAO;
    public DrawingManager(){drawingDAO = new DrawingDAO();}
    public void addDrawingIcon(byte[] dataImage, String text) throws SQLException {
        drawingDAO.addDrawingIcon(dataImage, text);
    }

    public List<DrawingIcon> getAllDrawingIcons() throws SQLException {
        return drawingDAO.getAllIcons();
    }

    public List<CabelAndColor> getAllCables() {
        return drawingDAO.getAllCables();
    }
}
