package dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

//Singleton Class
public class DBConnector {
    private static DBConnector dbConnector = null;

    private static final String PROP_FILE = "data/dbCredentials/config.properties";
    private SQLServerDataSource dataSource; //variable for creating a connection to the DB


    protected DBConnector() throws IOException {
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream(PROP_FILE));

        String server = databaseProperties.getProperty("Server");
        String database = databaseProperties.getProperty("Database");
        String user = databaseProperties.getProperty("User");
        String password = databaseProperties.getProperty("Password");

        dataSource = new SQLServerDataSource();
        dataSource.setServerName(server);
        dataSource.setDatabaseName(database);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setTrustServerCertificate(true);
    }

    public static DBConnector getInstance() {
        try {
            if (dbConnector == null) {
                dbConnector = new DBConnector();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dbConnector;
    }

    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }

    public static void main(String[] args) throws Exception {

        DBConnector databaseConnector = new DBConnector().getInstance();

        try (Connection connection = databaseConnector.getConnection()) {
            System.out.println("Is it open? " + !connection.isClosed());
            //TODO test, and get something out of the DB


        } //Connection gets closed here
    }
}


