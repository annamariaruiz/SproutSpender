package pro100.group10.sproutspender.models;

import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("serial")
public class Budget implements Serializable{
	public enum CategoryType {
		GENERAL,
		FOOD,
		TRANSPORTATION,
		ENTERTAINMENT,
		MISCELLANEOUS
	}
	//DO THE PREVIOUS END-DATE THING
	public static final CategoryType[] categoryRank = new CategoryType[] { CategoryType.FOOD,
			CategoryType.TRANSPORTATION, CategoryType.ENTERTAINMENT, CategoryType.MISCELLANEOUS, CategoryType.GENERAL };
	
	private int id;
	private Date date;
	private Date endDate;
	private Date prevEndDate;
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
	
	public Date getPrevEndDate() {
		return prevEndDate;
	}

	public void setPrevEndDate(Date prevEndDate) {
		this.prevEndDate = prevEndDate;
	}

	@Override
	public String toString() {
		return String.valueOf(getCurrentAmount());
	}
}
