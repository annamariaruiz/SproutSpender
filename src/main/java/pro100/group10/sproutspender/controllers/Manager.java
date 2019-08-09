package pro100.group10.sproutspender.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;

import pro100.group10.sproutspender.models.Bill;
import pro100.group10.sproutspender.models.Budget;

public class Manager {
	private boolean timeFrame;
	private HashMap<String, Bill> bills = new HashMap<>();
	private Budget[] budgets = new Budget[Budget.CategoryType.values().length];
	
	public Manager() {
		init();
	}
	
	public void init() {
		//populate bills
		//populate budgets
		nextCycle(); //Call the fix for the cycles
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
		
}
