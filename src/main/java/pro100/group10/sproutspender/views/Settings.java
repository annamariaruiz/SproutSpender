package pro100.group10.sproutspender.views;

import java.sql.SQLException;
import java.time.LocalDate;

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
import pro100.group10.sproutspender.controllers.Manager;

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
					HomeController.manager.getStartDate();
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
			if (startDate != null) {
				// newCycleM(startDate);
				Stage stage = (Stage) saveButton.getScene().getWindow();
				stage.close();
			} else {
				alert.setText("Please pick a date");
			}
		}
	}
	
	public void init() {
		settings.setTitle("Settings");
		settings.setResizable(false);
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../views/Settings.fxml"));

			Scene scene = new Scene(root, 600, 400);
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
