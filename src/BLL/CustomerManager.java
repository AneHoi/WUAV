package BLL;

import BE.Customer;
import DAL.CustomerDAO;

import java.sql.SQLException;
import java.util.List;

public class CustomerManager {

    private CustomerDAO customerDAO;

    public CustomerManager(){
        customerDAO = new CustomerDAO();
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.getAllCostumers();
    }
    public Customer getChosenCustomer(int chosenCustomer) throws SQLException {
        return customerDAO.getChosenCustomer(chosenCustomer);
    }

    public void saveCustomer(Customer customer) {
        customerDAO.saveCustomer(customer);
    }
    public void updateCustomer(Customer customer) throws SQLException {
        customerDAO.updateCustomer(customer);
    }
    public void deleteCustomer(Customer customer) throws SQLException {
        customerDAO.deleteCustomer(customer);
    }
}
