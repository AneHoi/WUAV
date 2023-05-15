package DAL.Interfaces;

import BE.Customer;

import java.sql.SQLException;
import java.util.List;

public interface ICustomerDAO {
    Customer getChosenCustomer(int chosenCustomer) throws SQLException;

}
