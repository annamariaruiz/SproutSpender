package pro100.group10.sproutspender.models;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class Bill implements Serializable {
	
	public enum TimeFrame {
		WEEKLY,
		BIWEEKLY,
		MONTHLY
	}
	
	private int id;
	private int ManagerID;
	private float amount;
	private Date date; //Date due
	private String name;
	private boolean paid;
	private TimeFrame timeFrame;
	
	
	public Bill() {}
	
	public Bill(float amount, Date date, String name, TimeFrame timeF, boolean paid) {
		setAmount(amount);
		setDate(date);
		setName(name);
		setTimeFrame(timeF);
		setPaid(paid);
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TimeFrame getTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(TimeFrame timeFrame) {
		this.timeFrame = timeFrame;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public int getManagerID() {
		return ManagerID;
	}

	public void setManagerID(int managerID) {
		ManagerID = managerID;
	}
}
