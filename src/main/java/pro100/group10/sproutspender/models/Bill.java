package pro100.group10.sproutspender.models;

import java.sql.Date;

public class Bill {
	private int id;
	private float amount;
	private Date date;
	private String name;
	private boolean timeFrame;
	
	public Bill(float amount, Date date, String name, boolean timeF) {
		setAmount(amount);
		setDate(date);
		setName(name);
		setTimeFrame(timeF);
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

	public boolean isTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(boolean timeFrame) {
		this.timeFrame = timeFrame;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
