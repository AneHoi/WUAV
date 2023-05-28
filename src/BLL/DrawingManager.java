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

    public ArrayList<CabelAndColor> getAllCables() {
        CabelAndColor hdmi = new CabelAndColor("HDMI", Color.BLUE);
        CabelAndColor ethernet = new CabelAndColor("Ethernet", Color.BROWN);
        CabelAndColor power = new CabelAndColor("Power", Color.SILVER);
        CabelAndColor antenna = new CabelAndColor("Antenna", Color.YELLOW);
        CabelAndColor sound = new CabelAndColor("Sound", Color.LAVENDER);
        return new ArrayList<>(Arrays.asList(hdmi, ethernet, power, antenna, sound));

    }
}
