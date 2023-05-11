package DAL;

import DAL.Interfaces.ISectionDAO;
import BE.Section;
import com.itextpdf.text.Image;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SectionDAO implements ISectionDAO {
    private DBConnector db;
    public SectionDAO(){db = DBConnector.getInstance();}

    @Override
    public List getSections(int reportID) throws SQLException {
        List<Section> sections = new ArrayList<>();
        try (Connection conn = db.getConnection()){
            String sql =  "SELECT * FROM Section JOIN User_ ON Section.Section_Made_By_Tech = User_.User_ID WHERE Section_Report_ID = " + reportID + ";";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int sectionID = rs.getInt("Section_ID");
                String sectionTitle = rs.getString("Section_Title");
                String sketchComment = rs.getString("Section_Sketch_Comment");
                String imageComment = rs.getString("Section_Image_Comment");
                String description = rs.getString("Section_Description");
                String techName = rs.getString("User_Full_Name");

                Section s = new Section(sectionID, sectionTitle, sketchComment, imageComment, description, techName, reportID);
                sections.add(s);

            }
        }
        return sections;
    }

    @Override
    public void createNewSection(String sectionTitle, byte[] sketch, String sketchComment, byte[] image, String imageComment, String description, int madeByTech, int reportID) throws SQLException {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Report(Section_Title, Section_Sketch, Section_Sketch_Comment, Section_Image, Section_Image_Comment, Section_Description, Section_Made_By_Tech, Section_Report_ID, Section_Created_Date, Section_Created_Time) VALUES(?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, sectionTitle);
            stmt.setBytes(2, sketch);
            stmt.setString(3,sketchComment);
            stmt.setBytes(4, image);
            stmt.setString(5,imageComment);
            stmt.setString(6,description);
            stmt.setInt(7,madeByTech);
            stmt.setInt(8,reportID);
            stmt.setDate(9, Date.valueOf(date));
            stmt.setTime(10,Time.valueOf(time));

            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not create section");
        }
    }

}
