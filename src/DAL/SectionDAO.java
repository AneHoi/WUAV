package DAL;

import DAL.Interfaces.ISectionDAO;
import BE.Section;

import java.sql.*;
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
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(sql);

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
}
