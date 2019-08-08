package pro100.group10.sproutspender.controllers;

import java.sql.Date;
import java.util.HashMap;

import pro100.group10.sproutspender.models.Bill;

public class Manager {
	private boolean timeFrame;
	private HashMap<String, Bill> bills = new HashMap<>();
	
	public Manager() {}
	
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
