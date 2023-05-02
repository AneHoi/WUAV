package DAL;

import BE.Case;
import BE.Report;
import DAL.Interfaces.ICaseDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            String sql = "SELECT * FROM Case_ JOIN User_ ON Case_.Case_Assigned_Tech_ID = User_.User_ID WHERE Case_Customer_ID = " + customerID + ";";
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                int caseID = rs.getInt("Case_ID");
                String caseName = rs.getString("Case_Name");
                String caseDescription = rs.getString("Case_Description");
                String contactPerson = rs.getString("Case_Contact_Person");
                String techName = rs.getString("User_Full_Name");
                LocalDate date = rs.getDate("Case_Created_Date").toLocalDate();

                Case c = new Case(caseID,caseName,caseDescription,contactPerson,customerID,techName,date);
                cases.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not get cases from Database");
        }
        return cases;

    }
}
