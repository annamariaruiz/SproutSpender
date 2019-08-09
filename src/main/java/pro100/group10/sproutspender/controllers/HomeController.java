package pro100.group10.sproutspender.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pro100.group10.sproutspender.models.Database;

public class HomeController {
	
	private Manager m = new Manager();
	
	@FXML
	private Button start;
	
	@FXML
	private TextField dbName;
	
	@FXML
	private TextField username;
	
	@FXML
	private TextField password;
	
	@FXML
	private Label alert;
	
	public void init() {
		boolean valid = m.isValid(dbName.getText().trim());
		boolean empty = dbName.getText().trim().isEmpty();
		if(valid && !empty) {
			alert.setText("");
			
			//create database
			Database db = new Database();
			try {
				db.setConnection(username.getText().trim(), password.getText().trim());
				db.canConnect();
				//call to open table
			} catch (RuntimeException e) {
				alert.setText("Connection failed");
			}
			
		} else if (valid && empty) {
			alert.setText("Database name must not be empty");
		} else if (!valid){
			alert.setText("Database name format incorrect");
		} else {
			System.out.println("Idk, something broke");
		}
	}
}
