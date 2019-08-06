package pro100.group10.sproutspender.controllers;

import pro100.group10.sproutspender.models.Bill;

public class Manager {
	private boolean timeFrame;
	
	public Manager() {}
	
	public Bill nextBill() {
		return null;
	}
	
	public boolean isValid() {
		//Is the database name valid.
			//Calls checkDatabases
			//Check if the database name is valid
		return false;
	}
	
	public boolean checkDatabases(String dbt) {
		//Checks if it exists
		return false;
	}

	public boolean isTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(boolean timeFrame) {
		this.timeFrame = timeFrame;
	}
	
	
}
