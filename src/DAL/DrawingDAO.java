package DAL;

import BE.CabelAndColor;
import BE.DrawingIcon;
import DAL.Interfaces.IDrawingDAO;
import javafx.scene.paint.Color;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DrawingDAO implements IDrawingDAO {
    private DBConnector db;

    public DrawingDAO() {
        db = DBConnector.getInstance();
    }

    @Override
    public void addDrawingIcon(byte[] dataImage, String text) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO DrawingTable(ImageIcon, IconDescription) VALUES (?, ?);";
            PreparedStatement ps1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps1.setBytes(1, dataImage);
            ps1.setString(2, text);
            ps1.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    @Override
    public List<DrawingIcon> getAllIcons() throws SQLException {
        List<DrawingIcon> allIcons = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM DrawingTable;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                byte[] image = rs.getBytes("ImageIcon");
                String description = rs.getString("IconDescription");
                DrawingIcon drawingIcon = new DrawingIcon(image, description);
                drawingIcon.setImageWithBytes(drawingIcon.getImageData());
                allIcons.add(drawingIcon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
        return allIcons;
    }

    public List<CabelAndColor> getAllCables() {
        List<CabelAndColor> cabelAndColors = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM CableAndColor;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String cableName = rs.getString("Cable_Name");
                String colorName = rs.getString("Color_Name");
                CabelAndColor cabelAndColor = new CabelAndColor(cableName, colorName);
                cabelAndColors.add(cabelAndColor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cabelAndColors;
    }
}
