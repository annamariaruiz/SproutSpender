package pro100.group10.sproutspender.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public class Database {
	
	private Connection connection = null;
	private SQLServerDataSource ds = null; 
	
	public Database() {
		ds = new SQLServerDataSource();
		ds.setUser("sproutspender");
		ds.setPassword("sproutspender");
	    ds.setServerName("localhost");
	    ds.setPortNumber(Integer.parseInt("1433"));
	    try {
	        connection = ds.getConnection();
	        checkCreateDB();
	    } catch (SQLServerException sqle) {
	    	
	    }
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
        try(Statement stmt = connection.createStatement()) {
            String sql = "IF OBJECT_ID('Bills') IS NULL CREATE TABLE Bills(id INT PRIMARY KEY IDENTITY(1, 1), name VARCHAR(255), amount float, duedate Date, timeframe BIT )";
            stmt.executeUpdate(sql);
            sql = "IF OBJECT_ID('Budgets') IS NULL CREATE TABLE Budgets(id INT PRIMARY KEY IDENTITY(1, 1), limit float, category VARCHAR(255), currentAmount float )";
            stmt.executeUpdate(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
