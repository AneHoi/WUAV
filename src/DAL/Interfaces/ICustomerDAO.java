package DAL.Interfaces;

import BE.Customer;
import BE.User;

import java.sql.SQLException;
import java.util.List;

public interface ICustomerDAO {
    Customer getChosenCustomer(int chosenCustomer) throws SQLException;

    void storeUserCustomerLink(int userID, int customerID) throws SQLException;

    List<Customer> getRecentlyViewedCustomers(int userID) throws SQLException;
}
