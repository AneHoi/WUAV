package GUI.Model;

import BE.Customer;
import BE.Report;
import BLL.Manager;

import java.sql.SQLException;
import java.util.List;

public class Model {

    private List<Customer> customers;
    private Manager manager;

    public Model(){
        manager = new Manager();
    }

    public List<Customer> getAllCostumers() {
        customers = manager.getAllCustomers();
        return customers;
    }


    public void saveCustomer(Customer customer) {
        manager.saveCustomer(customer);
    }

    public void createNewReport(String reportName, String reportDescription, int caseID) throws SQLException {
        manager.createNewReport(reportName,reportDescription, caseID);
    }

    public List<Report> getReports(int caseID) throws SQLException {
        return manager.getReports(caseID);
    }
}
