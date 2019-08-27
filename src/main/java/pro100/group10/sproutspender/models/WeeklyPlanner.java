package pro100.group10.sproutspender.models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class WeeklyPlanner {

	private Budget day1;
	private Budget day2;
	private Budget day3;
	private Budget day4;
	private Budget day5;
	private Budget day6;
	private Budget day7;
	
	
	public Budget getDay(int day) {
		Method method = null;
		try {
			method = getClass().getMethod("getDay" + day);
		} catch (NoSuchMethodException nsme) {
			Alert alert = new Alert(AlertType.ERROR, "The specified day could not be found. This probably indicates an iterative error.\n" + nsme.getMessage(), ButtonType.CLOSE);
			Optional<ButtonType> response = alert.showAndWait();
			nsme.printStackTrace();
		} catch (SecurityException se) {
			Alert alert = new Alert(AlertType.ERROR, "Something's very wrong. You somehow violated security policy..\n" + se.getMessage(), ButtonType.CLOSE);
			Optional<ButtonType> response = alert.showAndWait();
		}
		
		Budget budg = null;
		try {
			budg = (((Budget) method.invoke(this)));
		} catch (IllegalAccessException iae1) {
			Alert alert = new Alert(AlertType.ERROR, "The budget could not access its own methods.\n" + iae1.getMessage(), ButtonType.CLOSE);
			Optional<ButtonType> response = alert.showAndWait();
		} catch (IllegalArgumentException iae2) {
			Alert alert = new Alert(AlertType.ERROR, "Budget's getDay method was called with the wrong arguments..\n" + iae2.getMessage(), ButtonType.CLOSE);
			Optional<ButtonType> response = alert.showAndWait();
			iae2.printStackTrace();
		} catch (InvocationTargetException ite) {
			Alert alert = new Alert(AlertType.ERROR, "There was a problem calling getDay in budget.\n" + ite.getMessage(), ButtonType.CLOSE);
			Optional<ButtonType> response = alert.showAndWait();
			ite.printStackTrace();
		}
		
		return budg;
	}

	public void setDay(int day, Budget budg) {
		Method method = null;
		try {
			method = getClass().getMethod("setDay" + day, Budget.class);
		} catch (NoSuchMethodException nsme) {
			// TODO write catch block
			nsme.printStackTrace();
		} catch (SecurityException se) {
			// TODO write catch block
			se.printStackTrace();
		}

		try {
			method.invoke(this, budg);
		} catch (IllegalAccessException iae1) {
			// TODO write catch block
			iae1.printStackTrace();
		} catch (IllegalArgumentException iae2) {
			// TODO write catch block
			iae2.printStackTrace();
		} catch (InvocationTargetException ite) {
			// TODO write catch block
			ite.printStackTrace();
		}
	}
	
	public Budget getDay1() {
		return day1;
	}
	public void setDay1(Budget day1) {
		this.day1 = day1;
	}
	public Budget getDay2() {
		return day2;
	}
	public void setDay2(Budget day2) {
		this.day2 = day2;
	}
	public Budget getDay3() {
		return day3;
	}
	public void setDay3(Budget day3) {
		this.day3 = day3;
	}
	public Budget getDay4() {
		return day4;
	}
	public void setDay4(Budget day4) {
		this.day4 = day4;
	}
	public Budget getDay5() {
		return day5;
	}
	public void setDay5(Budget day5) {
		this.day5 = day5;
	}
	public Budget getDay6() {
		return day6;
	}
	public void setDay6(Budget day6) {
		this.day6 = day6;
	}
	public Budget getDay7() {
		return day7;
	}
	public void setDay7(Budget day7) {
		this.day7 = day7;
	}
}
