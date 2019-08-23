package pro100.group10.sproutspender.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import pro100.group10.sproutspender.controllers.HomeController;
import pro100.group10.sproutspender.controllers.Manager;
import pro100.group10.sproutspender.models.Bill.TimeFrame;
import pro100.group10.sproutspender.models.Budget.CategoryType;

public class Database {
	
	private Connection connection = null;
	private SQLServerDataSource ds = null;
	private final String TABLE_NAME = "Budgets";
	
	
	public SQLServerDataSource getDS() {
		return ds;
	}

	public void setDS(SQLServerDataSource ds) {
		this.ds = ds;
	}
	
	public Database() {
		ds = new SQLServerDataSource();
	    ds.setServerName("localhost");
	    ds.setPortNumber(Integer.parseInt("1433"));
    }

    public Database(String username, String password, String server, int port, String dbName) throws SQLException {
        ds = new SQLServerDataSource();
        ds.setUser(username);
        ds.setPassword(password);
        ds.setServerName(server);
        ds.setPortNumber(port);
        ds.setDatabaseName(dbName);
        try {
            connection = ds.getConnection();
            checkCreateDB();
        } catch (SQLServerException sqle) {
        	System.out.println("connection unestablished");
        	System.out.println(sqle.getMessage());
        	
        }
    }
    
    public void close() {
    	if(connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException sqle) {
				throw new RuntimeException("Failed to close connection\n", sqle);
			}
		}
    }

    private void checkCreateDB() throws SQLException {
        ds.setDatabaseName("master");
        try(Statement stmt = connection.createStatement()) {
        	String sql = "IF DB_ID('" + ds.getDatabaseName() + "') IS NULL CREATE DATABASE " + ds.getDatabaseName();
        	stmt.executeUpdate(sql);
        }
        
        if(ds.getDatabaseName() == "master") ds.setDatabaseName("SproutSpenderDB");
        connection.close();
        connection = ds.getConnection();
        checkCreateTables();
    }
        
    private void checkCreateTables() throws SQLException {
        String createSQL;
    	try(Statement stmt = connection.createStatement()) {
    		createSQL = "IF OBJECT_ID('Bills') IS NULL CREATE TABLE Bills(id INT PRIMARY KEY IDENTITY(1, 1), name VARCHAR(255), amount float, duedate Date, timeframe VARCHAR(255), paid int)";
            stmt.executeUpdate(createSQL);
            createSQL = "IF OBJECT_ID('Budgets') IS NULL CREATE TABLE Budgets(id INT PRIMARY KEY IDENTITY(1, 1), date DATE, endDate DATE, limit float, category VARCHAR(25), currentAmount float )";
            stmt.executeUpdate(createSQL);
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
    
    public void store(Budget b, boolean call) throws SQLException {
    	Manager man = HomeController.manager;
         try(Statement stmt = connection.createStatement()) {
        	 if(b.getEndDate() == null) b.setEndDate(new Date(0L));
        	 String sql = null;
        	 if(call) {
        		 Date end = man.newCycle(null, b.getDate());
        		 sql = String.format(
                         "INSERT INTO Budgets (limit, category, currentAmount, date, endDate) VALUES (%.2f, '%s', %.2f, '%s', '%s')",
                         b.getLimit(), b.getCategory(), b.getCurrentAmount(), b.getDate(), end);
         	} else if (!call) {
         		sql = String.format(
                        "INSERT INTO Budgets (limit, category, currentAmount, date, endDate) VALUES (%.2f, '%s', %.2f, '%s', '%s')",
                        b.getLimit(), b.getCategory(), b.getCurrentAmount(), b.getDate(), b.getEndDate());
         	}
             stmt.executeUpdate(sql);
         }
    	
    	b.setID(getLastID());
    }
    
    public void createBi(Bill b) throws SQLException {
		 LocalDate now = LocalDate.now();
		 Date date = Date.valueOf(now);
		 int paid = 0;
		 
		 if(b.isPaid()) {
			 paid = 1;
		 } else if(!b.isPaid()) {
			 paid = 0;
		 }
		 
		 try(Statement stmt = connection.createStatement()) {
		     String sql = String.format(
		             "INSERT INTO Bills (name, amount, duedate, timeFrame, paid) VALUES ('%s', %.2f, '%s', '%s', '%d')",
		             b.getName(), b.getAmount(), date.toString(), b.getTimeFrame().toString(), paid);
		     stmt.executeUpdate(sql);
		 }
    }
    
    public Budget lookUp(int id) throws SQLException {
    	Budget foundBudg = new Budget();
		
		String selectSQL =
				"SELECT * FROM " + TABLE_NAME + " WHERE id = " + id;
		
		ResultSet budgRow = null;
		
		try(Statement statement = connection.createStatement()) {
			budgRow = statement.executeQuery(selectSQL);
			budgRow.next();
			
			if(!budgRow.wasNull()) {
				foundBudg.setID(budgRow.getInt("id"));
				foundBudg.setDate(budgRow.getDate("date"));
				foundBudg.setEndDate(budgRow.getDate("endDate"));
				foundBudg.setLimit(budgRow.getFloat("limit"));
				foundBudg.setCategory(CategoryType.valueOf(budgRow.getString("category")));
				foundBudg.setCurrentAmount(budgRow.getFloat("currentAmount"));
			}
		}
		
		return foundBudg;
    }

    public Budget lookUpByDayAndCat(Date date, CategoryType cat) throws SQLException {
    	String selectSQL =
    		"SELECT * FROM " + TABLE_NAME
    		+ " WHERE date = '" + date.toString() + "' AND category = '" + cat.toString() + "'";
    	
    	ResultSet dayRow = null;
    	Budget foundBudg = null;
    	
    	try(Statement stmt = connection.createStatement()) {
    		dayRow = stmt.executeQuery(selectSQL);
    		if(dayRow.next()) {
    			foundBudg = new Budget();
    			foundBudg.setID(dayRow.getInt("id"));
				foundBudg.setDate(dayRow.getDate("date"));
				foundBudg.setEndDate(dayRow.getDate("endDate"));
				foundBudg.setLimit(dayRow.getFloat("limit"));
				foundBudg.setCategory(CategoryType.valueOf(dayRow.getString("category")));
				foundBudg.setCurrentAmount(dayRow.getFloat("currentAmount"));
    		}
    	}
    	
    	return foundBudg;
    }
    
    public void update(Budget b) throws SQLException {
    	String updateSQL =
    		"Update " + TABLE_NAME + " "
				+ "SET date = '" + b.getDate() + "', "
				+ "endDate = '" + b.getEndDate() + "', "
				+ "limit = " + b.getLimit() + ", "
				+ "category = '" + b.getCategory() + "', "
				+ "currentAmount = " + b.getCurrentAmount() + " "
			+ "WHERE id = " + b.getID();
    	
    	try(Statement stmt = connection.createStatement()) {
    		stmt.executeUpdate(updateSQL);
    	}
    }
    
    public void updateBill(Bill b) throws SQLException {
    	int paid = 0;
    	if(b.isPaid()) {
    		paid = 1;
    	}
    	String updateSQL =
    		"Update Bills" + " "
    			+ "SET name = '" + b.getName() + "', "
				+ "duedate = '" + b.getDate().toString() + "', "
				+ "amount = " + b.getAmount() + ", "
				+ "timeframe = '" + b.getTimeFrame().toString() + "', "
				+ "paid = " + paid + " "
			+ "WHERE id = " + b.getId();
    	
    	try(Statement stmt = connection.createStatement()) {
    		stmt.executeUpdate(updateSQL);
    	}
    }
    
    public void remove(int id) throws SQLException {
    	String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id = " + id;
		
		try(Statement statement = connection.createStatement()) {
			statement.executeUpdate(deleteSQL);
		}
    }
    
    public void removeBill(int id) throws SQLException {
    	String deleteSQL = "DELETE FROM Bills WHERE id = " + id;
		
		try(Statement statement = connection.createStatement()) {
			statement.executeUpdate(deleteSQL);
		}
    }
    
    public int size() throws SQLException {
    	int size = -1;
		String selectSQL =
				"SELECT COUNT(*) FROM " + TABLE_NAME;
		
		ResultSet row = null;
		
		try(Statement statement = connection.createStatement()) {
			row = statement.executeQuery(selectSQL);
			row.next();
			size = row.getInt(1);
		}
		
		return size;
    }
    
	public HashMap<String, Bill> selectAllBills() {
		HashMap<String, Bill> bills = new HashMap<>();

		Statement stmt;
		try {
			stmt = connection.createStatement();
			String sql = "SELECT * FROM Bills";
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				String name = rs.getString("name");
				Float amount = rs.getFloat("amount");
				String dateS = rs.getString("duedate");
				Date date = Date.valueOf(dateS);
				int paidi = rs.getInt("paid");
				boolean paid = false;
				if(paidi == 1) {
					paid = true;
				}
				
				String timeFrame = rs.getString("timeFrame");
				TimeFrame timeF = Bill.TimeFrame.BIWEEKLY;
				if(timeF.toString().equalsIgnoreCase(timeFrame)) {
					timeF = Bill.TimeFrame.BIWEEKLY;
				} else if("weekly".equalsIgnoreCase(timeFrame)) {
					timeF = Bill.TimeFrame.WEEKLY;
				} else if("monthly".equalsIgnoreCase(timeFrame)) {
					timeF = Bill.TimeFrame.MONTHLY;
				}
				
				int id = rs.getInt("id");
				Bill b = new Bill(amount, date, name, timeF, paid);
				b.setId(id);
				bills.put(b.getName(), b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bills;
	}
    
	public ArrayList<Budget> selectAllBudg() {
		ArrayList<Budget> budgets = new ArrayList<>();

		Statement stmt;
		try {
			stmt = connection.createStatement();
			String sql = "SELECT * FROM Budgets";
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				Float limit = rs.getFloat("limit");
				Float currentAmount = rs.getFloat("currentAmount");
				String dateS = rs.getString("endDate");
				java.util.Date dateJ = null;
				try {
					dateJ = new SimpleDateFormat("yyyy-MM-dd").parse(dateS);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Date date = new Date(dateJ.getTime());
				
				String enDateS = rs.getString("endDate");
				java.util.Date enDateJ = null;
				try {
					enDateJ = new SimpleDateFormat("yyyy-MM-dd").parse(enDateS);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Date enDate = new Date(enDateJ.getTime());
				
				String category = rs.getString("category");
				CategoryType cat = Budget.CategoryType.GENERAL;
				if(("food").equalsIgnoreCase(category)) {
					cat = Budget.CategoryType.FOOD;
				} else if("transportation".equalsIgnoreCase(category)) {
					cat = Budget.CategoryType.TRANSPORTATION;
				} else if("entertainment".equalsIgnoreCase(category)) {
					cat = Budget.CategoryType.ENTERTAINMENT;
				} else if("miscellaneous".equalsIgnoreCase(category)) {
					cat = Budget.CategoryType.MISCELLANEOUS;
				}
				
				int id = rs.getInt("id");
				Budget b = new Budget(limit, cat, enDate);
				b.setEndDate(date);
				b.setID(id);
				b.setCurrentAmount(currentAmount);
				budgets.add(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return budgets;
	}
    
    public void canConnect() throws RuntimeException {
    	try {
    		connection = ds.getConnection();
    		checkCreateDB();
    	} catch(SQLServerException sse) {
    		throw new RuntimeException("Could not connect to database.\n" + sse);
    	} catch(SQLException sqle) {
    		throw new RuntimeException("Could not create database.\n" + sqle);
    	}
    }
    
    public Connection getConnection() {
		return connection;
    }
    
    public void setConnection(String username, String password, String dbName) {
    	ds.setUser(username);
    	ds.setPassword(password);
    	ds.setDatabaseName(dbName);
    }
}
