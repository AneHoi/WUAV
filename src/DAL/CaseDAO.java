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

    /**
     * get all cases connected to this customer
     * @param customerID this customers ID
     * @return A list of cases connected to the costumer
     * @throws SQLException
     */
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

    /**
     * Creating a new case based on the customer
     * @param caseName the name of the case, that is about to be created
     * @param caseContact the contact person for the specific case
     * @param caseDescription The description of the case
     * @param customerID The Customer, the case will be linked to
     * @throws SQLException
     */
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

    /**
     * Add a technician to a specific case
     * In the db, this creates a link between the technician and the case.
     * The method deletes the current links between the technicians and the specific case to establish the connection with
     * ALL the technicians that is sent into the method
     * @param caseID
     * @param chosenTechnicians
     * @throws SQLException
     */
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

    /**
     * A caseID is sent into the method to get the Case for from the id.
     * Only used in Search for reports view, because
     * the CaseId is the only thing from the cases saved in the tableview
     * @param chosenCase
     * @return
     * @throws SQLException
     */
    @Override
    public Case getChosenCase(int chosenCase) throws SQLException {
        Case c = null;
        String sql = "SELECT * FROM Case_ LEFT JOIN User_ ON Case_.Case_Assigned_Tech_ID = User_.User_ID WHERE Case_.Case_ID = " + chosenCase + ";";
        try (Connection conn = db.getConnection()) {
            //String sql1 = "SELECT * FROM Case_ WHERE Case_.Case_Name ='" + chosenCase + "';";
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

                c = new Case(caseID, caseName, caseDescription, contactPerson, customerID, techName, date);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not get Case from Database");
        }
        return c;
    }

    /**
     * Get all the cases from the database that are assigned to a user in the system
     * @return
     * @throws SQLException
     */
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
                LocalDate caseClosedDate = null;
                if (rs.getDate("Case_Closed_Date") != null) {
                    caseClosedDate = rs.getDate("Case_Closed_Date").toLocalDate();
                }
                int daysToKeep = rs.getInt("Case_Days_To_Keep");

                Case c = new Case(caseID, caseName, caseDescription, contactPerson, customerID, techName, date, caseClosedDate, daysToKeep);
                cases.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not get Cases from Database");
        }
        return cases;
    }

    /**
     * Update a case based on the parameters:
     * @param caseID
     * @param caseName
     * @param contactPerson
     * @param caseDescription
     * @throws SQLException
     */
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

    /**
     * Reruns all the technicians assigned to the case
     * @param caseID
     * @return
     * @throws SQLException
     */
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

    /**
     * Detele the case and all the connections to it
     * @param selectedCase
     * @throws SQLException
     */
    public void deleteCase(Case selectedCase) throws SQLException {
        try (Connection conn = db.getConnection()) {
            // Create statement object
            Statement stmt = conn.createStatement();

            // Set auto-commit to false
            conn.setAutoCommit(false);

            //Create SQL statements
            String sql1 = "DELETE FROM Technicians_Assigned_To_Case WHERE Case_ID = " + selectedCase.getCaseID() + ";";
            String sql2 = "DELETE FROM Report WHERE Report_Case_ID = " + selectedCase.getCaseID() + ";";
            String sql3 = "DELETE FROM Case_ WHERE Case_ID = " + selectedCase.getCaseID() + ";";

            //Add to batch
            stmt.addBatch(sql1);
            stmt.addBatch(sql2);
            stmt.addBatch(sql3);

            stmt.executeBatch();

            //Explicitly commit statements to apply changes
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not get delete the case from the database");
        }
    }

    /**
     * Close the specific case
     * @param chosenCase
     * @throws SQLException
     */
    public void closeCase(Case chosenCase) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE Case_ SET Case_Closed_Date = (?), Case_Days_To_Keep = (?) WHERE Case_ID = " + chosenCase.getCaseID() + ";";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setInt(2, 1461);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not close Case in database");
        }
    }

    /**
     * Expand how much time, to keep the specific case, if it is closed
     * @param selectedCase
     * @param daysToKeep
     * @throws SQLException
     */
    public void expandKeepingTime(Case selectedCase, int daysToKeep) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE Case_ SET Case_Days_To_Keep = (?) WHERE Case_ID = " + selectedCase.getCaseID() + ";";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, daysToKeep);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not update the time for keeping this casein the database");
        }
    }

    /**
     * Looks for what open cases the user have looked at recently.
     * @param userID
     * @param caseID
     * @throws SQLException
     */
    public void storeUserCaseLink(int userID, int caseID) throws SQLException {
        try (Connection conn = db.getConnection()) {

            String sql1 = "SELECT User_ID FROM User_Active_Cases_Link WHERE User_ID = (?) AND Case_ID = (?);";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, userID);
            ps1.setInt(2, caseID);
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                return;
            }

            String sql2 = "SELECT COUNT(*) FROM Report WHERE Report_Case_ID = (?) AND Report_Is_Active = 'Open';";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, caseID);
            ResultSet rs2 = ps2.executeQuery();
            rs2.next();
            int activeReportsCount = rs2.getInt(1);
            if (activeReportsCount == 0) {
                return;
            }

            String sql3 = "INSERT INTO User_Active_Cases_Link (User_ID, Case_ID) VALUES (?, ?);";
            PreparedStatement ps3 = conn.prepareStatement(sql3);
            ps3.setInt(1, userID);
            ps3.setInt(2, caseID);
            ps3.executeUpdate();


            String sql4 = "SELECT COUNT(*) FROM User_Active_Cases_Link WHERE User_ID = (?);";
            PreparedStatement ps4 = conn.prepareStatement(sql4);
            ps4.setInt(1, userID);
            ResultSet rs3 = ps4.executeQuery();
            rs3.next();
            int linkCount = rs3.getInt(1);


            if (linkCount > 10) {
                String sql5 = "DELETE TOP (?) FROM User_Active_Cases_Link WHERE User_ID = (?) AND User_Active_Cases_Link_ID " +
                        "IN (SELECT User_Active_Cases_Link_ID FROM User_Active_Cases_Link WHERE User_ID = (?) " +
                        "ORDER BY User_Active_Cases_Link_ID ASC);";
                PreparedStatement ps5 = conn.prepareStatement(sql5);
                ps5.setInt(1, linkCount - 10);
                ps5.setInt(2, userID);
                ps5.setInt(3, userID);
                ps5.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Get all active cases connected to a user
     * @param userID
     * @return
     * @throws SQLException
     */
    public List<Case> getUsersActiveCases(int userID) throws SQLException {
        List<Case> usersActiveCases = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM User_Active_Cases_Link JOIN Case_ ON User_Active_Cases_Link.Case_ID = Case_.Case_ID JOIN User_ ON Case_.Case_Assigned_Tech_ID = User_.User_ID WHERE User_Active_Cases_Link.User_ID = (?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int caseID = rs.getInt("Case_ID");
                String caseName = rs.getString("Case_Name");
                String caseDescription = rs.getString("Case_Description");
                String caseContactPerson = rs.getString("Case_Contact_Person");
                int caseCustomerID = rs.getInt("Case_Customer_ID");
                String caseAssignedTech = rs.getString("User_Full_Name");
                LocalDate caseCreatedDate = rs.getDate("Case_Created_Date").toLocalDate();

                Case c = new Case(caseID, caseName, caseDescription, caseContactPerson, caseCustomerID, caseAssignedTech, caseCreatedDate);
                usersActiveCases.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return usersActiveCases;
    }
}
