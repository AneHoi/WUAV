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
            throw new SQLException("Could not get Cases from Database");
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
    public void addTechnicianToCase(int caseID, List<Technician> chosenTechnicians) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql1 = "DELETE FROM Technicians_Assigned_To_Case WHERE Case_ID = ?";
            try (PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
                stmt1.setInt(1, caseID);
                stmt1.executeUpdate();
            }

            String sql2 = "INSERT INTO Technicians_Assigned_To_Case(Technician_ID, Case_ID) VALUES (?, ?)";
            try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                for (Technician t : chosenTechnicians) {
                    stmt2.setInt(1, t.getUserID());
                    stmt2.setInt(2, caseID);
                    stmt2.addBatch();
                }
                stmt2.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not add Technician to Case");
        }
    }


    @Override
    public List<Case> getAllCases() throws SQLException {
        List<Case> cases = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Case_ LEFT JOIN User_ ON Case_.Case_Assigned_Tech_ID = User_.User_ID;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int caseID = rs.getInt("Case_ID");
                String caseName = rs.getString("Case_Name");
                String caseDescription = rs.getString("Case_Description");
                String contactPerson = rs.getString("Case_Contact_Person");
                int customerID = rs.getInt("Case_Customer_ID");
                String techName = rs.getString("User_Full_Name");
                LocalDate date = rs.getDate("Case_Created_Date").toLocalDate();

                Case c = new Case(caseID, caseName, caseDescription, contactPerson, customerID, techName, date);
                cases.add(c);
            }

        } catch (SQLException e) {
            throw new SQLException("Could not get Cases from Database");
        }
        return cases;
    }

    @Override
    public void updateCase(int caseID, String caseName, String contactPerson, String caseDescription) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE Case_ SET Case_Name = (?), Case_Description = (?), Case_Contact_Person = (?) WHERE Case_ID = (?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, caseName);
            ps.setString(2, caseDescription);
            ps.setString(3, contactPerson);
            ps.setInt(4, caseID);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not update Case in database");
        }
    }

    @Override
    public List<Technician> getAssignedTechnicians(int caseID) throws SQLException {
        List<Technician> assignedTechs = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Technicians_Assigned_To_Case LEFT JOIN User_ ON Technicians_Assigned_To_Case.Technician_ID = User_.User_ID WHERE Case_ID = " + caseID + ";";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int techID = rs.getInt("User_ID");
                String techName = rs.getString("User_Full_Name");
                boolean isActive = rs.getBoolean("User_Active");

                Technician t = new Technician(techID, techName);
                if (isActive) {
                    assignedTechs.add(t);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not get assigned Technicians from database");
        }
        return assignedTechs;
    }
}
