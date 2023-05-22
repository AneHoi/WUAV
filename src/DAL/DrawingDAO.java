package DAL;

import BE.DrawingIcon;
import DAL.Interfaces.IDrawingDAO;
import javafx.fxml.FXML;

import java.sql.*;
import java.util.ArrayList;
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
}
