package DAL;

import BE.Report;
import DAL.Interfaces.IReportDAO;
import GUI.Controller.ControllerAssistant;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO implements IReportDAO {

    private DBConnector db;

    public ReportDAO() {
        db = DBConnector.getInstance();
    }

    @Override
    public void createNewReport(String reportName, String reportDescription, int caseID, int userID) throws SQLException {
        LocalDate date = LocalDate.now();
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Report(Report_Name, Report_Description, Report_Assigned_Tech_ID, Report_Case_ID, Report_Created_Date, Report_Log_ID, Report_Is_Active) VALUES(?,?,?,?,?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, reportName);
            ps.setString(2, reportDescription);
            ps.setInt(3,userID);
            ps.setInt(4, caseID);
            ps.setDate(5, Date.valueOf(date));
            ps.setInt(6,1);
            ps.setBoolean(7, true);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not create report");
        }

    }

    @Override
    public List<Report> getReports(int caseID) throws SQLException {
        List<Report> reports = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Report JOIN User_ ON Report.Report_Assigned_Tech_ID = User_.User_ID WHERE Report_Case_ID = " + caseID + ";";
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
            int reportID = rs.getInt("Report_ID");
            String reportName = rs.getString("Report_Name");
            String reportDescription = rs.getString("Report_Description");
            String techName = rs.getString("User_Full_Name");
            LocalDate createdDate = rs.getDate("Report_Created_Date").toLocalDate();
            int logID = rs.getInt("Report_Log_ID");
            boolean isActive = rs.getBoolean("Report_Is_Active");

            Report r = new Report(reportID, reportName, reportDescription, caseID, techName, createdDate, logID, isActive);
            reports.add(r);}
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not get reports from Database");
        }
        return reports;
    }
}
