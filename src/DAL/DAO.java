package DAL;

import BE.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private DBConnector db;

    public DAO() {
        db = DBConnector.getInstance();
    }
    public List<Customer> getAllCostumers() {
        List<Customer> customers = new ArrayList<>();
        try(Connection conn = db.getConnection()){
            String sql = "SELECT * FROM Customer;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Customer_Address");
                String mail = rs.getString("Customer_Mail");
                String tlf = rs.getString("Customer_tlf");
                int cvr = rs.getInt("Customer_CVR");
                String type = rs.getString("Customer_Type");

                Customer customer = new Customer(id, name, address, tlf, mail, cvr, type);
                customers.add(customer);
            }
            return customers;

        }
        catch (Exception e) {

        }

        return customers;
    }

    public void saveCustomer(Customer customer) {
        try (Connection conn = db.getConnection()){
            String sql = "INSERT INTO Customer" +
                    "(Customer_Name, Customer_Address, Customer_Mail, Customer_tlf, Customer_CVR, Customer_Type)" +
                    "VALUES(?,?,?,?,?,?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, customer.getCustomerName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getPhoneNumber());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setInt(5, customer.getCVR());
            preparedStatement.setString(6, customer.getCustomerType());


            //preparedStatement.setInt(1, customer.getCustomerID());
            /*
            preparedStatement.setString(2, customer.getCustomerName());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setString(4, customer.getPhoneNumber());
            preparedStatement.setString(5, customer.getEmail());
            preparedStatement.setInt(6, customer.getCVR());
            preparedStatement.setString(7, customer.getCustomerType());
             */
            preparedStatement.executeUpdate();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
