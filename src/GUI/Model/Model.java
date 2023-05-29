package GUI.Model;


import BE.*;
import BLL.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private static Model model;
    private Customer currentCustomer;
    private Case currentCase;
    private Report currentReport;
    private List<Customer> customers;
    private CustomerManager customerManager;
    private ReportManager reportManager;
    private UserManager userManager;

    private CaseManager caseManager;
    private DrawingManager drawingManager;

    public Model() {
        customerManager = new CustomerManager();
        reportManager = new ReportManager();
        userManager = new UserManager();
        caseManager = new CaseManager();
        drawingManager = new DrawingManager();
    }

    public static Model getInstance() {
        if (model == null) model = new Model();
        return model;
    }

    public Customer getChosenCustomer(int chosenCustomer) throws SQLException {
        return customerManager.getChosenCustomer(chosenCustomer);
    }

    public List<Customer> getAllCustomers() throws SQLException {
        customers = customerManager.getAllCustomers();
        return customers;
    }

    public void saveCustomer(Customer customer) {
        customerManager.saveCustomer(customer);
    }

    public void createNewReport(String reportName, String reportDescription, int caseID, int userID) throws SQLException {
        reportManager.createNewReport(reportName, reportDescription, caseID, userID);
    }

    public Report getChosenReport(int reportID) throws SQLException {
        return reportManager.getChosenReport(reportID);
    }

    public List<Report> getReports(int caseID) throws SQLException {
        return reportManager.getReports(caseID);
    }


    public List<ReportCaseAndCustomer> getAllReports() throws SQLException {
        return reportManager.getAllReports();
    }

    public List<Technician> getAllTechnicians() throws SQLException {
        return userManager.getAllTechnicians();
    }

    public void createNewCase(String caseName, String caseContact, String caseDescription, int customerID) throws SQLException {
        caseManager.createNewCase(caseName, caseContact, caseDescription, customerID);
    }

    public void addTechnicianToCase(int caseID, List<Technician> chosenTechnicians) throws SQLException {
        caseManager.addTechnicianToCase(caseID, chosenTechnicians);
    }

    public List<Case> getAllCases() throws SQLException {
        return caseManager.getAllCases();
    }

    public List<User> getAllUsers() throws SQLException {
        return userManager.getAllUsers();
    }

    public void updateUser(int userID, String fullName, String userName, String userTlf, String userEmail, boolean userActive) throws SQLException {
        userManager.updateUser(userID, fullName, userName, userTlf, userEmail, userActive);
    }

    public void createNewUser(String fullName, String userName, String userTlf, String userEmail, int userType) throws SQLException {
        userManager.createNewUser(fullName, userName, userTlf, userEmail, userType);
    }
    public void setPassword(String userName, String password) throws Exception {
        userManager.handlePassword(userName, password);
    }

    public User checkLogIn(String userName, String password) throws Exception {
        return userManager.checkLoggedInUser(userName, password);
    }
    public boolean checkPassword(String hashedPW, String password) throws Exception {
        return userManager.checkPassword(hashedPW, password);
    }

    public void updateCase(int caseID, String caseName, String contactPerson, String caseDescription) throws SQLException {
        caseManager.updateCase(caseID, caseName, contactPerson, caseDescription);
    }

    public void updateCustomer(Customer customer) throws SQLException {
        customerManager.updateCustomer(customer);
    }

    public List<Technician> getAssignedTechnicians(int caseID) throws SQLException {
        return caseManager.getAssignedTechnicians(caseID);
    }

    public void SaveTextToReport(int position, int reportID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportManager.SaveTextToReport(position, reportID, txt, userID, createdDate, createdTime);
    }

    public void SaveImageToReport(int position, int reportID, byte[] dataImage, String comment, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportManager.SaveImageToReport(position, reportID, dataImage, comment, userID, createdDate, createdTime);
    }

    public void deleteCustomer(Customer customer) throws SQLException {
        customerManager.deleteCustomer(customer);

    }

    public void setCurrentReport(Report selectedItem) {
        currentReport = selectedItem;
    }

    public Report getCurrentReport() {
        return currentReport;
    }

    public void setCurrentCase(Case currentCase) {
        this.currentCase = currentCase;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public Case getCurrentCase() {
        return currentCase;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public List<Case> getCasesForThisCustomer(int customerID) throws SQLException {
        return caseManager.getCasesForThisCustomer(customerID);
    }

    public List<TextsAndImagesOnReport> getImagesAndTextsForReport(int currentReportID) throws SQLException {
        return reportManager.getImagesAndTextsForReport(currentReportID);
    }

    public void updateImageInReport(int imageID, byte[] dataImage, String comment, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportManager.updateImageInReport(imageID, dataImage, comment, userID, createdDate, createdTime);
    }

    public void deletePartOfReport(TextsAndImagesOnReport textOrImage) throws SQLException {
        reportManager.deletePartOfReport(textOrImage);
    }

    public Case getChosenCase(int chosenCase) throws SQLException {
        return caseManager.getChosenCase(chosenCase);
    }

    public void updateTextInReport(int textID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportManager.updateTextInReport(textID, txt, userID, createdDate, createdTime);
    }

    public void moveItemUp(int textOrImageID, int positionOnReport) throws SQLException, IllegalStateException {
        reportManager.moveItemUp(textOrImageID, positionOnReport);
    }

    public void moveItemDown(int textOrImageID, int positionOnReport) throws SQLException, IllegalStateException {
        reportManager.moveItemDown(textOrImageID, positionOnReport);
    }

    public void deleteCase(Case casen) throws SQLException {
        caseManager.deleteCase(casen);
    }

    public void submitReportForReview(int reportID) throws SQLException {
        reportManager.submitReportForReview(reportID);
    }

    public void closeReport(int reportID) throws SQLException {
        reportManager.closeReport(reportID);
    }

    public void updateReport(int reportID, String reportName, String reportDescription, int userID) throws SQLException {
        reportManager.updateReport(reportID, reportName, reportDescription, userID);
    }

    public void deleteReport(int reportID) throws SQLException {
        reportManager.deleteReport(reportID);
    }

    public void closeCase(Case chosenCase) throws SQLException {
        caseManager.closeCase(chosenCase);
    }

    public void expandKeepingTime(Case casen, int daysToKeep) throws SQLException {
        caseManager.expandKeepingTime(casen, daysToKeep);
    }

    public void saveLoginDetails(int reportID, String component, String username, String password, String additionalInfo, LocalDate createdDate, LocalTime createdTime, int userID) throws SQLException {
        reportManager.saveLoginDetails(reportID, component, username, password, additionalInfo, createdDate, createdTime, userID);
    }

    public void noLoginInfoForThisReport(int reportID, LocalDate createdDate, LocalTime createdTime, int userID) throws SQLException {
        reportManager.noLoginInfoForThisReport(reportID, createdDate, createdTime, userID);
    }

    public List<LoginDetails> getLoginDetails(int reportID) throws SQLException {
        return reportManager.getLoginDetails(reportID);
    }

    public void deleteLoginDetails(int loginDetailsID) throws SQLException {
        reportManager.deleteLoginDetails(loginDetailsID);
    }

    public void updateLoginDetails(int loginDetailsID, String component, String username, String password, String additionalInfo, LocalDate createdDate, LocalTime createdTime, int userID) throws SQLException {
        reportManager.updateLoginDetails(loginDetailsID, component, username, password, additionalInfo, createdDate, createdTime, userID);
    }

    public void updateToNoLogin(int loginDetailsID, LocalDate createdDate, LocalTime createdTime, int userID) throws SQLException {
        reportManager.updateToNoLogin(loginDetailsID, createdDate, createdTime, userID);
    }

    public void addDrwaingIcon(byte[] dataImage, String text) throws SQLException {
        drawingManager.addDrawingIcon(dataImage, text);
    }

    public List<DrawingIcon> getAllDrawingIcons() throws SQLException {
        return drawingManager.getAllDrawingIcons();
    }

    public void storeUserCustomerLink(int userID, int customerID) throws SQLException {
        customerManager.storeUserCustomerLink(userID, customerID);
    }

    public void storeUserCaseLink(int userID, int caseID) throws SQLException {
        caseManager.storeUserCaseLink(userID, caseID);
    }

    public List<Case> getUsersActiveCases(int userID) throws SQLException {
        return caseManager.getUsersActiveCases(userID);
    }

    public List<Customer> getRecentlyViewedCustomers(int userID) throws SQLException {
        return customerManager.getRecentlyViewedCustomers(userID);
    }

    public ArrayList<CabelAndColor> getAllCables() {
        return drawingManager.getAllCables();
    }
}
