package pro100.group10.sproutspender.models;

import java.sql.Date;

public class Bill {
	
	public enum TimeFrame {
		WEEKLY,
		BIWEEKLY,
		MONTHLY
	}
	
	private int id;
	private float amount;
	private Date date; //Date due
	private String name;
	private boolean paid;
	private TimeFrame timeFrame;
	
	
	public Bill() {}
	
	public Bill(float amount, Date date, String name, TimeFrame timeF) {
		setAmount(amount);
		setDate(date);
		setName(name);
		setTimeFrame(timeF);
		setPaid(false);
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

	public TimeFrame isTimeFrame() {
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
	
	

}
