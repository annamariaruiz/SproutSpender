package pro100.group10.sproutspender.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;

import pro100.group10.sproutspender.models.Bill;
import pro100.group10.sproutspender.models.Budget;
import pro100.group10.sproutspender.models.Database;

@SuppressWarnings("serial")
public class Manager implements Serializable{
	private boolean timeFrame;
	private Date endDate;
	private HashMap<String, Bill> bills = new HashMap<>();
	private Budget[] budgets = new Budget[Budget.CategoryType.values().length];
	private Database db = new Database();
	
	public Manager(Database db, String dbName) { 
		this.db = db;
		init(db, dbName);
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Manager m = new Manager(new Database(), "HelloDarkness");
	}
	
	public void init(Database db, String dbName) {
		
		if(dbName != null) {
			HomeController.manager = deserialize(dbName);
			if(endDate != null) {
				LocalDate ld = LocalDate.now();
				Calendar today = Calendar.getInstance();
				today.clear();
				today.set(ld.getYear(), ld.getMonthValue(), 1);
				today.add(Calendar.DATE, -1);
				java.util.Date endD = today.getTime();
				Date end = new Date(endD.getTime());
				
				endDate = end;
				timeFrame = false;
				for(Budget b : budgets) {
					b.setEndDate(end);
				}
				nextCycleBu();
				nextCycleBi();
			}
		}
	}

	public void newCycle(LocalDate ld) {
		Date startDate = Date.valueOf(ld);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		if(timeFrame) {
			calendar.add(Calendar.DATE, 7);
		} else if(!timeFrame ) {
			calendar.add(Calendar.MONTH, 1);
		}
		
		Date end = (Date) calendar.getTime();
		
		for(int i = 0; i < budgets.length; i++) {
			budgets[i].setEndDate(end);
			
			Budget b = budgets[i];
			
			try {
				db.update(b);
			} catch (SQLException e) {e.printStackTrace();};
			update(db);
		}
	}
	
	public void nextCycleBu() {
		boolean next = false;
		
		LocalDate ld = LocalDate.now();
		Date today = Date.valueOf(ld);
		
		if(endDate.before(today)) {
			next = true;
		}
		
		if(next) {
			newCycle(ld);
		}
	}
	
	public void nextCycleBi() {
		boolean next = false;
		
		for(String bill : bills.keySet()) {
			if(bills.get(bill).isPaid()) {
				next = true;
			}
			
			if(next) {
				Bill b = bills.get(bill);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(bills.get(bill).getDate());		
				if(bills.get(bill).getTimeFrame() == Bill.TimeFrame.BIWEEKLY) {
					calendar.add(Calendar.DATE, 14);
				} else if(bills.get(bill).getTimeFrame() == Bill.TimeFrame.MONTHLY) {
					calendar.add(Calendar.MONTH, 1);
				} else if(bills.get(bill).getTimeFrame() == Bill.TimeFrame.WEEKLY) {
					calendar.add(Calendar.DATE, 7);
				}
				Date due = (Date) calendar.getTime();
				b.setDate(due);
				try {
					db.updateBill(b);
				} catch (SQLException e) {e.printStackTrace();};
				update(db);
			}
		}
	}
	
	public Bill nextBill() {
		Date d = null;
		Bill next = null;

		for(String name : bills.keySet()) {
			if(d == null) {
				d = bills.get(name).getDate();
			} else if(bills.get(name).getDate().before(d) && d != null) {
				d = bills.get(name).getDate();
				next = bills.get(name);
			}
		}
		return next;
	}
	
	public void update(Database db) {
		bills = db.selectAllBills();
		budgets = db.selectAllBudg();
	}
	
	public boolean isValid(String str) {
		boolean vali = true;
		if(str.contains("/") || str.contains("\\") || str.contains("~")	|| str.contains("#") || str.contains("%") 
				|| str.contains("&") || str.contains("*") || str.contains("{") || str.contains("}") 
				|| str.contains(":") || str.contains("?") || str.contains("+") || str.contains("|") 
				|| str.contains("\"") || str.contains("'") || str.contains("$") || str.contains("!") 
				|| str.contains("@")) {
			vali = false;
		}
		return vali;
	}

	public boolean isTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(boolean timeFrame) {
		this.timeFrame = timeFrame;
	}
	
	public void serialize(Manager m, String dbName) {
		String path = ".\\src\\managers\\";
		FileOutputStream fileOut = null;
		ObjectOutputStream out = null;

		String fileName = path + dbName + ".ser";
		
		try {
			fileOut = new FileOutputStream(fileName);
			out = new ObjectOutputStream(fileOut);
			out.writeObject(m);

		} catch (IOException ioe) {
			System.out.println("Exception is caught");
		} finally {
			try {
			out.close();
			fileOut.close();
			System.out.println("Serialization completed.");
			} catch(IOException ioe2) {
				System.out.println("Exception is caught");
			} catch(NullPointerException npe) {
				
			}
		}
	}
	
	public Manager deserialize(String dbName) {
		String path = ".\\src\\managers\\" + dbName + ".ser";
		
		File target = new File(path);
		
		Manager found = null;
		
		if(target.exists()) {

			FileInputStream fileIn = null;
			ObjectInputStream in = null;
		
			try {
				fileIn = new FileInputStream(path);
				in = new ObjectInputStream(fileIn);
				found = (Manager) in.readObject();
	
			} catch (IOException ioe) {
				System.out.println("Exception is caught");
			} catch (ClassNotFoundException ex) {
				System.out.println("ClassNotFoundException is caught.");
			} finally {
				try {
				in.close();
				fileIn.close();
				System.out.println("Deserialization completed.");
				} catch(IOException ioe2) {
					System.out.println("Exception is caught");
				}
			}
		}		
		
		return found;
	}
		
}
