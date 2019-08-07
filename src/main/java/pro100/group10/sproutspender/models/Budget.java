package pro100.group10.sproutspender.models;

import java.sql.Date;

public class Budget {

	int id;
	Date date;
	CategoryType category;
	float limit;
	float currentAmount;
	
	public Budget(int id, Date date, int amount) {
		
	}
}
