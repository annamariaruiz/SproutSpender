package pro100.group10.sproutspender.models;

import java.sql.Date;

public class Budget {
	public enum CategoryType {
		GENERAL
	}
	
	private int id;
	private Date date;
	private float limit;
	private CategoryType category;
	private float currentAmount;
	

	public Budget(float limit, CategoryType category, Date date) {
		setLimit(limit);
		setCategory(category);
		setDate(date);
	}
	
	
	public float getLimit() {
		return limit;
	}

	public void setLimit(float limit) {
		this.limit = limit;
	}

	public CategoryType getCategory() {
		return category;
	}

	public void setCategory(CategoryType category) {
		this.category = category;
	}

	public float getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(float currentAmount) {
		this.currentAmount = currentAmount;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getID() {
		return id;
	}


	public void setID(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
