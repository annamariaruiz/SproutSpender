package pro100.group10.sproutspender.views;

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
import javafx.stage.Stage;

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
	
	private Stage settings = new Stage();

	@FXML
	private void saveToFile(ActionEvent ae) {
		LocalDate startDate = timeStart.getValue();
		if (weekTime.isSelected()) {
			if (startDate != null) {
				// newCycleW(startDate);
				Stage stage = (Stage) saveButton.getScene().getWindow();
				alert.setText("Saved");
			} else {
				alert.setText("Please pick a date");
			}
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
}
