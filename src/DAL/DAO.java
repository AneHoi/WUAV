package DAL;

import BE.Customer;
import BE.Section;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private DBConnector db;

    public DAO() {
        db = DBConnector.getInstance();
    }

    public List<Customer> getAllCostumers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Customer;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Customer_Address");
                String mail = rs.getString("Customer_Mail");
                String tlf = rs.getString("Customer_Tlf");
                int cvr = rs.getInt("Customer_CVR");
                String type = rs.getString("Customer_Type");

                Customer customer = new Customer(id, name, address, tlf, mail, cvr, type);
                customers.add(customer);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return customers;
    }

    public void saveCustomer(Customer customer) {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Customer" + "(Customer_Name, Customer_Address, Customer_Mail, Customer_Tlf, Customer_CVR, Customer_Type)" + "VALUES(?,?,?,?,?,?);";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, customer.getCustomerName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPhoneNumber());
            stmt.setString(4, customer.getEmail());
            stmt.setInt(5, customer.getCVR());
            stmt.setString(6, customer.getCustomerType());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createSection(Section section) throws SQLException {
        int id = 0;
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Section(Section_Title, Section_Sketch, Section_Sketch_Comment, Section_Image, Section_Image_Comment, Section_Description, Section_Made_By_Tech, Section_Report_ID, Section_Addendum_ID) VALUES(?,?,?,?,?,?,?,?,?);";

            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, section.getSectionTitle());
            stmt.setBytes(2, section.getSketchBytes());
            stmt.setString(3, section.getSketchComment());
            stmt.setBytes(4, section.getImageBytes());
            stmt.setString(5, section.getImageComment());
            stmt.setString(6, section.getDescription());
            stmt.setString(7, section.getMadeByTechnician());
            stmt.setInt(8, section.getReportID());
            stmt.setInt(9, section.getAddendumID());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not create section " + e);
        }
        return id;
    }

    public List<Section> getAllSections(int currentReportID) throws SQLException {
        List<Section> allSections = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Section LEFT JOIN User_ ON Section.Section_Made_By_Tech = User_.User_ID WHERE Section_Report_ID = " + currentReportID + ";";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("Section_ID");
                String title = rs.getString("Section_Title");
                byte[] sketchBytes = rs.getBytes("Section_Sketch");
                String sketchComment = rs.getString("Section_Sketch_Comment");
                byte[] imageBytes = rs.getBytes("Section_Image");
                String imageComment = rs.getString("Section_Image_Comment");
                String description = rs.getString("Section_Description");
                String madeByTechnician = rs.getString("User_Full_Name");
                int reportID = rs.getInt("Section_Report_ID");
                int addendumID = rs.getInt("Section_Addendum_ID");
                LocalDate createdDate = rs.getDate("Section_Created_Date").toLocalDate();
                LocalTime createdTime = rs.getTime("Section_Created_Time").toLocalTime();


                Section s = new Section(id, title, sketchComment, imageComment, description, madeByTechnician, reportID, addendumID, createdDate, createdTime);

                if (sketchBytes != null) {
                    s.setSketchBytes(sketchBytes);
                    s.setSketchWithByte(sketchBytes);
                }

                if (imageBytes != null) {
                    s.setSketchBytes(imageBytes);
                    s.setImageWithByte(imageBytes);
                }

                allSections.add(s);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return allSections;
    }

    public void updateCurrentSection(Section currentSection) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = """
                    UPDATE Section SET Section_Title = (?), 
                    Section_Sketch = (?), 
                    Section_Sketch_Comment = (?), 
                    Section_Image = (?), 
                    Section_Image_Comment = (?),
                    Section_Description = (?)
                    WHERE Section_ID = """ + currentSection.getSectionID() + ";";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentSection.getSectionTitle());
            stmt.setBytes(2, currentSection.getSketchBytes());
            stmt.setString(3, currentSection.getSketchComment());
            stmt.setBytes(4, currentSection.getImageBytes());
            stmt.setString(5, currentSection.getImageComment());
            stmt.setString(6, currentSection.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void createSectionForReport(Section section) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Section(Section_Title, Section_Sketch, Section_Sketch_Comment, Section_Image, Section_Image_Comment, Section_Description, Section_Made_By_Tech, Section_Report_ID, Section_Created_Date, Section_Created_Time) VALUES(?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, section.getSectionTitle());
            stmt.setBytes(2, section.getSketchBytes());
            stmt.setString(3, section.getSketchComment());
            stmt.setBytes(4, section.getImageBytes());
            stmt.setString(5, section.getImageComment());
            stmt.setString(6, section.getDescription());
            stmt.setInt(7, 1);
            stmt.setInt(8, section.getReportID());
            stmt.setDate(9, Date.valueOf(LocalDate.now()));
            stmt.setTime(10, Time.valueOf(LocalTime.now()));

            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not create section " + e);
        }
    }

    public void createSectionForAddendum(Section section) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Section(Section_Title, Section_Sketch, Section_Sketch_Comment, Section_Image, Section_Image_Comment, Section_Description, Section_Made_By_Tech, Section_Addendum_ID, Section_Created_Date, Section_Created_Time) VALUES(?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, section.getSectionTitle());
            stmt.setBytes(2, section.getSketchBytes());
            stmt.setString(3, section.getSketchComment());
            stmt.setBytes(4, section.getImageBytes());
            stmt.setString(5, section.getImageComment());
            stmt.setString(6, section.getDescription());
            stmt.setInt(7, 1);
            stmt.setInt(8, section.getAddendumID());
            stmt.setDate(9, Date.valueOf(LocalDate.now()));
            stmt.setTime(10, Time.valueOf(LocalTime.now()));

            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not create section " + e);
        }
    }

    public void deleteSection(int sectionID) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "DELETE FROM Section WHERE Section_ID = " + sectionID + ";";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
