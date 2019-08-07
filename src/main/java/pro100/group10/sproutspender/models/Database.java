package pro100.group10.sproutspender.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public class Database {
	
	private Connection connection = null;
	private SQLServerDataSource ds = null; 
	private String tableName = null;
	
	public Database() {
		ds = new SQLServerDataSource();
	    ds.setServerName("localhost");
	    ds.setPortNumber(Integer.parseInt("1433"));
    }

    public Database(String username, String password, String server, int port) {
        ds = new SQLServerDataSource();
        ds.setUser(username);
        ds.setPassword(password);
        ds.setServerName(server);
        ds.setPortNumber(port);
        try {
            connection = ds.getConnection();
            checkCreateDB();
        } catch (SQLServerException e) {
        
        }
    }

    private void checkCreateDB() {
        ds.setDatabaseName("master");
        try(Statement stmt = connection.createStatement()) {
        	String sql = "IF DB_ID('SproutSpenderDB') IS NULL CREATE DATABASE SproutSpenderDB";
        	stmt.executeUpdate(sql);
        	
        } catch (SQLException sqle) {
        	System.out.println(sqle);
        }
        
        ds.setDatabaseName("SproutSpenderDB");
        checkCreateTables();
    }
        
    private void checkCreateTables() {
        String createSQL;
    	try(Statement stmt = connection.createStatement()) {
//            createSQL = "IF OBJECT_ID('Bills') IS NULL CREATE TABLE Bills(id INT PRIMARY KEY IDENTITY(1, 1), name VARCHAR(255), amount float, duedate Date, timeframe BIT )";
//            stmt.executeUpdate(sql);
            createSQL = "IF OBJECT_ID('Budgets') IS NULL CREATE TABLE Budgets(id INT PRIMARY KEY IDENTITY(1, 1), limit float, category int, currentAmount float )";
            stmt.executeUpdate(createSQL);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void update(Budget b) throws SQLException {
    	String updateSQL =
    		"Update " + tableName + " "
				+ "SET date = '" + b.getDate().toString() + "', "
				+ "SET limit = " + b.getLimit() + ", "
				+ "SET category = " + b.getCategory().ordinal() + ", "
				+ "SET currentAmount = " + b.getCurrentAmount() + " "
			+ "WHERE id = " + b.getID();
    	
    	try(Statement stmt = connection.createStatement()) {
    		stmt.executeUpdate(updateSQL);
    	}
    }
    
    public void canConnect() throws RuntimeException {
    	try {
    		connection = ds.getConnection();
    		checkCreateDB();
    	} catch(SQLServerException sqle) {
    		throw new RuntimeException("Could not connect to database.\n");
    	}
    }
    
    public void setConnection(String username, String password) {
    	ds.setUser(username);
    	ds.setPassword(password);
    }
}
