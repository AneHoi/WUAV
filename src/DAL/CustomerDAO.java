package DAL;

import BE.Customer;
import DAL.Interfaces.ICustomerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements ICustomerDAO {

    private DBConnector db;

    public CustomerDAO() {
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

                Customer customerVar = new Customer(id, name, address, tlf, mail, cvr, type);
                customers.add(customerVar);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return customers;
    }
    public Customer getChosenCustomer(int chosenCustomer) throws SQLException {
        Customer customerVar = null;
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM Customer WHERE Customer.Customer_ID = " + chosenCustomer + ";";
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

                customerVar = new Customer(id, name, address, tlf, mail, cvr, type);
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return customerVar;
    }
    public void saveCustomer(Customer customerVar) {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Customer" + "(Customer_Name, Customer_Address, Customer_Mail, Customer_Tlf, Customer_CVR, Customer_Type)" + "VALUES(?,?,?,?,?,?);";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, customerVar.getCustomerName());
            stmt.setString(2, customerVar.getAddress());
            stmt.setString(3, customerVar.getPhoneNumber());
            stmt.setString(4, customerVar.getEmail());
            stmt.setInt(5, customerVar.getCVR());
            stmt.setString(6, customerVar.getCustomerType());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateCustomer(Customer customerVar) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = """
                    UPDATE Customer SET Customer_Name = (?), 
                    Customer_Address = (?), 
                    Customer_Mail = (?), 
                    Customer_Tlf = (?), 
                    Customer_CVR = (?),
                    Customer_Type = (?)
                    WHERE Customer_ID = """ + customerVar.getCustomerID() + ";";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, customerVar.getCustomerName());
            ps.setString(2, customerVar.getAddress());
            ps.setString(3, customerVar.getEmail());
            ps.setString(4, customerVar.getPhoneNumber());
            ps.setInt(5, customerVar.getCVR());
            ps.setString(6, customerVar.getCustomerType());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void deleteCustomer(Customer customer) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "DELETE FROM Customer WHERE Customer_ID = " + customer.getCustomerID() + ";";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

}
