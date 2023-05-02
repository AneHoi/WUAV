package DAL;

import BE.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private DBConnector db;

    public DAO() {
        db = DBConnector.getInstance();
    }

    public List<Customer> getAllCostumers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Customer;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Customer_Address");
                String mail = rs.getString("Customer_Mail");
                String tlf = rs.getString("Customer_Tlf");
                int cvr = rs.getInt("Customer_CVR");
                String type = rs.getString("Customer_Type");

                Customer customer = new Customer(id, name, address, tlf, mail, cvr, type);
                customers.add(customer);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return customers;
    }

    public void saveCustomer(Customer customer) {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Customer" +
                    "(Customer_Name, Customer_Address, Customer_Mail, Customer_Tlf, Customer_CVR, Customer_Type)" +
                    "VALUES(?,?,?,?,?,?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, customer.getCustomerName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getPhoneNumber());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setInt(5, customer.getCVR());
            preparedStatement.setString(6, customer.getCustomerType());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
