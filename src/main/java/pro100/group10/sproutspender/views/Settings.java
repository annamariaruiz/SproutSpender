package pro100.group10.sproutspender.views;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import pro100.group10.sproutspender.controllers.HomeController;

public class Settings {

	@FXML
	private RadioButton weekTime;
	@FXML
	private RadioButton monthTime;
	@FXML
	private DatePicker timeStart;
	@FXML
	private Button saveButton;
	@FXML
	private Label alert;
	@FXML
	private Label foodBudgetLimit;
	@FXML
	private Label transBudgetLimit;
	@FXML
	private Label entertainmentBudgetLimit;
	@FXML
	private Label miscBudgetLimit;
	@FXML
	private Slider foodSlider;
	@FXML
	private Slider transSlider;
	@FXML
	private Slider entertainmentSlider;
	@FXML
	private Slider miscSlider;
	
	private Stage settings = new Stage();

	@FXML
	private void saveToFile(ActionEvent ae) throws SQLException {
		LocalDate startDate = timeStart.getValue();
		if (weekTime.isSelected()) {
				Stage stage = (Stage) saveButton.getScene().getWindow();
//				Manager m = new Manager();
				String timeType = "weekly";
				if(weekTime.isSelected()) {
					timeType = "weekly";
				} else if (monthTime.isSelected()) {
					timeType = "monthly";
				}
				LocalDate start = null;
				if(timeStart.getValue() == null) {
					java.util.Date input = HomeController.manager.getStartDate();
					Date date = new Date(input.getTime());
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					start = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
//					start = HomeController.manager.getStartDate();
				} else {
					start = timeStart.getValue();
				}
				float food = (float) foodSlider.getValue();
				float trans = (float) transSlider.getValue();
				float entertainment = (float) entertainmentSlider.getValue();
				float misc = (float) miscSlider.getValue();
				
				HomeController.manager.changeSettings(start, timeType, food, trans, entertainment, misc);
				alert.setText("Saved");
		} else if (monthTime.isSelected()) {
				Stage stage = (Stage) saveButton.getScene().getWindow();
				String timeType = "weekly";
				if(weekTime.isSelected()) {
					timeType = "weekly";
				} else if (monthTime.isSelected()) {
					timeType = "monthly";
				}
				LocalDate start = null;
				if(timeStart.getValue() == null) {
					java.util.Date input = HomeController.manager.getStartDate();
					Date date = new Date(input.getTime());
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					start = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
//						start = HomeController.manager.getStartDate();
				} else {
					start = timeStart.getValue();
				}
				float food = (float) foodSlider.getValue();
				float trans = (float) transSlider.getValue();
				float entertainment = (float) entertainmentSlider.getValue();
				float misc = (float) miscSlider.getValue();
				
				HomeController.manager.changeSettings(start, timeType, food, trans, entertainment, misc);
				alert.setText("Saved");
			
		}
	}
	
	public void init() {
		settings.setTitle("Settings");
		settings.setResizable(false);
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../views/Settings.fxml"));

			Scene scene = new Scene(root, 600, 410);
			scene.getStylesheets().add(getClass().getResource("../views/settings.css").toString());
			settings.setResizable(false);
			settings.setScene(scene);
			settings.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void change() {
		foodBudgetLimit.setText("");
	}
}
