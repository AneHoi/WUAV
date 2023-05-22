package BLL;

import BE.DrawingIcon;
import DAL.DrawingDAO;

import java.sql.SQLException;
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
}
