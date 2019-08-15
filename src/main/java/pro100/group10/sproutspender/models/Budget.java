package pro100.group10.sproutspender.models;

import java.sql.Date;

public class Budget {
	public enum CategoryType {
		GENERAL,
		FOOD,
		TRANSPORTATION,
		ENTERTAINMENT,
		MISCELLANEOUS
	}
	
	public static final CategoryType[] categoryRank = new CategoryType[] { CategoryType.FOOD,
			CategoryType.TRANSPORTATION, CategoryType.ENTERTAINMENT, CategoryType.MISCELLANEOUS, CategoryType.GENERAL };
	
	private int id;
	private Date date;
	private Date endDate;
	private float limit = 0;
	private CategoryType category;
	private float currentAmount = 0;
	
	public Budget() {}
	
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


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public String toString() {
		return String.valueOf(getCurrentAmount());
	}
}
