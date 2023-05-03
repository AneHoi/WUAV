package DAL;

import BE.Customer;
import BE.Section;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;
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
    public int createSection(Section section) throws SQLException {
        int id = 0;
        try(Connection conn = db.getConnection()) {
            String sql = "INSERT INTO Section(Section_Title, Section_Sketch, Section_Sketch_Comment, Section_Image, Section_Image_Comment, Section_Description, Section_Made_By_Tech, Section_Report_ID, Section_Addendum_ID) VALUES(?,?,?,?,?,?,?,?,?);";

            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1,section.getSectionTitle());
            stmt.setBytes(2, section.getSketchBytes());
            stmt.setString(3,section.getSketchComment());
            stmt.setBytes(4,section.getImageBytes());
            stmt.setString(5, section.getImageComment());
            stmt.setString(6, section.getDescription());
            stmt.setString(7, section.getMadeByTechnician());
            stmt.setInt(8,section.getReportID());
            stmt.setInt(9,section.getAddendumID());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()){
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not create section "+e);
        }
        return id;
    }
}
