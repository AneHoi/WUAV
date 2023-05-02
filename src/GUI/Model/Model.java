package GUI.Model;

import BE.Case;
import BE.Customer;
import BE.Report;
import BLL.Manager;

import java.sql.SQLException;
import java.util.List;

public class Model {

    private static Model model;
    private Customer currentCustomer;
    private Case currentCase;
    private Report currentReport;
    private List<Customer> customers;
    private Manager manager;

    public Model() {
        manager = new Manager();
    }

    public static Model getInstance() {
        if (model == null) model = new Model();
        return model;
    }

    public List<Customer> getAllCostumers() throws SQLException {
        customers = manager.getAllCustomers();
        return customers;
    }


    public void saveCustomer(Customer customer) {
        manager.saveCustomer(customer);
    }

    public void createNewReport(String reportName, String reportDescription, int caseID, int userID) throws SQLException {
        manager.createNewReport(reportName, reportDescription, caseID, userID);
    }

    public List<Report> getReports(int caseID) throws SQLException {
        return manager.getReports(caseID);
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
}
