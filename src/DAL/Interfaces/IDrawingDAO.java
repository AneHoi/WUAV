package DAL.Interfaces;

import BE.CabelAndColor;
import BE.DrawingIcon;

import java.sql.SQLException;
import java.util.List;

public interface IDrawingDAO {

    void addDrawingIcon(byte[] dataImage, String text) throws SQLException;
    List<DrawingIcon> getAllIcons() throws SQLException;
    List<CabelAndColor> getAllCables() throws SQLException;
}
