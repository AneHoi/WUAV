package DAL;

import BE.Case;
import BE.Technician;
import DAL.Interfaces.ICaseDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CaseDAO implements ICaseDAO {

    private DBConnector db;

    public CaseDAO() {
        db = DBConnector.getInstance();
    }

    @Override
    public List<Case> getCasesForThisCustomer(int customerID) throws SQLException {
        List<Case> cases = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Case_ LEFT JOIN User_ ON Case_.Case_Assigned_Tech_ID = User_.User_ID WHERE Case_Customer_ID = " + customerID + ";";
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                int caseID = rs.getInt("Case_ID");
                String caseName = rs.getString("Case_Name");
                String caseDescription = rs.getString("Case_Description");
                String contactPerson = rs.getString("Case_Contact_Person");
                String techName = rs.getString("User_Full_Name");
                LocalDate date = rs.getDate("Case_Created_Date").toLocalDate();

                Case c = new Case(caseID, caseName, caseDescription, contactPerson, customerID, techName, date);
                cases.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not get cases from Database");
        }
        return cases;

    }

    @Override
    public void createNewCase(String caseName, String caseContact, String caseDescription, int customerID) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Case_(Case_Name, Case_Description, Case_Contact_Person, Case_Customer_ID, Case_Created_Date) VALUES(?,?,?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, caseName);
            ps.setString(2, caseDescription);
            ps.setString(3, caseContact);
            ps.setInt(4, customerID);
            ps.setDate(5, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Could not create New Case");
        }
    }

    @Override
    public void addTechnicianToCase(int caseID, int technicianID) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE Case_ SET Case_Assigned_Tech_ID = " + technicianID + " WHERE Case_ID = " + caseID+";";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLException("Could not add Technician to Case");
        }
    }
}
