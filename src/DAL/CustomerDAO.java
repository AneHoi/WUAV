package DAL;

import DAL.Interfaces.ICustomerDAO;

public class CustomerDAO implements ICustomerDAO {

    private DBConnector db;

    public CustomerDAO() {
        db = DBConnector.getInstance();
    }

}
