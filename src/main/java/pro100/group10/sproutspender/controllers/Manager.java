package pro100.group10.sproutspender.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;

import pro100.group10.sproutspender.models.Bill;
import pro100.group10.sproutspender.models.Budget;

public class Manager {
	private boolean timeFrame;
	private Date endDate;
	private HashMap<String, Bill> bills = new HashMap<>();
	private Budget[] budgets = new Budget[Budget.CategoryType.values().length];
	
	public Manager() {}
	
	public Manager(String dbName) { 
		init(dbName);
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Manager m = new Manager();
	}
	
	public void init(String dbName) {
		deserialize(dbName);
		if(endDate == null) {
			LocalDate ld = LocalDate.now();
			Calendar today = Calendar.getInstance();
			today.clear();
			today.set(ld.getYear(), ld.getMonthValue(), 1);
			today.add(Calendar.DATE, -1);
			java.util.Date endD = today.getTime();
			Date end = new Date(endD.getTime());
			
			timeFrame = false;
			System.out.println(end);
			
			System.out.println("hello darkness my old friend");
		}
//		when manager is deserialized, it needs to populate the bills statement and the budget statement
		
		//populate bills
		//populate budgets
//		nextCycle(); //Call the fix for the cycles
	}

	public void newCycleW(LocalDate ld) {
		Date startDate = Date.valueOf(ld);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DATE, 7);
		Date end = (Date) calendar.getTime();
		
		for(Budget b : budgets) {
			b.setEndDate(end);
		}
	}
	
	public void newCycleM(LocalDate ld) {
		Date startDate = Date.valueOf(ld);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);		
		calendar.add(Calendar.MONTH, 1);
		Date end = (Date) calendar.getTime();
		
		for(Budget b : budgets) {
			b.setEndDate(end);
		}
	}
	
	public void nextCycle() {
		//Either use this global one or pass in the budget instead
		Budget b = budgets[0];
		boolean next = false;
		
		LocalDate ld = LocalDate.now();
		Date today = Date.valueOf(ld);
		
		if(b.getEndDate().before(today)) {
			next = true;
		}
		
		if(next && timeFrame) {
			newCycleW(ld);
		} else if(next && !timeFrame) {
			newCycleM(ld);
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
	
	//CRUD STATEMENT for bills may be irrelevant ~ Make some from the database
	public void addBill(Bill b) {
		bills.put(b.getName(), b);
	}
	
	public void updateBill(Bill b) {
		for(String name : bills.keySet()) {
			if(b.getName().equals(name)) {
				bills.replace(name, b);
			}
		}
	}
	
	public void deleteBill(Bill b) {
		for(String name : bills.keySet()) {
			if(b.getName().equals(name)) {
				bills.remove(name, bills.get(name));
			}
		}
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

			// Saving object into a file
			fileOut = new FileOutputStream(fileName);
			out = new ObjectOutputStream(fileOut);

			// Serialization of the object
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
	
				// Reading the object from a file
				fileIn = new FileInputStream(path);
				in = new ObjectInputStream(fileIn);
	
				// Deserialization of the object
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
