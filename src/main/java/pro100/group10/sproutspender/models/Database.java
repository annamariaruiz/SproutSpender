package pro100.group10.sproutspender.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import pro100.group10.sproutspender.models.Budget.CategoryType;

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
            createSQL = "IF OBJECT_ID('Budgets') IS NULL CREATE TABLE Budgets(id INT PRIMARY KEY IDENTITY(1, 1), date DATE, limit float, category VARCHAR(25), currentAmount float )";
            stmt.executeUpdate(createSQL);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    public int getLastID() throws SQLException {
    	int lastID = -1;
    	String selectSQL = "SELECT SCOPE_IDENTITY()";
    	ResultSet lastIDRow = null;
    	
    	try(Statement stmt = connection.createStatement()) {
    		lastIDRow = stmt.executeQuery(selectSQL);
    		lastIDRow.next();
    		lastID = lastIDRow.getInt(1);
    	}
    	
    	return lastID;
    }
    
    public int store(Budget b) throws SQLException {
    	String insertSQL =
    		"INSERT INTO " + tableName + "("
				+ "date, "
				+ "limit, "
				+ "category, "
				+ "currentAmount"
			+ ") "
			+ "VALUES('"
				+ b.getDate().toString() + "', "
				+ b.getLimit() + ", "
				+ b.getCategory().toString() + ", "
				+ b.getCurrentAmount()
			+ ")";
    	
    	try(Statement stmt = connection.createStatement()) {
    		stmt.executeUpdate(insertSQL);
    	}
    	
    	return getLastID();
    }
    
    public Budget lookUp(int id) throws SQLException {
    	Budget foundBudg = new Budget();
		
		String selectSQL =
				"SELECT * FROM " + tableName + " WHERE id = " + id;
		
		ResultSet budgRow = null;
		
		try(Statement statement = connection.createStatement()) {
			budgRow = statement.executeQuery(selectSQL);
			budgRow.next();
			
			if(!budgRow.wasNull()) {
				foundBudg.setID(budgRow.getInt("id"));
				foundBudg.setDate(budgRow.getDate("date"));
				foundBudg.setLimit(budgRow.getFloat("limit"));
				foundBudg.setCategory(CategoryType.valueOf(budgRow.getString("category")));
				foundBudg.setCurrentAmount(budgRow.getFloat("currentAmount"));
			}
		}
		
		return foundBudg;
    }

    public List<Budget> lookUpByDay(Date date) throws SQLException {
    	List<Budget> budgetsOnDay = new ArrayList<>();
    	
    	String selectSQL =
    		"SELECT * FROM " + tableName
    		+ " WHERE date = '" + date.toString() + "'";
    	
    	return null;
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
    
    public void remove(int id) throws SQLException {
    	String deleteSQL = "DELETE FROM " + tableName + " WHERE id = " + id;
		
		try(Statement statement = connection.createStatement()) {
			statement.executeUpdate(deleteSQL);
		}
    }
    
    public int size() throws SQLException {
    	int size = -1;
		String selectSQL =
				"SELECT COUNT(*) FROM " + tableName;
		
		ResultSet row = null;
		
		try(Statement statement = connection.createStatement()) {
			row = statement.executeQuery(selectSQL);
			row.next();
			size = row.getInt(1);
		}
		
		return size;
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
