package DAL;

import BE.*;
import DAL.Interfaces.IReportDAO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
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
            ps.setInt(3, userID);
            ps.setInt(4, caseID);
            ps.setDate(5, Date.valueOf(date));
            ps.setInt(6, 1);
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
                reports.add(r);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not get reports from Database");
        }
        return reports;
    }

    @Override
    public List<ReportCaseAndCustomer> getAllReports() throws SQLException {
        List<ReportCaseAndCustomer> reportCaseAndCustomers = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Report JOIN User_ ON Report.Report_Assigned_Tech_ID = User_.User_ID JOIN Case_ ON Report.Report_Case_ID = Case_.Case_ID JOIN Customer ON Case_.Case_Customer_ID = Customer.Customer_ID;";
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
                int caseID = rs.getInt("Case_ID");
                String caseName = rs.getString("Case_Name");
                String caseDescription = rs.getString("Case_Description");
                String contactPerson = rs.getString("Case_Contact_Person");
                int caseCustomerID = rs.getInt("Case_Customer_ID");
                LocalDate caseCreatedDate = rs.getDate("Case_Created_Date").toLocalDate();
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Customer_Address");
                String phoneNumber = rs.getString("Customer_Tlf");
                String email = rs.getString("Customer_Mail");
                int cvr = rs.getInt("Customer_CVR");
                String customerType = rs.getString("Customer_Type");

                Report reportObj = new Report(reportID, reportName, reportDescription,techName,createdDate,logID,isActive);
                Case caseObj = new Case(caseID,caseName, caseDescription, contactPerson, caseCustomerID, techName, caseCreatedDate);
                Customer customerObj = new Customer(customerID, customerName, address, phoneNumber, email, cvr, customerType);
                ReportCaseAndCustomer rCC = new ReportCaseAndCustomer(reportObj, caseObj, customerObj);
                reportCaseAndCustomers.add(rCC);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not get reports from Database");
        }
        return reportCaseAndCustomers;
    }

    @Override
    public void createNewAddendum(String addendumName, String addendumDescription, int caseID, int reportID, int userID) throws SQLException {
        LocalDate date = LocalDate.now();
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Addendum(Addendum_Name, Addendum_Description, Addendum_Assigned_Tech_ID, Addendum_Report_ID, Addendum_Case_ID, Addendum_Created_Date, Addendum_Log_ID, Addendum_Is_Active) VALUES(?,?,?,?,?,?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, addendumName);
            ps.setString(2, addendumDescription);
            ps.setInt(3, userID);
            ps.setInt(4, reportID);
            ps.setInt(5, caseID);
            ps.setDate(6, Date.valueOf(date));
            ps.setInt(7, 1);
            ps.setBoolean(8, true);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not create report");
        }

    }

    @Override
    public List<Addendum> getAddendums(int caseID, int reportID) throws SQLException {
        List<Addendum> addendums = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Addendum JOIN User_ ON Addendum.Addendum_Assigned_Tech_ID = User_.User_ID WHERE Addendum_Case_ID = " + caseID + " AND Addendum_Report_ID =" + reportID + ";";
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                int addendumID = rs.getInt("Addendum_ID");
                String addendumName = rs.getString("Addendum_Name");
                String addendumDescription = rs.getString("Addendum_Description");
                String techName = rs.getString("User_Full_Name");
                LocalDate createdDate = rs.getDate("Addendum_Created_Date").toLocalDate();
                int logID = rs.getInt("Addendum_Log_ID");
                boolean isActive = rs.getBoolean("Addendum_Is_Active");

                Addendum a = new Addendum(addendumID, addendumName, addendumDescription, caseID, techName, createdDate, logID, isActive, reportID);
                addendums.add(a);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not get reports from Database");
        }
        return addendums;
    }

    @Override
    public void SaveTextToReport(int position, int reportID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Text_On_Report(Text_On_Report_Text, Text_On_Report_Made_By_Tech, Text_On_Report_Created_Date, Text_On_Report_Created_Time) VALUES (?, ?, ?, ?)";
            PreparedStatement ps1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, txt);
            ps1.setInt(2, userID);
            ps1.setDate(3, Date.valueOf(createdDate));
            ps1.setTime(4, Time.valueOf(createdTime));


            int Text_On_Report_ID;
            int rowsAffected = ps1.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps1.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Text_On_Report_ID = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve generated keys.");
                    }
                }
            } else {
                throw new SQLException("No rows affected. Failed to insert Image_On_Report.");
            }
            String sql2 = "INSERT INTO Text_And_Image_Report_Link(Report_ID, Text_Or_Image, Text_On_Report_ID, Position_In_Report) VALUES(?,?,?,?);";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, reportID);
            ps2.setString(2,"Text");
            ps2.setInt(3, Text_On_Report_ID);
            ps2.setInt(4, position);
            ps2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    @Override
    public void SaveImageToReport(int position, int reportID, byte[] dataImage, String comment, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Image_On_Report(Image_On_Report_Image, Image_On_Report_Comment, Image_On_Report_Made_By_Tech, Image_On_Report_Created_Date, Image_On_Report_Created_Time) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement ps1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps1.setBytes(1, dataImage);
            ps1.setString(2, comment);
            ps1.setInt(3, userID);
            ps1.setDate(4, Date.valueOf(createdDate));
            ps1.setTime(5, Time.valueOf(createdTime));

            int Image_On_Report_ID;
            int rowsAffected = ps1.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps1.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Image_On_Report_ID = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve generated keys.");
                    }
                }
            } else {
                throw new SQLException("No rows affected. Failed to insert Image_On_Report.");
            }

            String sql2 = "INSERT INTO Text_And_Image_Report_Link(Report_ID, Text_Or_Image, Image_On_Report_ID, Position_In_Report) VALUES(?,?,?,?);";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, reportID);
            ps2.setString(2,"Image");
            ps2.setInt(3, Image_On_Report_ID);
            ps2.setInt(4, position);
            ps2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    @Override
    public List<TextOnReport> getAllTextFieldsForReport(int currentReportID) throws SQLException {
        List<TextOnReport> textsOnReport = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Text_And_Image_Report_Link JOIN Text_On_Report ON Text_And_Image_Report_Link.Text_On_Report_ID = Text_On_Report.Text_On_Report_ID JOIN User_ ON Text_On_Report.Text_On_Report_Made_By_Tech = User_.User_ID WHERE Report_ID =" + currentReportID + " AND Text_Or_Image = 'Text';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int userID = rs.getInt("User_ID");
                String fullName = rs.getString("User_Full_Name");
                Technician t = new Technician(userID, fullName);
                String text = rs.getString("Text_On_Report_Text");
                LocalDate date = rs.getDate("Text_On_Report_Created_Date").toLocalDate();
                LocalTime time = rs.getTime("Text_On_Report_Created_Time").toLocalTime();
                int positionInReport = rs.getInt("Position_In_Report");
                TextOnReport tOR = new TextOnReport(text, positionInReport, t, date, time);
                textsOnReport.add(tOR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }

        return textsOnReport;
    }

    @Override
    public List<ImageOnReport> getAllImagesForReport(int currentReportID) throws SQLException {
        List<ImageOnReport> imagesOnReport = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Text_And_Image_Report_Link JOIN Image_On_Report ON Text_And_Image_Report_Link.Image_On_Report_ID = Image_On_Report.Image_On_Report_ID JOIN User_ ON Image_On_Report.Image_On_Report_Made_By_Tech = User_.User_ID WHERE Report_ID =" + currentReportID + " AND Text_Or_Image = 'Image';";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int userID = rs.getInt("User_ID");
                String fullName = rs.getString("User_Full_Name");
                Technician t = new Technician(userID, fullName);
                byte[] imageBytes = rs.getBytes("Image_On_Report_Image");
                String imageComment = rs.getString("Image_On_Report_Comment");
                LocalDate date = rs.getDate("Image_On_Report_Created_Date").toLocalDate();
                LocalTime time = rs.getTime("Image_On_Report_Created_Time").toLocalTime();
                int positionInReport = rs.getInt("Position_In_Report");

                ImageOnReport iOR = new ImageOnReport(imageBytes,imageComment,positionInReport,t,date,time);
                imagesOnReport.add(iOR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }

        return imagesOnReport;
    }

}

