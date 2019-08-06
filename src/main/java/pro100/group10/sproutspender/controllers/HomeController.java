package pro100.group10.sproutspender.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HomeController {
	
	@FXML
	private Button start;
	
	@FXML
	private TextField dbName;
	
	@FXML
	private Label alert;
	
	public void init() {
		if(dbName.getText().trim().isEmpty()) {
			alert.setText("Database name must not be empty");
			alert.setLayoutX(213);
		} else {
			alert.setText("");
		}
		
		
	}
}
