package DAL;

import BE.*;
import DAL.Interfaces.IReportDAO;

import javax.swing.plaf.nimbus.State;
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
            ps.setString(7, "Open");
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not create report");
        }

    }

    @Override
    public Report getChosenReport(int reportID) throws SQLException {
        Report report = null;
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Report JOIN User_ ON Report.Report_Assigned_Tech_ID = User_.User_ID WHERE Report_ID = " + reportID + ";";
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                int reportId = rs.getInt("Report_ID");
                String reportName = rs.getString("Report_Name");
                String reportDescription = rs.getString("Report_Description");
                String techName = rs.getString("User_Full_Name");
                LocalDate createdDate = rs.getDate("Report_Created_Date").toLocalDate();
                int logID = rs.getInt("Report_Log_ID");
                String isActive = rs.getString("Report_Is_Active");

                report = new Report(reportId, reportName, reportDescription, techName, createdDate, logID, isActive);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Could not get reports from Database");
        }
        return report;
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
                String isActive = rs.getString("Report_Is_Active");

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
                String isActive = rs.getString("Report_Is_Active");
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

                Report reportObj = new Report(reportID, reportName, reportDescription, techName, createdDate, logID, isActive);
                Case caseObj = new Case(caseID, caseName, caseDescription, contactPerson, caseCustomerID, techName, caseCreatedDate);
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
    public void SaveTextToReport(int position, int reportID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Text_Or_Image_On_Report(Text_On_Report, Added_By_Tech, Added_Date, Added_Time) VALUES (?, ?, ?, ?)";
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
            String sql2 = "INSERT INTO Text_And_Image_Report_Link(Report_ID, Text_Or_Image_On_Report_ID, Text_Or_Image, Position_In_Report) VALUES(?,?,?,?);";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, reportID);
            ps2.setInt(2, Text_On_Report_ID);
            ps2.setString(3, "Text");
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
            String sql = "INSERT INTO Text_Or_Image_On_Report(Image_On_Report_Image, Image_On_Report_Comment, Added_By_Tech, Added_Date, Added_Time) VALUES (?, ?, ?, ?, ?);";
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

            String sql2 = "INSERT INTO Text_And_Image_Report_Link(Report_ID, Text_Or_Image_On_Report_ID, Text_Or_Image, Position_In_Report) VALUES(?,?,?,?);";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, reportID);
            ps2.setInt(2, Image_On_Report_ID);
            ps2.setString(3, "Image");
            ps2.setInt(4, position);
            ps2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    @Override
    public List<TextsAndImagesOnReport> getImagesAndTextsForReport(int currentReportID) throws SQLException {
        List<TextsAndImagesOnReport> textsAndImagesOnReports = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Text_And_Image_Report_Link JOIN Text_Or_Image_On_Report" +
                    " ON Text_And_Image_Report_Link.Text_Or_Image_On_Report_ID = Text_Or_Image_On_Report.Text_Or_Image_On_Report_ID" +
                    " JOIN User_ ON Text_Or_Image_On_Report.Added_By_Tech = User_.User_ID WHERE Report_ID ="
                    + currentReportID + " ORDER BY Position_In_Report;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String textOrImage = rs.getString("Text_Or_Image");
                int userID = rs.getInt("User_ID");
                String fullName = rs.getString("User_Full_Name");
                Technician t = new Technician(userID, fullName);
                int iD = rs.getInt("Text_Or_Image_On_Report_ID");
                if (textOrImage.equals("Text")) {
                    String text = rs.getString("Text_On_Report");
                    LocalDate date = rs.getDate("Added_Date").toLocalDate();
                    LocalTime time = rs.getTime("Added_Time").toLocalTime();
                    int positionInReport = rs.getInt("Position_In_Report");
                    TextsAndImagesOnReport tOR = new TextsAndImagesOnReport(iD, text, textOrImage, positionInReport, t, date, time);
                    textsAndImagesOnReports.add(tOR);
                } else {
                    byte[] imageData = rs.getBytes("Image_On_Report_Image");
                    String comment = rs.getString("Image_On_Report_Comment");
                    LocalDate date = rs.getDate("Added_Date").toLocalDate();
                    LocalTime time = rs.getTime("Added_Time").toLocalTime();
                    int positionInReport = rs.getInt("Position_In_Report");
                    TextsAndImagesOnReport tOR = new TextsAndImagesOnReport(iD, imageData, comment, textOrImage, positionInReport, t, date, time);
                    textsAndImagesOnReports.add(tOR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
        return textsAndImagesOnReports;
    }

    public void updateImageInReport(int imageID, byte[] dataImage, String comment, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE Text_Or_Image_On_Report SET Image_On_Report_Image = (?), Image_On_Report_Comment = (?), Added_By_Tech = (?), Added_Date = (?), Added_Time = (?) WHERE Text_Or_Image_On_Report_ID = (?);";
            PreparedStatement ps1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps1.setBytes(1, dataImage);
            ps1.setString(2, comment);
            ps1.setInt(3, userID);
            ps1.setDate(4, Date.valueOf(createdDate));
            ps1.setTime(5, Time.valueOf(createdTime));
            ps1.setInt(6, imageID);

            ps1.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    @Override
    public void deletePartOfReport(TextsAndImagesOnReport textOrImage) throws SQLException {
        try (Connection conn = db.getConnection()) {
            int position = 0;
            String sql1 = "SELECT * FROM Text_And_Image_Report_Link WHERE Text_Or_Image_On_Report_ID = (?);";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, textOrImage.getTextOrImageID());
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                position = rs.getInt("Position_In_Report");
            }

            String sql2 = "DELETE FROM Text_Or_Image_On_Report WHERE Text_Or_Image_On_Report_ID = (?);";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, textOrImage.getTextOrImageID());
            ps2.executeUpdate();

            String sql3 = "UPDATE Text_And_Image_Report_Link SET Position_In_Report = Position_In_Report - 1 WHERE Position_In_Report > (?);";
            PreparedStatement ps3 = conn.prepareStatement(sql3);
            ps3.setInt(1, position);
            ps3.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }

    }

    public void updateTextInReport(int textID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE Text_Or_Image_On_Report SET Text_On_Report = (?), Added_By_Tech = (?), Added_Date = (?), Added_Time = (?) WHERE Text_Or_Image_On_Report_ID = (?);";
            PreparedStatement ps1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, txt);
            ps1.setInt(2, userID);
            ps1.setDate(3, Date.valueOf(createdDate));
            ps1.setTime(4, Time.valueOf(createdTime));
            ps1.setInt(5, textID);

            ps1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public void moveItemUp(int textOrImageID, int positionOnReport) throws SQLException, IllegalStateException {
        try (Connection conn = db.getConnection()) {

            conn.setAutoCommit(false);

            int itemAboveId;
            String getItemAboveIdQuery = "SELECT Text_Or_Image_On_Report_ID FROM Text_And_Image_Report_Link WHERE Position_In_Report = ?";
            try (PreparedStatement getItemAboveIdStatement = conn.prepareStatement(getItemAboveIdQuery)) {
                getItemAboveIdStatement.setInt(1, positionOnReport - 1);
                try (ResultSet resultSet = getItemAboveIdStatement.executeQuery()) {
                    if (resultSet.next()) {
                        itemAboveId = resultSet.getInt("Text_Or_Image_On_Report_ID");
                    } else {
                        throw new IllegalStateException("The item is already at the top and cannot be moved up.");
                    }
                }
            }


            String updatePositionsQuery = "UPDATE Text_And_Image_Report_Link SET Position_In_Report = ? WHERE Text_Or_Image_On_Report_ID = ?";
            try (PreparedStatement updatePositionsStatement = conn.prepareStatement(updatePositionsQuery)) {

                updatePositionsStatement.setInt(1, positionOnReport - 1);
                updatePositionsStatement.setInt(2, textOrImageID);
                updatePositionsStatement.executeUpdate();

                updatePositionsStatement.setInt(1, positionOnReport);
                updatePositionsStatement.setInt(2, itemAboveId);
                updatePositionsStatement.executeUpdate();
            }


            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }


    public void moveItemDown(int textOrImageID, int positionOnReport) throws SQLException, IllegalStateException {
        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false);
            int itemBelowId;
            String getItemBelowIdQuery = "SELECT Text_Or_Image_On_Report_ID FROM Text_And_Image_Report_Link WHERE Position_In_Report = ?";
            try (PreparedStatement getItemBelowIdStatement = db.getConnection().prepareStatement(getItemBelowIdQuery)) {
                getItemBelowIdStatement.setInt(1, positionOnReport + 1);
                try (ResultSet resultSet = getItemBelowIdStatement.executeQuery()) {
                    if (resultSet.next()) {
                        itemBelowId = resultSet.getInt("Text_Or_Image_On_Report_ID");
                    } else {
                        throw new IllegalStateException("The item is already at the top and cannot be moved up.");
                    }
                }
            }
            String updatePositionsQuery = "UPDATE Text_And_Image_Report_Link SET Position_In_Report = ? WHERE Text_Or_Image_On_Report_ID = ?";
            try (PreparedStatement updatePositionsStatement = conn.prepareStatement(updatePositionsQuery)) {

                updatePositionsStatement.setInt(1, positionOnReport + 1);
                updatePositionsStatement.setInt(2, textOrImageID);
                updatePositionsStatement.executeUpdate();

                updatePositionsStatement.setInt(1, positionOnReport);
                updatePositionsStatement.setInt(2, itemBelowId);
                updatePositionsStatement.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    @Override
    public void submitReportForReview(int reportID) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE Report SET Report_Is_Active = 'Submitted For Review' WHERE Report_ID = " + reportID + ";";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    @Override
    public void closeReport(int reportID) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE Report SET Report_Is_Active = 'Closed' WHERE Report_ID = " + reportID + ";";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public void updateReport(int reportID, String reportName, String reportDescription, int userID) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE Report SET Report_Name = (?), Report_Description = (?), Report_Assigned_Tech_ID = (?) WHERE Report_ID = (?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, reportName);
            ps.setString(2, reportDescription);
            ps.setInt(3, userID);
            ps.setInt(4, reportID);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public void deleteReport(int reportID) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "DELETE FROM Report WHERE Report_ID = (?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,reportID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }
}

