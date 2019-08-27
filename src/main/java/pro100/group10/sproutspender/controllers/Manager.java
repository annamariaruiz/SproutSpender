package pro100.group10.sproutspender.controllers;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import pro100.group10.sproutspender.models.Bill;
import pro100.group10.sproutspender.models.Budget;
import pro100.group10.sproutspender.models.Database;
import pro100.group10.sproutspender.models.User;

@SuppressWarnings("serial")
public class Manager implements Serializable{
	
	public enum Timeframe {
		WEEKLY,
		MONTHLY
	}
	
	private int ID;
	private int UserID;
	private String name;
//	private boolean timeFrame;//285 -> Manager
	private Timeframe timeFrame;//285 -> Manager
	private Date startDate;
	private Date endDate;
	private Date prevEndDate;
	private User user;
	private HashMap<String, Bill> bills = new HashMap<>();
	private ArrayList<Budget> budgets = new ArrayList<>();
	private ArrayList<Budget> allBudgets = new ArrayList<>();
	private HashMap<String, Float> budgetLimits = new HashMap<>();

	public Database db = new Database();
	
	public Manager(Database db, String dbName, User u) { 
		if(u != null) {
			this.db = db;
			init(db, dbName, u);
		} else {
			System.out.println("Login failed.");
		}
		
	}
	
	public Manager(int iD, String name, Timeframe timeFrame, Date startDate, Date endDate, Date prevEndDate, HashMap<String, Float> budgetLimits) {
		ID = iD;
		this.name = name;
		this.timeFrame = timeFrame;
		this.startDate = startDate;
		this.endDate = endDate;
		this.prevEndDate = prevEndDate;
		this.budgetLimits = budgetLimits;
	}
	
	public Manager() {}

	public void init(Database db, String dbName, User u) {
		if(dbName != null) {
			HomeController.manager = db.importManager(dbName, u);
			update(db);
			nextCycleBi();
			if(HomeController.manager.timeFrame != null) {
				nextCycleBu();
			} else {//Make the end Date and previous End Date here since it would need to be null to get in here.
				LocalDate ld = LocalDate.now(); //Find the local date that is today.
				Calendar endCal = Calendar.getInstance(); //Make a new Calendar variable.
				endCal.set(ld.getYear(), ld.getMonthValue() + 1, 1); //Set endCal to be the current year, next month, and the first day 
				endCal.add(Calendar.DATE, -1); //Change the end date to be the last day of the previous month
				java.util.Date endD = endCal.getTime(); //Turn into a java.util.Date object
				Date end = new Date(endD.getTime()); //Turn into a sql.date object
				endDate = end; //Set the class level endDate to be the new end made
				HomeController.manager.endDate = endDate;
				
				Calendar defaul = Calendar.getInstance();//Make a new Calendar variable
				defaul.set(2000, 1, 1);//Set the default date to January 1st 2000
				java.util.Date prevEndD = defaul.getTime(); // Turn into a java.util.date object
				Date prevEnd = new Date(prevEndD.getTime());//Turn into a sql.date object
				prevEndDate = prevEnd;
				HomeController.manager.prevEndDate = prevEndDate;
				
				//Default Start Date
				Calendar startCal = Calendar.getInstance();
				startCal.add(Calendar.MONTH, -1);
				startCal.add(Calendar.DATE, 1);
				java.util.Date startD = startCal.getTime();
				Date start = new Date(startD.getTime());
				startDate = start;
				
				timeFrame = Timeframe.MONTHLY; //Default timeFrame = Month
				HomeController.manager.timeFrame = timeFrame;
				
				//Default limits
				budgetLimits.put("FOOD", (float) 843);
				budgetLimits.put("TRANSPORTATION", (float) 843);
				budgetLimits.put("ENTERTAINMENT", (float) 843);
				budgetLimits.put("MISCELLANEOUS", (float) 843);
				HomeController.manager.budgetLimits = budgetLimits;
			}
			allBudgets.clear();
			for (Budget b : allBudgets) {
				if(b.getEndDate().after(HomeController.manager.startDate)) {//TODO Check and see if the end date is before or after the start date
					budgets.add(b);
				}
			}
		}
	}
	
	public void changeSettings(LocalDate newS, String timeFram, float food, float transit, float entertain, float misc) throws SQLException { //Depending on the new start date, the end date  would change
		Timeframe timeFrame = null;
		if(timeFram.equalsIgnoreCase(Timeframe.MONTHLY.toString())) {
			timeFrame = Timeframe.MONTHLY;
		} else if(timeFram.equalsIgnoreCase(Timeframe.WEEKLY.toString())) {
			timeFrame = Timeframe.WEEKLY;
		}	
		this.timeFrame = timeFrame; //Update the time frame
		HomeController.manager.setTimeFrame(timeFrame);
		
		budgetLimits.clear(); //Update the limits
		budgetLimits.put("FOOD", food);
		budgetLimits.put("TRANSPORTATION", transit);
		budgetLimits.put("ENTERTAINMENT", entertain);
		budgetLimits.put("MISCELLANEOUS", misc);
		HomeController.manager.setBudgetLimits(budgetLimits);
		
		//Ensure the new start date is valid.
		Date newStart = Date.valueOf(newS); //new Start Date
		if(newStart.after(Date.valueOf(LocalDate.now()))) {//If the new start date is in the future
			newStart = newStartFuture(newS, newStart);
		} else if(newStart.before(Date.valueOf(LocalDate.now()))) {
			newStart = newStartPast(newS, newStart);
		}
		startDate = newStart;
		HomeController.manager.setStartDate(newStart);
	
		for (Budget b : allBudgets) {//Fix the budgets to only be the ones located in the current window of time
			if(b.getEndDate().after(startDate)) {
				budgets.add(b);
			}
		}
		
		newCycle(null, newStart, null); //New end date for the manager
		
		 for(Budget b : budgets) { //New end dates and limits for the specified categories
			 b.setEndDate(HomeController.manager.endDate);
			 if(b.getCategory() == Budget.CategoryType.FOOD) {
				 b.setLimit(food);
			 } else if(b.getCategory() == Budget.CategoryType.TRANSPORTATION) {
				 b.setLimit(transit);
			 } else if(b.getCategory() == Budget.CategoryType.ENTERTAINMENT) {
				 b.setLimit(entertain);
			 } else if(b.getCategory() == Budget.CategoryType.MISCELLANEOUS) {
				 b.setLimit(misc);
			 } 
			 db.update(b);
		 }
		 HomeController.manager.setEndDate(HomeController.manager.endDate);
		 db.updateManager(HomeController.manager);
	}

	private Date newStartFuture(LocalDate newS, Date newStart) {
		if(HomeController.manager.timeFrame.equals(Timeframe.WEEKLY)) {//If the timeFrame goes by weeks
		    long diff1 = ChronoUnit.DAYS.between(newS, LocalDate.now());
		    int diff = (int) -diff1; //Difference between the future start date and today
		    if(diff % 7 == 0) { //If there is no remainder then set the new startdate to be today
		    	Calendar official = Calendar.getInstance();
		    	official.set(newS.getYear(), newS.getMonthValue() - 1, newS.getDayOfMonth());
		    	official.add(Calendar.DATE, -diff);
		    	java.util.Date temp = official.getTime();
		    	newStart = new Date(temp.getTime());
		    } else { //If there is a remainder
		    	int base = diff / 7; //How many weeks you can go back without passing today
		    	int newDiff = (base + 1) * 7;
		    	Calendar official = Calendar.getInstance();
		    	official.set(newS.getYear(), newS.getMonthValue() - 1, newS.getDayOfMonth());
		    	official.add(Calendar.DATE, -newDiff);
		    	java.util.Date temp = official.getTime();
		    	newStart = new Date(temp.getTime());
		    }
		} else if(HomeController.manager.timeFrame.equals(Timeframe.MONTHLY)) {//If the timeFrame goes by months
			Calendar startCalendar = new GregorianCalendar();
			startCalendar.setTime(Date.valueOf(LocalDate.now()));
			Calendar endCalendar = new GregorianCalendar();
			endCalendar.setTime(newStart);
			int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
			int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
	    	Calendar official = Calendar.getInstance();
	    	official.set(newS.getYear(), newS.getMonthValue() - 1, newS.getDayOfMonth());
	    	official.add(Calendar.MONTH, -diffMonth);
	    	java.util.Date temp = official.getTime();
	    	newStart = new Date(temp.getTime());
		} 
		return newStart;
	}

	private Date newStartPast(LocalDate newS, Date newStart) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(newStart);
		if(HomeController.manager.timeFrame.equals(Timeframe.WEEKLY)) {
			calendar.add(Calendar.DATE, 7);
		} else if(HomeController.manager.timeFrame.equals(Timeframe.MONTHLY)) {
			calendar.add(Calendar.MONTH, 1);
		}
		java.util.Date endD = calendar.getTime(); //Turn into a java.util.Date object
		Date end = new Date(endD.getTime()); //Turn into a sql.date object
		
		if(end.before(Date.valueOf(LocalDate.now()))) {
			if(HomeController.manager.timeFrame.equals(Timeframe.WEEKLY)) { //Week
				long diff1 = ChronoUnit.DAYS.between(LocalDate.now(), newS);
			    int diff = (int) -diff1; //Difference between the future start date and today
			    if(diff % 7 == 0) { //If there is no remainder then set the new startdate to be today 
			    	Calendar official = Calendar.getInstance();
			    	official.set(newS.getYear(), newS.getMonthValue() - 1, newS.getDayOfMonth());
			    	official.add(Calendar.DATE, diff);
			    	java.util.Date temp = official.getTime();
			    	newStart = new Date(temp.getTime());
			    } else { //If there is a remainder
			    	int base = diff / 7; //How many weeks you can go forward without passing today
			    	int newDiff = base * 7;
			    	Calendar official = Calendar.getInstance();
			    	official.set(newS.getYear(), newS.getMonthValue() - 1, newS.getDayOfMonth());
			    	official.add(Calendar.DATE, newDiff);
			    	java.util.Date temp = official.getTime();
			    	newStart = new Date(temp.getTime());
			    }
			} else { //Month
				Calendar startCalendar = new GregorianCalendar();
				startCalendar.setTime(newStart);
				Calendar endCalendar = new GregorianCalendar();
				endCalendar.setTime(Date.valueOf(LocalDate.now()));
				int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
				int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		    	Calendar official = Calendar.getInstance();
		    	official.set(newS.getYear(), newS.getMonthValue() - 1, newS.getDayOfMonth());
		    	official.add(Calendar.MONTH, diffMonth);
		    	java.util.Date temp = official.getTime();
		    	newStart = new Date(temp.getTime());
			}
		}
		return newStart;
	}
	
	public Date newCycle(LocalDate ld, Date d, Budget b) {
		Date startDay = null;
		if(ld != null) {
			startDay = Date.valueOf(ld);
		} else {
			startDay = d;
		}
		HomeController.manager.startDate = startDay;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDay);
		timeFrame = HomeController.manager.timeFrame;
		if(HomeController.manager.timeFrame.equals(Timeframe.WEEKLY)) {
			calendar.add(Calendar.DATE, 7);
		} else if(timeFrame.equals(Timeframe.MONTHLY)) {
			calendar.add(Calendar.MONTH, 1);
		}
		java.util.Date endD = calendar.getTime(); //Turn into a java.util.Date object
		Date end = new Date(endD.getTime()); //Turn into a sql.date object
		HomeController.manager.endDate = end;
		
		if(b != null ) {
			b.setEndDate(end);
			try {
				HomeController.manager.db.update(b);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return end;
	}
	
	public void nextCycleBu() {
		boolean next = false;
		
		LocalDate ld = LocalDate.now();
		Date today = Date.valueOf(ld);
		
		if(HomeController.manager.endDate.before(today)) {
			next = true;
		}
		
		if(next) {
			newCycle(ld, null, null);
		}
	}
	
	public void nextCycleBi() {
		//Call this when you pay a bill
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
				java.util.Date end = calendar.getTime(); //Turn into a java.util.Date object
				Date due = new Date(end.getTime()); //Turn into a sql.date object
				b.setDate(due);
				b.setPaid(false);
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
		allBudgets = db.selectAllBudg();
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

	public Timeframe getTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(Timeframe timeFrame) {
		this.timeFrame = timeFrame;
	}
		
	public ArrayList<Budget> getBudgets() {
		return budgets;
	}
	
	public HashMap<String, Float> getBudgetLimits() {
		return budgetLimits;
	}

	public Date getPrevEndDate() {
		return prevEndDate;
	}

	
	public User getUser() {
		return user;
	}

	
	public void setUser(User user) {
		this.user = user;
	}

	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	public int getID() {
		return ID;
	}

	
	public void setID(int iD) {
		ID = iD;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setPrevEndDate(Date prevEndDate) {
		this.prevEndDate = prevEndDate;
	}

	public void setBudgetLimits(HashMap<String, Float> budgetLimits) {
		this.budgetLimits = budgetLimits;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}
}
