package BLL;

import BE.Customer;
import DAL.DAO;

import java.util.List;

public class Manager {

    private DAO dao;

    public Manager() {
        dao = new DAO();
    }

    public List<Customer> getAllCustomers() {
        return dao.getAllCostumers();
    }

    public void saveCustomer(Customer customer) {
        dao.saveCustomer(customer);
    }
}
