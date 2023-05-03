package GUI.Model;

import BE.Customer;
import BE.Section;
import BLL.Manager;

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

    public void createSection(Section section) throws Exception {
        manager.createSection(section);
    }
    public void saveCustomer(Customer customer) {
        manager.saveCustomer(customer);
    }
}
