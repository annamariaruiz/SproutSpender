package pro100.group10.sproutspender.views;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import pro100.group10.sproutspender.app.Main;

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
	private void saveToFile() {
		LocalDate startDate = timeStart.getValue();
		if (weekTime.isSelected()) {
			if (startDate != null) {
				// newCycleW(startDate);
				// close();
				alert.setText("saved");
			} else {
				alert.setText("pick a date ma dude");
			}
		} else if (monthTime.isSelected()) {
			if (startDate != null) {
				// newCycleM(startDate);
				// close();
				alert.setText("saved");
			} else {
				alert.setText("pick a date ma dude");
			}
		}

	}

	public void init() {
		Stage stage = Main.getStage();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../views/Settings.fxml"));

			Scene scene = new Scene(root, 600, 400);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
